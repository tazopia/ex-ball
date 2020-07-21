package spoon.web.seller.sale;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import spoon.accounting.domain.AccountingDto;
import spoon.accounting.service.AccountingService;
import spoon.common.utils.WebUtils;
import spoon.member.domain.CurrentUser;
import spoon.member.service.MemberService;

@Slf4j
@AllArgsConstructor
@Controller("seller.saleController")
@RequestMapping("#{config.pathSeller}")
public class SaleController {

    private AccountingService accountingService;

    private MemberService memberService;

    @RequestMapping(value = "sale/daily", method = RequestMethod.GET)
    public String daily(ModelMap map, @ModelAttribute("command") AccountingDto.Command command) {
        CurrentUser user = WebUtils.user();
        command.setAgency2(user.getAgency2());
        command.setAgency1(user.getAgency1());

        map.addAttribute("list", accountingService.daily(command));
        return "seller/sale/daily";
    }

    @RequestMapping(value = "sale/detail", method = RequestMethod.GET)
    public String detail(ModelMap map, @ModelAttribute("command") AccountingDto.Command command) {
        CurrentUser user = WebUtils.user();
        command.setAgency2(user.getAgency2());
        command.setAgency1(user.getAgency1());

        map.addAttribute("list", accountingService.gameAccount(command));
        map.addAttribute("amount", accountingService.amount(command));
        map.addAttribute("money", accountingService.money(command));
        map.addAttribute("point", accountingService.point(command));
        map.addAttribute("member", memberService.getMember(WebUtils.userid()));

        return "seller/sale/detail";
    }
}
