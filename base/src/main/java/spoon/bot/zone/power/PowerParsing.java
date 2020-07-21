package spoon.bot.zone.power;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import spoon.bot.zone.service.GameBotParsing;
import spoon.common.net.HttpParsing;
import spoon.common.utils.JsonUtils;
import spoon.config.domain.Config;
import spoon.gameZone.ZoneConfig;
import spoon.gameZone.power.Power;
import spoon.gameZone.power.service.PowerBotService;

import java.util.Calendar;
import java.util.Date;

@Slf4j
@AllArgsConstructor
@Service
public class PowerParsing implements GameBotParsing {

    private PowerBotService powerBotService;

    private static boolean isClosing = false;

    private static Date sdate = new Date();

    @Async
    @Override
    public void parsingGame() {
        isClosing = false;

        int count = 0;
        int times = ZoneConfig.getPower().getPowerMaker().getTimes();
        Calendar cal = Calendar.getInstance();
        cal.setTime(ZoneConfig.getPower().getPowerMaker().getGameDate(times));

        for (int i = 0; i < 6; i++) {
            times++;
            cal.add(Calendar.MINUTE, 5);

            if (cal.getTime().before(sdate)) continue;

            if (powerBotService.notExist(cal.getTime())) {
                Power power = new Power(times, cal.getTime());
                power.setOdds(ZoneConfig.getPower().getOdds());
                powerBotService.addGame(power);
                count++;
            }
        }
        log.info("파워볼 경기등록 : {}건", count);
    }

    @Async
    @Override
    public void closingGame() {
        if (isClosing) return;
        isClosing = true;

        int times = ZoneConfig.getPower().getPowerMaker().getTimes();
        Date gameDate = ZoneConfig.getPower().getPowerMaker().getGameDate(times);

        String json = HttpParsing.getJson(Config.getSysConfig().getZone().getPowerUrl());
        if (json == null) {
            isClosing = false;
            return;
        }

        Power result = JsonUtils.toModel(json, Power.class);
        if (result == null) {
            isClosing = false;
            return;
        }

        if (!gameDate.equals(result.getGameDate())) {
            isClosing = false;
            return;
        }

        isClosing = powerBotService.closingGame(result);

        log.debug("파워볼 경기 종료 : {}회차", result.getRound());
    }

    @Async
    @Override
    public void checkResult() {
        powerBotService.checkResult();
    }

    @Async
    @Override
    public void deleteGame() {
        powerBotService.deleteGame(3);
    }
}
