package spoon.gameZone.ladder.service;

import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import spoon.bet.entity.QBetItem;
import spoon.bet.repository.BetItemRepository;
import spoon.common.net.HttpParsing;
import spoon.common.utils.*;
import spoon.config.domain.Config;
import spoon.config.service.ConfigService;
import spoon.game.domain.MenuCode;
import spoon.gameZone.ZoneConfig;
import spoon.gameZone.ZoneDto;
import spoon.gameZone.ladder.*;
import spoon.member.domain.Role;
import spoon.member.entity.Member;
import spoon.member.service.MemberService;
import spoon.support.web.AjaxResult;

import java.util.Date;

@Slf4j
@AllArgsConstructor
@Service
public class LadderServiceImpl implements LadderService {

    private ConfigService configService;

    private MemberService memberService;

    private LadderGameService ladderGameService;

    private LadderBotService ladderBotService;

    private LadderRepository ladderRepository;

    private BetItemRepository betItemRepository;

    private static QLadder q = QLadder.ladder;

    @Transactional
    @Override
    public boolean updateConfig(LadderConfig ladderConfig) {
        try {
            configService.updateZoneConfig("ladder", JsonUtils.toString(ladderConfig));
            ZoneConfig.setLadder(ladderConfig);
            // 이미 등록된 게임의 배당을 변경한다.
            ladderRepository.updateOdds(ZoneConfig.getLadder().getOdds());
        } catch (RuntimeException e) {
            log.error("사다리 설정 변경에 실패하였습니다. - {}", e.getMessage());
            log.info("{}", ErrorUtils.trace(e.getStackTrace()));
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public Iterable<Ladder> getComplete() {
        return ladderRepository.findAll(q.closing.isFalse(), new Sort(Sort.Direction.ASC, "sdate"));
    }

    @Override
    public Page<Ladder> getClosing(ZoneDto.Command command, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder(q.closing.isTrue());

        // 날짜별 검색
        if (StringUtils.notEmpty(command.getGameDate())) {
            builder.and(q.sdate.like(DateUtils.sdate(command.getGameDate())));
        }
        // 회차별 검색
        if (command.getRound() != null) {
            builder.and(q.round.eq(command.getRound()));
        }

        return ladderRepository.findAll(builder, pageable);
    }

    @Override
    public LadderDto.Score findScore(Long id) {
        Ladder ladder = ladderRepository.findOne(id);

        LadderDto.Score score = new LadderDto.Score();
        score.setId(ladder.getId());
        score.setRound(ladder.getRound());
        score.setGameDate(ladder.getGameDate());
        score.setStart(ladder.getStart());
        score.setLine(ladder.getLine());
        score.setOddeven(ladder.getOddeven());
        score.setCancel(ladder.isCancel());

        // 봇 연결
        String json = HttpParsing.getJson(Config.getSysConfig().getZone().getLadderUrl() + "?sdate=" + ladder.getSdate());
        if (json == null) return score;

        ladder = JsonUtils.toModel(json, Ladder.class);
        if (ladder == null) return score;

        // 봇에 결과가 있다면
        if (ladder.isClosing()) {
            score.setOddeven(ladder.getOddeven());
            score.setLine(ladder.getLine());
            score.setStart(ladder.getStart());

            if (!"ODD".equals(score.getOddeven()) && !"EVEN".equals(score.getOddeven())) {
                score.setCancel(true);
            }
        }

        return score;
    }

    @Transactional
    @Override
    public boolean closingGame(LadderDto.Score score) {
        Ladder ladder = ladderRepository.findOne(score.getId());

        try {
            if (ladder.isChangeResult(score)) {
                // 현재 지급된 머니 포인트를 되돌린다.
                ladderGameService.rollbackPayment(ladder);
            }

            // 스코어 입력
            ladder.updateScore(score);
            ladderRepository.saveAndFlush(ladder);
            ladderGameService.closingBetting(ladder);
            ladderBotService.checkResult();
        } catch (RuntimeException e) {
            log.error("사다리 {} - {}회차 수동처리를 하지 못하였습니다. - {}", ladder.getGameDate(), ladder.getRound(), e.getMessage());
            log.info("{}", ErrorUtils.trace(e.getStackTrace()));
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Transactional
    @Override
    public AjaxResult closingAllGame() {
        QBetItem qi = QBetItem.betItem;
        int total = 0;
        int closing = 0;

        Iterable<Ladder> iterable = ladderRepository.findAll(q.closing.isFalse().and(q.gameDate.before(DateUtils.beforeMinutes(5))));
        for (Ladder ladder : iterable) {
            total++;
            long count = betItemRepository.count(qi.menuCode.eq(MenuCode.LADDER).and(qi.groupId.eq(ladder.getSdate())).and(qi.cancel.isFalse()));
            if (count > 0) continue;

            String json = HttpParsing.getJson(Config.getSysConfig().getZone().getLadderUrl() + "?sdate=" + ladder.getSdate());
            if (json == null) continue;

            Ladder result = JsonUtils.toModel(json, Ladder.class);
            if (result == null) continue;

            if (result.isClosing()) {
                ladder.setStart(result.getStart());
                ladder.setLine(result.getLine());
                ladder.setOddeven(result.getOddeven());
                ladder.setClosing(true);
                ladderRepository.saveAndFlush(ladder);
                closing++;
            }
        }
        ladderBotService.checkResult();
        return new AjaxResult(true, "전체 " + total + "경기중 " + closing + "경기를 종료처리 했습니다.");
    }

    @Override
    public LadderDto.Config gameConfig() {
        LadderDto.Config gameConfig = new LadderDto.Config();
        LadderConfig config = ZoneConfig.getLadder();

        Date gameDate = config.getZoneMaker().getGameDate();
        Ladder ladder = ladderRepository.findOne(q.gameDate.eq(gameDate));

        if (ladder == null) {
            gameConfig.setGameDate(gameDate);
            gameConfig.setRound(config.getZoneMaker().getRound());
            return gameConfig;
        }

        String userid = WebUtils.userid();
        if (userid == null) return gameConfig;

        Member member = memberService.getMember(userid);
        int level = member.getLevel();
        gameConfig.setEnabled(config.isEnabled() && Config.getSysConfig().getZone().isLadder());
        if (member.getRole() == Role.DUMMY) {
            gameConfig.setMoney(10000000);
        } else {
            gameConfig.setMoney(member.getMoney());
        }
        gameConfig.setGameDate(ladder.getGameDate());
        gameConfig.setSdate(ladder.getSdate());
        gameConfig.setRound(ladder.getRound());
        gameConfig.setWin(config.getWin()[level]);
        gameConfig.setMax(config.getMax()[level]);
        gameConfig.setMin(config.getMin()[level]);
        gameConfig.setOdds(ladder.getOdds());

        int betTime = (int) (ladder.getGameDate().getTime() - new Date().getTime() - config.getBetTime() * 1000) / 1000;
        if (betTime < 0) betTime = 0;
        gameConfig.setBetTime(betTime);

        gameConfig.setOddeven(config.isOddeven());
        gameConfig.setStart(config.isStart());
        gameConfig.setLine(config.isLine());
        gameConfig.setLineStart(config.isLineStart());

        return gameConfig;
    }
}
