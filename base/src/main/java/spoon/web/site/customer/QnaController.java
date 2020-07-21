package spoon.web.site.customer;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spoon.common.utils.WebUtils;
import spoon.config.domain.Config;
import spoon.customer.domain.QnaDto;
import spoon.customer.service.QnaService;
import spoon.support.web.AjaxResult;

@AllArgsConstructor
@Controller
@RequestMapping("#{config.pathSite}")
public class QnaController {

    private QnaService qnaService;

    @RequestMapping(value = "customer/qna", method = RequestMethod.GET)
    public String list(ModelMap map,
                       @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = "regDate") Pageable pageable) {
        map.addAttribute("page", qnaService.page(WebUtils.userid(), pageable));
        return "site/customer/qna/list";
    }

    @RequestMapping(value = "customer/qna/add", method = RequestMethod.GET)
    public String add(ModelMap map) {
        map.addAttribute("add", new QnaDto.Add());
        return "site/customer/qna/add";
    }

    @RequestMapping(value = "customer/qna/add", method = RequestMethod.POST)
    public String add(QnaDto.Add add, RedirectAttributes ra) {
        boolean success = qnaService.add(add);
        if (!success) {
            ra.addFlashAttribute("message", "상담 등록에 실패하였습니다.\n\n잠시 후 다시 이용하여 주세요.");
        }
        return "redirect:" + Config.getPathSite() + "/customer/qna";
    }

    @ResponseBody
    @RequestMapping(value = "customer/qna/view", method = RequestMethod.POST)
    public AjaxResult view(Long id) {
        return qnaService.view(id);
    }

    @ResponseBody
    @RequestMapping(value = "customer/qna/delete", method = RequestMethod.POST)
    public AjaxResult hidden(Long id) {
        return qnaService.hidden(id);
    }

}
