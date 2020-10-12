package spoon.bot.balance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import spoon.bot.balance.service.wrold.WorldPowerService;
import spoon.config.domain.Config;

@Slf4j
@RequiredArgsConstructor
@Component
public class WorldTask {

    private final WorldPowerService worldPowerService;

    /**
     * 파워볼
     */
    @Scheduled(cron = "10 2/5 * * * ?")
    public void powerBalance() {
        if (canBalance()) {
            worldPowerService.balance();
        }
    }

    private boolean canBalance() {
        return Config.getSiteConfig().getBalance().isEnabled();
    }

}
