package spoon.web.seller.accounting;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import spoon.common.utils.WebUtils;
import spoon.member.domain.CurrentUser;
import spoon.seller.domain.Seller;
import spoon.seller.domain.SellerDto;
import spoon.seller.service.ShareService;
import spoon.support.web.AjaxResult;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Controller("seller.shareController")
@RequestMapping("#{config.pathSeller}")
public class ShareController {

    private ShareService shareService;

    /**
     * 대리점 수수료율 관리
     */
    @RequestMapping(value = "accounting/share", method = RequestMethod.GET)
    public String shareRate(ModelMap map, SellerDto.Command command) {
        CurrentUser user = WebUtils.user();
        command.setAgency2(user.getAgency2());
        command.setAgency1(user.getAgency1());

        List<Seller> list = shareService.sellerShare(command);

        map.addAttribute("hq", list.get(0));
        map.addAttribute("list", list);
        map.addAttribute("command", command);

        return "/seller/accounting/share";
    }

    /**
     * 대리점 수수료율 수정
     */
    @ResponseBody
    @RequestMapping(value = "accounting/share", method = RequestMethod.POST)
    public AjaxResult shareRate(SellerDto.Update update) {
        return shareService.updatePartnerShare(update);
    }

}
