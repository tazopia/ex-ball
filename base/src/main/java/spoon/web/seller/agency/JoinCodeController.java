package spoon.web.seller.agency;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import spoon.common.utils.WebUtils;
import spoon.member.domain.CurrentUser;
import spoon.seller.domain.JoinCodeDto;
import spoon.seller.service.JoinCodeService;

@Slf4j
@AllArgsConstructor
@Controller("seller.joinCodeController")
@RequestMapping("#{config.pathSeller}")
public class JoinCodeController {

    private JoinCodeService joinCodeService;

    /**
     * 가입코드 페이지
     */
    @RequestMapping(value = "agency/joinCode", method = RequestMethod.GET)
    public String list(ModelMap map, JoinCodeDto.Partner command) {
        CurrentUser user = WebUtils.user();
        command.setAgency2(user.getAgency2());
        command.setAgency1(user.getAgency1());

        map.addAttribute("list", joinCodeService.getPartner(command));
        return "seller/agency/joinCode";
    }

}
