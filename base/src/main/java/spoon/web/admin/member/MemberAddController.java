package spoon.web.admin.member;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import spoon.common.utils.JsonUtils;
import spoon.config.domain.Config;
import spoon.member.domain.MemberDto;
import spoon.member.service.MemberAddService;
import spoon.member.service.MemberService;
import spoon.support.web.AjaxResult;

@Slf4j
@AllArgsConstructor
@Controller("admin.memberAddController")
@RequestMapping("#{config.pathAdmin}")
public class MemberAddController {

    private MemberService memberService;

    private MemberAddService memberAddService;

    @RequestMapping(value = "member/add", method = RequestMethod.GET)
    public String add(ModelMap map) {
        map.addAttribute("member", new MemberDto.Add());
        map.addAttribute("agencies", JsonUtils.toString(memberService.getAgencyList()));
        map.addAttribute("banks", Config.getBanks());
        map.addAttribute("pathJoin", Config.getPathJoin());
        return "admin/member/popup/add";
    }

    @ResponseBody
    @RequestMapping(value = "member/add", method = RequestMethod.POST)
    public AjaxResult add(MemberDto.Add add) {
        return memberAddService.adminAdd(add);
    }


    @RequestMapping(value = "member/dummy", method = RequestMethod.GET)
    public String dummy(ModelMap map) {
        map.addAttribute("dummy", new MemberDto.Dummy());
        return "admin/member/popup/dummy";
    }

    @ResponseBody
    @RequestMapping(value = "member/dummy", method = RequestMethod.POST)
    public AjaxResult dummy(MemberDto.Dummy dummy) {
        return memberAddService.adminAddDummy(dummy);
    }

}
