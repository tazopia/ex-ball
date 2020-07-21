package spoon.web.seller.accounting;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import spoon.common.utils.WebUtils;
import spoon.member.domain.CurrentUser;
import spoon.member.domain.Role;
import spoon.member.service.MemberService;
import spoon.sale.domain.SaleDto;
import spoon.sale.service.SaleService;

@Slf4j
@AllArgsConstructor
@Controller("seller.accountingController")
@RequestMapping("#{config.pathSeller}")
public class AccountingController {

    private SaleService saleService;

    private MemberService memberService;

    /**
     * 현재 정산금 페이지
     */
    @RequestMapping(value = "/accounting/current", method = RequestMethod.GET)
    public String sale(ModelMap map, SaleDto.Command command) {
        CurrentUser user = WebUtils.user();
        command.setAgency2(user.getAgency2());
        command.setAgency1(user.getAgency1());

        map.addAttribute("list", saleService.currentSale(command));
        return "/seller/accounting/current";
    }

    /**
     * 총판 정산 리스트
     */
    @RequestMapping(value = "/accounting/list", method = RequestMethod.GET)
    public String list(ModelMap map, SaleDto.Command command, @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = {"regDate"}) Pageable pageable) {
        CurrentUser user = WebUtils.user();
        command.setAgency2(user.getAgency2());
        command.setAgency1(user.getAgency1());

        if (user.getRole() == Role.AGENCY2) {
            map.addAttribute("page", saleService.getPage(command, pageable));
            return "/seller/accounting/list";
        }

        map.addAttribute("page", saleService.getPageItem(command, pageable));
        return "/seller/accounting/item";
    }

}
