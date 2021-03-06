package spoon.gameZone.crownBaccarat;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import spoon.common.utils.DateUtils;

import java.util.Date;

@Slf4j
public class CrownBaccaratDto {

    @Data
    public static class Config {
        private long money;
        private boolean enabled;
        private int round;
        private Date gameDate;
        private int betTime;
        private String sdate;
        private double[] odds;
        private int max;
        private int win;
        private int min;

        public String getGameDateName() {
            return DateUtils.format(gameDate, "MM/dd(E)");
        }

        public String getGameTimeName() {
            return DateUtils.format(gameDate, "HH:mm");
        }
    }

    @Data
    public static class Score {
        private long id;
        private int round;
        private Date gameDate;
        private boolean cancel;

        private String p;
        private String b;
        private String c;
        private String result;
    }
}
