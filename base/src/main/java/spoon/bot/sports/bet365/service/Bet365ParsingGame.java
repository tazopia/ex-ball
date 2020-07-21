package spoon.bot.sports.bet365.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import spoon.bot.sports.bet365.domain.BotBet365;
import spoon.bot.sports.service.ParsingGame;
import spoon.common.net.HttpParsing;
import spoon.common.utils.JsonUtils;
import spoon.common.utils.WebUtils;
import spoon.config.domain.Config;
import spoon.game.domain.GameCode;
import spoon.game.domain.MenuCode;
import spoon.game.entity.Game;
import spoon.game.entity.League;
import spoon.game.entity.Sports;
import spoon.game.entity.Team;
import spoon.game.service.GameBotService;
import spoon.game.service.sports.LeagueService;
import spoon.game.service.sports.SportsService;
import spoon.game.service.sports.TeamService;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class Bet365ParsingGame implements ParsingGame {

    private GameBotService gameBotService;

    private SportsService sportsService;

    private LeagueService leagueService;

    private TeamService teamService;

    private static String ut;

    private static int cnt = 0;

    private static boolean action = false;

    @Override
    @Async
    public void parsingGame() {
        if (action) return;

        action = true;
        int saved = 0;
        int updated = 0;

        cnt++;
        if (cnt > 10) {
            cnt = 0;
            ut = null;
        }

        String json = HttpParsing.getJson(getParsingUrl());
        if (json == null) {
            action = false;
            return;
        }

        List<BotBet365> list = JsonUtils.toBet365List(json);
        if (list == null) {
            action = false;
            return;
        }

        for (BotBet365 bot : list) {
            if (ut == null || ut.compareTo(bot.getUt()) < 0) ut = bot.getUt();

            League league = Config.getLeagueMap().get(bot.leagueKey());
            if (!league.isEnabled()) continue;

            if (gameBotService.isExist(bot.getSiteCode(), bot.getSiteId())) {
                if (updateGame(bot)) {
                    updated++;
                }
            } else {
                if (addGame(bot)) {
                    saved++;
                }
            }
        }
        log.debug("Bet365 게임 업데이트 - 전체 : {}, 신규: {}, 업데이트: {}", list.size(), saved, updated);
        action = false;
    }

    @Override
    public void resetUt(String update) {
        ut = update;
    }

    private boolean addGame(BotBet365 bot) {
        Game game = makeGame(bot);
        updateSports(game.getSportsBean());
        updateLeague(game.getLeagueBean(), bot.getLeagueFlag());
        updateTeam(game.getTeamHomeBean());
        updateTeam(game.getTeamAwayBean());

        return gameBotService.addGame(game);
    }

    private boolean updateGame(BotBet365 bot) {
        Game game = gameBotService.getGame(bot.getSiteCode(), bot.getSiteId());
        // 자동 업데이트가 아니라면 업데이트를 하지 않는다.
        if (!game.isAutoUpdate()) return false;
        // 업데이트가 바뀌지 않으면 업데이트를 하지 않는다.
        if (bot.getUt().equals(game.getUt())) return false;

        copyGame(bot, game);

        updateSports(game.getSportsBean());
        updateLeague(game.getLeagueBean(), bot.getLeagueFlag());
        updateTeam(game.getTeamHomeBean());
        updateTeam(game.getTeamAwayBean());

        return gameBotService.updateGame(game);
    }

    private Game makeGame(BotBet365 bot) {
        Game game = new Game();
        game.setSiteCode(bot.getSiteCode());
        game.setSiteId(bot.getSiteId());
        game.setGroupId(bot.getGroupId());
        game.setGameDate(bot.getGameDate());
        game.setSports(bot.getSports());
        game.setMenuCode(bot.getMenuCode());
        game.setGameCode(bot.getGameCode());
        game.setSpecial(bot.getSpecial());
        game.setLeague(bot.getLeague());
        game.setTeamHome(bot.getTeamHome());
        game.setTeamAway(bot.getTeamAway());
        game.setHandicap(bot.getHandicap());
        game.setBetHome(bot.isBetHome());
        game.setBetDraw(bot.isBetDraw());
        game.setBetAway(bot.isBetAway());
        game.setDeleted(bot.isDel());
        game.setClosing(bot.isClosing());
        game.setCancel(bot.isCancel());
        game.setSort(bot.getSort());
        game.setUt(bot.getUt());
        game.setAutoUpdate(autoUpdate());
        game.setEnabled(autoParsing());
        makeOdds(bot, game);

        return game;
    }

    private void copyGame(BotBet365 bot, Game game) {
        game.setGameDate(bot.getGameDate());
        game.setSports(bot.getSports());
        game.setMenuCode(bot.getMenuCode());
        game.setGameCode(bot.getGameCode());
        game.setSpecial(bot.getSpecial());
        game.setLeague(bot.getLeague());
        game.setTeamHome(bot.getTeamHome());
        game.setTeamAway(bot.getTeamAway());
        game.setHandicap(bot.getHandicap());
        game.setBetHome(bot.isBetHome());
        game.setBetDraw(bot.isBetDraw());
        game.setBetAway(bot.isBetAway());
        game.setClosing(bot.isClosing());
        game.setCancel(bot.isCancel());
        game.setSort(bot.getSort());
        game.setUt(bot.getUt());

        if (!game.isEnabled()) {
            game.setDeleted(bot.isDel());
        }

        makeOdds(bot, game);
    }

    private void makeOdds(BotBet365 bot, Game game) {
        double oddsUpDown = getOddsUpDown(bot.getGameCode(), bot.getMenuCode());
        double oddsPlus = getOddsPlus(bot.getGameCode(), bot.getMenuCode());
        double oddsDraw = 0D;

        if (oddsUpDown == 100D && oddsPlus == 0D) {
            game.updateOdds(bot.getOddsHome(), bot.getOddsDraw(), bot.getOddsAway());
        } else {
            double oddsHome = convertOdds(bot.getOddsHome(), oddsUpDown, oddsPlus);
            // 승무패 무가 없는 경우
            if (bot.getOddsDraw() != 0) {
                oddsDraw = convertOdds(bot.getOddsDraw(), oddsUpDown, oddsPlus);
            }
            double oddsAway = convertOdds(bot.getOddsAway(), oddsUpDown, oddsPlus);
            game.updateOdds(oddsHome, oddsDraw, oddsAway);
        }
    }

    private double getOddsUpDown(GameCode gameCode, MenuCode menuCode) {
        double odds = 100D;
        switch (gameCode) {
            case MATCH:
                odds = Config.getGameConfig().getOddsUpDownMatch();
                break;
            case HANDICAP:
                odds = menuCode == MenuCode.SPECIAL ? Config.getGameConfig().getOddsUpDownHandicapSpecial() : Config.getGameConfig().getOddsUpDownHandicap();
                break;
            case OVER_UNDER:
                odds = menuCode == MenuCode.SPECIAL ? Config.getGameConfig().getOddsUpDownOverUnderSpecial() : Config.getGameConfig().getOddsUpDownOverUnder();
                break;
        }
        return odds;
    }

    private double getOddsPlus(GameCode gameCode, MenuCode menuCode) {
        double odds = 0D;
        switch (gameCode) {
            case MATCH:
                odds = Config.getGameConfig().getOddsPlusMatch();
                break;
            case HANDICAP:
                odds = menuCode == MenuCode.SPECIAL ? Config.getGameConfig().getOddsPlusHandicapSpecial() : Config.getGameConfig().getOddsPlusHandicap();
                break;
            case OVER_UNDER:
                odds = menuCode == MenuCode.SPECIAL ? Config.getGameConfig().getOddsPlusOverUnderSpecial() : Config.getGameConfig().getOddsPlusOverUnder();
                break;
        }
        return odds;
    }

    private double convertOdds(double odds, double oddsRate, double oddsPlusRate) {
        if (oddsRate != 100D) {
            odds = BigDecimal.valueOf(odds)
                    .multiply(BigDecimal.valueOf(oddsRate))
                    .divide(BigDecimal.valueOf(100D), 2, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
        }
        if (oddsPlusRate != 0D) {
            odds = BigDecimal.valueOf(odds).add(BigDecimal.valueOf(oddsPlusRate)).doubleValue();
        }
        return odds;
    }

    private void updateTeam(Team team) {
        if (team.getId() == null || team.getId() == 0) {
            teamService.addTeam(team);
        }
    }

    private void updateLeague(League league, String flag) {
        if (league.getId() == null || league.getId() == 0) {
            leagueService.addLeague(league, flag);
        } else if ("league.png".equals(league.getLeagueFlag()) && !"/images/league/league-default.png".equals(flag)) {
            leagueService.updateFlag(league, flag);
        }
    }

    private void updateSports(Sports sports) {
        if (sports.getId() == null || sports.getId() == 0) {
            sportsService.addSports(sports.getSportsName());
        }
    }

    private String getParsingUrl() {
        switch (Config.getSysConfig().getSports().getBet365()) {
            case "all":
                return Config.getSysConfig().getSports().getBet365Api() + "/api/v2/list" + (ut == null ? "" : "?ut=" + WebUtils.encoding(ut));
            case "cross":
                return Config.getSysConfig().getSports().getBet365Api() + "/api/v2/cross/list" + (ut == null ? "" : "?ut=" + WebUtils.encoding(ut));
            case "crossSoccer":
                return Config.getSysConfig().getSports().getBet365Api() + "/api/v2/crossSoccer/list" + (ut == null ? "" : "?ut=" + WebUtils.encoding(ut));
            case "special":
                return Config.getSysConfig().getSports().getBet365Api() + "/api/v2/special/list" + (ut == null ? "" : "?ut=" + WebUtils.encoding(ut));
            default:
                throw new IllegalArgumentException("SysConfig > Sports > bet365Api 의 정보가 잘못되었습니다. (" + Config.getSysConfig().getSports().getBet365() + ")");
        }
    }

    private boolean autoParsing() {
        return Config.getGameConfig().isAutoParsing();
    }

    private boolean autoUpdate() {
        return Config.getGameConfig().isAutoUpdate();
    }
}
