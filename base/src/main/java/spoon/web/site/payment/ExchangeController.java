package spoon.web.site.payment;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spoon.banking.service.ExchangeService;
import spoon.support.web.AjaxResult;

@AllArgsConstructor
@RestController
@RequestMapping("#{config.pathSite}")
public class ExchangeController {

    private ExchangeService exchangeService;

    @RequestMapping(value = "/payment/exchange", method = RequestMethod.POST)
    public AjaxResult exchange() {
        return exchangeService.exchange();
    }
}
