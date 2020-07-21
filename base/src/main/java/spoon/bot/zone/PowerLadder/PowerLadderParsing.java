package spoon.bot.zone.PowerLadder;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import spoon.bot.zone.service.GameBotParsing;
import spoon.common.net.HttpParsing;
import spoon.common.utils.JsonUtils;
import spoon.config.domain.Config;
import spoon.gameZone.ZoneConfig;
import spoon.gameZone.powerLadder.PowerLadder;
import spoon.gameZone.powerLadder.service.PowerLadderBotService;

import java.util.Calendar;
import java.util.Date;

@Slf4j
@AllArgsConstructor
@Service
public class PowerLadderParsing implements GameBotParsing {

    private PowerLadderBotService powerLadderBotService;

    private static boolean isClosing = false;

    private static Date sdate = new Date();

    @Async
    @Override
    public void parsingGame() {
        isClosing = false;

        int count = 0;
        int times = ZoneConfig.getPowerLadder().getPowerMaker().getTimes();
        Calendar cal = Calendar.getInstance();
        cal.setTime(ZoneConfig.getPowerLadder().getPowerMaker().getGameDate(times));

        for (int i = 0; i < 6; i++) {
            times++;
            cal.add(Calendar.MINUTE, 5);

            if (cal.getTime().before(sdate)) continue;

            if (powerLadderBotService.notExist(cal.getTime())) {
                PowerLadder powerLadder = new PowerLadder(times, cal.getTime());
                powerLadder.setOdds(ZoneConfig.getPowerLadder().getOdds());
                powerLadderBotService.addGame(powerLadder);
                count++;
            }
        }
        log.info("파워사다리 경기등록 : {}건", count);
    }

    @Async
    @Override
    public void closingGame() {
        if (isClosing) return;
        isClosing = true;

        int times = ZoneConfig.getPowerLadder().getPowerMaker().getTimes();
        Date gameDate = ZoneConfig.getPowerLadder().getPowerMaker().getGameDate(times);

        String json = HttpParsing.getJson(Config.getSysConfig().getZone().getPowerLadderUrl());
        if (json == null) {
            isClosing = false;
            return;
        }

        PowerLadder result = JsonUtils.toModel(json, PowerLadder.class);
        if (result == null) {
            isClosing = false;
            return;
        }

        if (!gameDate.equals(result.getGameDate())) {
            isClosing = false;
            return;
        }

        isClosing = powerLadderBotService.closingGame(result);

        log.debug("파워사다리 경기 종료 : {}회차", result.getRound());
    }

    @Async
    @Override
    public void checkResult() {
        powerLadderBotService.checkResult();
    }

    @Async
    @Override
    public void deleteGame() {
        powerLadderBotService.deleteGame(3);
    }
}
