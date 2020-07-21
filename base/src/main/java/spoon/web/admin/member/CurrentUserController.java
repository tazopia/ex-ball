package spoon.web.admin.member;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import spoon.common.utils.WebUtils;
import spoon.support.security.LoginUser;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller("admin.currentUserController")
@RequestMapping("#{config.pathAdmin}")
public class CurrentUserController {

    private SessionRegistry sessionRegistry;

    /**
     * 현재 접속자
     */
    @RequestMapping(value = "member/currentUser", method = RequestMethod.GET)
    public String currentUser(ModelMap map) {

        List<LoginUser> userList = new ArrayList<>();

        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof LoginUser) {
                LoginUser user = (LoginUser) principal;

                try {
                    user.getSession().getLastAccessedTime();
                } catch (RuntimeException e) {
                    continue;
                }

                if (user.getUser().getRole().getValue() <= WebUtils.role().getValue()) {
                    userList.add(user);
                }
            }
        }
        map.addAttribute("userList", userList);
        return "admin/member/popup/currentUser";
    }

}
