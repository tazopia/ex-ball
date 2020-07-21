package spoon.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import spoon.common.utils.WebUtils;
import spoon.config.domain.Config;
import spoon.member.domain.Role;
import spoon.member.service.LoginHistoryService;
import spoon.payment.service.EventPaymentService;

@Slf4j
@AllArgsConstructor
@Controller
public class LoginServiceController {

    private LoginHistoryService loginHistoryService;

    private EventPaymentService pointPaymentService;

    @RequestMapping("_login_service_")
    public String loginService() {

        // 로그인 히스토리 기록하기
        loginHistoryService.addHistory();

        // 로그인 포인트 지급하기
        pointPaymentService.loginPoint();

        return "redirect:" + defaultTargetUrl(WebUtils.role());
    }

    private String defaultTargetUrl(Role role) {
        switch (role) {
            case GOD:
            case SUPER:
            case ADMIN:
                return Config.getPathAdmin() + "/game/match/complete";
            case AGENCY1:
            case AGENCY2:
                return Config.getPathSeller() + "/sale/daily";
            default:
                return Config.getPathSite() + "/main";
        }
    }
}
