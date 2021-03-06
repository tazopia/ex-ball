package spoon.config.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteConfig {

    // 회사명
    private String since = "2017";
    private String companyName = "Stella Solution";

    // 아이피 블럭 설정
    private boolean ipAdmin;
    private boolean ipUser;

    // 서버점검
    private boolean block;
    private String blockMessage;

    // 게시판 작성시 들어가는 태그
    private String nickname;

    private Join join = new Join();

    private Balance balance = new Balance();

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Balance {
        private boolean enabled;
        private String key = "LOV0R96tCIzxoIAbIbK4obYoKSLvB8w1LIY8oFOqjbCccKZRowtG00tJX0NQ";
        private String host = "http://api.pb1004.com/api/bettings";
        private long min = 0;
        private double rate = 100D;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Join {
        private boolean joinButton;
        private boolean joinCodePage;
        private boolean showRecommend;
        private boolean requiredRecommend;
        private boolean checkRecommend;
        private boolean duplicatePhone;
        private boolean duplicateAccount;
        private boolean joinAutoAdmin;
        private String joinAutoMessage;
        private int joinPoint;
        private int joinLevel;
        private int sameIp = 5;
    }

    private Point point = new Point();

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Point {
        private boolean block;

        // 충전 최소, 기본단위
        private int depositMin = 10000;
        private int depositUnit = 10000;

        // 환전 최소, 기본단위
        private int withdrawMin = 10000;
        private int withdrawUnit = 10000;
        // 충전신청이 없는 환전 가능 / 불가능
        private boolean canWithdraw = false;


        // 롤링 기준
        private int rollingSports = 100;
        private int rollingZone = 300;
        // true: 환전신청이 안 되게 막는다, false: 메시지만 뿌린다. (만약 메시지도 안 뿌릴려면 html에서 주석처리)
        private boolean rollingBlock;

        // 충전 이벤트
        private boolean[] event = new boolean[7];
        private int[] eventRate = new int[7];
        private boolean eventFirst;
        private boolean eventPayment;

        // 충전 보너스
        private int[] joinRate = new int[11];
        private int[] firstRate = new int[11];
        private int[] everyRate = new int[11];

        private int login;
        private int board;
        private int boardMax;
        private int comment;
        private int commentMax;
        private int exchange;
    }

    public String getPathAdmin() {
        return Config.getPathAdmin();
    }

    public String getPathSeller() {
        return Config.getPathSeller();
    }

    public String getPathJoin() {
        return Config.getPathJoin();
    }

    public String getPathSite() {
        return Config.getPathSite();
    }

    public String getVersion() {
        return Config.getVersion();
    }

}
