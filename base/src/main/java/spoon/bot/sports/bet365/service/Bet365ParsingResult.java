package spoon.bot.sports.bet365.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import spoon.bot.sports.bet365.domain.BotBet365;
import spoon.bot.sports.service.ParsingResult;
import spoon.common.net.HttpParsing;
import spoon.common.utils.JsonUtils;
import spoon.common.utils.WebUtils;
import spoon.config.domain.Config;
import spoon.game.entity.Game;
import spoon.game.service.GameBotService;
import spoon.monitor.service.MonitorService;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class Bet365ParsingResult implements ParsingResult {

    private GameBotService gameBotService;

    private MonitorService monitorService;

    private static String ut;

    private static int cnt = 0;

    private static boolean action = false;

    @Override
    @Async
    public void closingGame() {
        if (action) return;

        action = true;
        int closed = 0;

        cnt++;
        if (cnt > 10) {
            cnt = 0;
            ut = null;
        }

        String json = HttpParsing.getJson(getClosingUrl());
        if (json == null) {
            action = false;
            return;
        }

        List<BotBet365> list = JsonUtils.toBet365List(json);
        if (list == null) {
            action = false;
            return;
        }

        for (BotBet365 bot : list) {
            if (ut == null || ut.compareTo(bot.getUt()) < 0) ut = bot.getUt();
            if (bot.getScoreHome() == null || bot.getScoreAway() == null) continue;

            Game game = gameBotService.getGame(bot.getSiteCode(), bot.getSiteId());
            if (game == null || game.isClosing() || game.isCancel()) continue;
            boolean success = gameBotService.gameScore(game.getId(), bot.getScoreHome(), bot.getScoreAway(), bot.isCancel());
            if (success) {
                closed++;
            }
        }

        monitorService.checkSports();

        log.debug("Bet365 게임 클로징 - 전체 : {}, 클로징 : {}", list.size(), closed);
        action = false;
    }

    private String getClosingUrl() {
        switch (Config.getSysConfig().getSports().getBet365()) {
            case "all":
                return Config.getSysConfig().getSports().getBet365Api() + "/api/v2/result" + (ut == null ? "" : "?ut=" + WebUtils.encoding(ut));
            case "cross":
                return Config.getSysConfig().getSports().getBet365Api() + "/api/v2/cross/result" + (ut == null ? "" : "?ut=" + WebUtils.encoding(ut));
            case "crossSoccer":
                return Config.getSysConfig().getSports().getBet365Api() + "/api/v2/crossSoccer/result" + (ut == null ? "" : "?ut=" + WebUtils.encoding(ut));
            case "special":
                return Config.getSysConfig().getSports().getBet365Api() + "/api/v2/special/result" + (ut == null ? "" : "?ut=" + WebUtils.encoding(ut));
            default:
                throw new IllegalArgumentException("SysConfig > Sports > bet365Api 의 정보가 잘못되었습니다. (" + Config.getSysConfig().getSports().getBet365() + ")");
        }
    }
}
