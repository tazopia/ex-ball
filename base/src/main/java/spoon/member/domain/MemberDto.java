package spoon.member.domain;

import lombok.Data;
import spoon.config.domain.Config;

public class MemberDto {

    @Data
    public static class Command {
        private String mode = "";
        private String searchType = "";
        private String searchValue = "";
        private String username = "";
        private boolean match;
        private String sort = "";
        private String level = "";
    }

    @Data
    public static class Join {
        private String code;
        private String recommender;
        private String userid;
        private String nickname;
        private String password;
        private String phone;
        private String bank;
        private String depositor;
        private String account;
        private String bankPassword;

    }

    @Data
    public static class Add {
        private String userid;
        private String password;
        private String nickname;
        private Role role;
        private int level = Config.getSiteConfig().getJoin().getJoinLevel();
        private String phone = "";
        private String recommender = "";
        private long money;
        private long point;
        private String agency1 = "";
        private String agency2 = "";
        private String bank = "";
        private String depositor = "";
        private String account = "";
        private String bankPassword = "";
        private String memo = "";
    }

    @Data
    public static class Agency {
        private String userid;
        private String password;
        private String nickname;
        private String phone = "";
        private String bank = "";
        private String depositor = "";
        private String account = "";
        private String bankPassword = "";
    }

    @Data
    public static class Dummy {
        private int start = 1;
        private int end = 10;
        private String text;
    }

    @Data
    public static class Update {
        private String userid;
        private String password;
        private String nickname;
        private int level;
        private boolean enabled;
        private boolean black;
        private boolean block;
        private boolean secession;
        private boolean balanceLadder;
        private boolean balanceDari;
        private boolean balanceLowhi;
        private boolean balanceAladdin;
        private boolean balancePower;
        private String phone;
        private String recommender;
        private String agency1;
        private String agency2;
        private String memo;
        private String bank;
        private String depositor;
        private String account;
        private String bankPassword;
    }

    @Data
    public static class Seller {
        private String agency1;
        private String agency2;
        private String depositor;
        private String username;
        private boolean match;
    }

    @Data
    public static class Exchange {
        private String userid;
        private String nickname;
    }

}
