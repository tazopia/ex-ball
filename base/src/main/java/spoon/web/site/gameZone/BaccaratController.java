package spoon.web.site.gameZone;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import spoon.common.utils.JsonUtils;
import spoon.gameZone.ZoneDto;
import spoon.gameZone.baccarat.BaccaratDto;
import spoon.gameZone.baccarat.service.BaccaratGameService;
import spoon.gameZone.baccarat.service.BaccaratService;
import spoon.support.web.AjaxResult;

@Slf4j
@AllArgsConstructor
@Controller
@RequestMapping(value = "#{config.pathSite}")
public class BaccaratController {

    private BaccaratService baccaratService;

    private BaccaratGameService baccaratGameService;

    @RequestMapping(value = "zone/baccarat", method = RequestMethod.GET)
    public String zone(ModelMap map) {
        map.addAttribute("config", JsonUtils.toString(baccaratService.gameConfig()));
        return "site/zone/baccarat";
    }

    @RequestMapping(value = "zone/baccarat/score", method = RequestMethod.GET)
    public String score(ModelMap map, @ModelAttribute("command") ZoneDto.Command command,
                        @PageableDefault(size = 20, direction = Sort.Direction.DESC, sort = "sdate") Pageable pageable) {
        map.addAttribute("page", baccaratService.getClosing(command, pageable));
        return "site/score/baccarat";
    }

    @ResponseBody
    @RequestMapping(value = "zone/baccarat/config", method = RequestMethod.POST)
    public BaccaratDto.Config config() {
        return baccaratService.gameConfig();
    }

    @ResponseBody
    @RequestMapping(value = "zone/baccarat/betting", method = RequestMethod.POST)
    public AjaxResult betting(@RequestHeader(value = "AJAX") boolean ajax, @RequestBody ZoneDto.Bet bet) {
        if (!ajax) {
            return new AjaxResult(false, "페이지를 찾을 수 없습니다.");
        }
        return baccaratGameService.betting(bet);
    }
}
