package spoon.bot.balance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class WorldResult {

    private boolean result;

    private String balance;

    private String message;

    private long point;

}
