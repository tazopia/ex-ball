package spoon.web.seller.agency;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import spoon.common.utils.WebUtils;
import spoon.config.domain.Config;
import spoon.member.domain.CurrentUser;
import spoon.member.domain.MemberDto;
import spoon.member.domain.Role;
import spoon.member.service.MemberAddService;
import spoon.member.service.MemberListService;
import spoon.member.service.MemberService;
import spoon.support.web.AjaxResult;

@AllArgsConstructor
@Controller("seller.sellerController")
@RequestMapping("#{config.pathSeller}")
public class MemberController {

    private MemberListService memberListService;

    private MemberAddService memberAddService;

    private MemberService memberService;

    @RequestMapping(value = "agency/member", method = RequestMethod.GET)
    public String list(ModelMap map, MemberDto.Seller command,
                       @PageableDefault(size = 50, sort = {"joinDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        CurrentUser user = WebUtils.user();
        command.setAgency2(user.getAgency2());

        if (user.getRole() == Role.AGENCY1) {
            command.setAgency1(user.getAgency1());
        } else {
            map.addAttribute("agencyList", memberService.getAgency1List(user.getUserid()));
        }

        map.addAttribute("command", command);
        map.addAttribute("page", memberListService.sellerList(command, pageable));

        return "seller/agency/member";
    }

    @RequestMapping(value = "agency/add", method = RequestMethod.GET)
    public String add(ModelMap map) {
        map.addAttribute("member", new MemberDto.Agency());
        map.addAttribute("banks", Config.getBanks());
        map.addAttribute("pathJoin", Config.getPathJoin());

        return "seller/agency/add";
    }

    @ResponseBody
    @RequestMapping(value = "member/add", method = RequestMethod.POST)
    public AjaxResult add(MemberDto.Agency add) {
        return memberAddService.sellerAdd(add);
    }

}
