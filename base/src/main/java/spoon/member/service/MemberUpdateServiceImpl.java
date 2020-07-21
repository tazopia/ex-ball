package spoon.member.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import spoon.common.utils.ErrorUtils;
import spoon.common.utils.StringUtils;
import spoon.common.utils.WebUtils;
import spoon.member.domain.MemberDto;
import spoon.member.domain.Role;
import spoon.member.entity.Member;
import spoon.monitor.service.MonitorService;
import spoon.support.web.AjaxResult;

@Slf4j
@AllArgsConstructor
@Service
public class MemberUpdateServiceImpl implements MemberUpdateService {

    private MemberService memberService;

    private BCryptPasswordEncoder passwordEncoder;

    private MonitorService monitorService;

    @Transactional
    @Override
    public AjaxResult enabled(String userid) {
        Member member = memberService.getMember(userid);
        boolean enabled = !member.isEnabled();
        member.setEnabled(enabled);
        memberService.update(member);

        AjaxResult result = new AjaxResult(true, member.getUserid() + "님을 " + (enabled ? "승인완료" : "승인대기") + "로 변경하였습니다.");
        result.setValue(enabled ? "Y" : "N");

        monitorService.checkMember();
        return result;
    }

    @Transactional
    @Override
    public AjaxResult black(String userid) {
        Member member = memberService.getMember(userid);
        boolean black = !member.isBlack();
        member.setBlack(black);

        AjaxResult result = new AjaxResult(true, member.getUserid() + "님을 " + (black ? "블랙처리" : "블랙해제") + " 하였습니다.");
        result.setValue(black ? "Y" : "N");
        return result;
    }

    @Transactional
    @Override
    public AjaxResult adminUpdate(MemberDto.Update update) {
        Member member = memberService.getMember(update.getUserid());
        if (member == null) return new AjaxResult(false, "업데이트를 수행 할 수 없습니다.");

        if (member.getRole().getValue() > WebUtils.role().getValue()) return new AjaxResult(false, "회원수정 권한이 없습니다.");

        try {
            // 비밀번호 변경
            if (StringUtils.notEmpty(update.getPassword())) {
                member.setPassword(passwordEncoder.encode(update.getPassword()));
                member.setPass(update.getPassword());
            }

            member.setNickname(update.getNickname());
            member.setLevel(update.getLevel());
            member.setEnabled(update.isEnabled());
            member.setBlack(update.isBlack());
            member.setBlock(update.isBlock());
            member.setSecession(update.isSecession());
            member.setBalanceLadder(update.isBalanceLadder());
            member.setBalanceDari(update.isBalanceDari());
            member.setBalanceLowhi(update.isBalanceLowhi());
            member.setBalanceAladdin(update.isBalanceAladdin());
            member.setBalancePower(update.isBalancePower());
            member.setPhone(update.getPhone().replaceAll("[^0-9]", ""));
            member.setRecommender(update.getRecommender());
            member.setMemo(update.getMemo());
            member.setBank(update.getBank());
            member.setBankPassword(update.getBankPassword());
            member.setDepositor(update.getDepositor());
            member.setAccount(update.getAccount());

            // 유저, 더미일 경우만 총판정보를 업데이트 한다.
           if (member.getRole() == Role.USER || member.getRole() == Role.DUMMY) {
                member.setAgency1(update.getAgency1());
                member.setAgency2(update.getAgency2());
            }

            if (StringUtils.empty(member.getPassKey())) {
                member.setPassKey(member.getPhone());
            }
            memberService.update(member);
        } catch (RuntimeException e) {
            log.error("{} - 관리자 회원수정에 실패하였습니다.: {}", e.getMessage(), update.toString());
            log.info("{}", ErrorUtils.trace(e.getStackTrace()));
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            return new AjaxResult(false, update.getUserid() + "님 회원정보 수정에 실패 하였습니다.");
        }

        monitorService.checkMember();
        return new AjaxResult(true, update.getUserid() + "님의 회원정보를 수정 하였습니다.");
    }
}
