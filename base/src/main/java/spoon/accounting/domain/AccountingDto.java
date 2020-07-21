package spoon.accounting.domain;

import lombok.Data;
import spoon.common.utils.DateUtils;
import spoon.game.domain.MenuCode;

import java.util.Date;

public class AccountingDto {

    @Data
    public static class Command {
        private String sdate;
        private String edate;
        private String agency1;
        private String agency2;
        private String menuCode;

        public Command() {
            this.sdate = DateUtils.todayString();
            this.edate = this.sdate;
        }

        public String getStart() {
            return this.sdate.replaceAll("\\.", "-");
        }

        public String getEnd() {
            return this.edate.replaceAll("\\.", "-");
        }
    }

    @Data
    public static class Daily {
        private String dt;
        private long member;
        private long inMoney;
        private long outMoney;
        private long betUser;
        private long sportsUser;
        private long sportsBet;
        private long sportsHit;
        private long zoneUser;
        private long zoneBet;
        private long zoneHit;
        private long loginCnt;

        public Date getDdate() {
            return DateUtils.parse(this.dt, "yyyy-MM-dd");
        }

        public int getWeek() {
            return DateUtils.week(getDdate());
        }
    }

    @Data
    public static class Game {
        private String menuCode;
        private long betMoney;
        private long winMoney;
        private long loseMoney;
        private long ingMoney;

        public String getMenuName() {
            return MenuCode.valueOf(menuCode.toUpperCase()).getName();
        }
    }

    @Data
    public static class Amount {
        private String code;
        private long amount;
    }

}
