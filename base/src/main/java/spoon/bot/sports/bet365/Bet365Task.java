package spoon.bot.sports.bet365;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import spoon.bot.sports.service.ParsingGame;
import spoon.bot.sports.service.ParsingResult;
import spoon.config.domain.Config;

@Slf4j
@AllArgsConstructor
@Component
public class Bet365Task {

    private ParsingGame bet365ParsingGame;

    private ParsingResult bet365ParsingResult;

    @Scheduled(cron = "15/30 * * * * *")
    public void parsingGame() {
        if ("none".equals(Config.getSysConfig().getSports().getBet365())) return;
        bet365ParsingGame.parsingGame();
    }

    @Scheduled(cron = "25/30 * * * * *")
    public void parsingResult() {
        if ("none".equals(Config.getSysConfig().getSports().getBet365())) return;
        bet365ParsingResult.closingGame();
    }

}
