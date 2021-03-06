package spoon.config.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SysConfig {

    private Sports sports = new Sports();

    private Zone zone = new Zone();

    private Event event = new Event();

    private String apiKey;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public class Sports {
        private String bet365Api = "http://api65.stelladb.com";

        // none, all, cross, special
        private String bet365 = "none";

        private String bestApi = "http://api43.stelladb.com";

        // none, all, cross, special
        private String best = "none";

        private String sportsApi = "http://api.top2500.com";

        // none, all, cross, special
        private String sports = "none";

        private String ferrariApi = "http://api31.stelladb.com";

        private String ferrari = "none";

        // 핸디캡, 오버언더 스프레드 최대 갯수
        private int spread = 1;

        // 라이브 메뉴 보여줄지 유무
        private boolean canLive;

        // 스프레드 여러개 사용 허용
        private boolean enableSpread = false;

        // 양방 허용
        private boolean enableSure = false;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public class Zone {

        // 게임 및 게임존 파싱 여부 (데모페이지 만들때 필요)
        private boolean enabled = true;

        // 발란스 사용여부
        private boolean balance;
        private String balanceType = "";

        // 폴리곤 발란스
        private String balanceUrl = "http://poly.upndown-88.com:8084";

        // 게이트 발란스
        private String balanceGateDariUrl = "HTTPS://API-GATE.COM/DO_BET_NSDD_V3.ASP";
        private String balanceGateLadderUrl = "HTTPS://API-GATE.COM/DO_BET_NSDR_V3.ASP";
        private String balanceGateSuttaUrl = "HTTPS://API-GATE.COM/DO_BET_NSGS_V3.ASP";
        private String balanceGatePowerUrl = "HTTPS://API-GATE.COM/DO_BET_NSPB_V3.ASP";

        public boolean getCanBalanceAladdin() {
            return "polygon".equals(this.balanceType);
        }

        public boolean getCanBalanceLowhi() {
            return "polygon".equals(this.balanceType);
        }

        // 사다리
        private boolean ladder;
        private String ladderUrl = "http://api22.stelladb.com/ladder";
        private String ladderPlayer = "http://ladder.named.com/main.php";

        private boolean dari;
        private String dariUrl = "http://api22.stelladb.com/dari";
        private String dariPlayer = "http://daridari.named.com/";

        private boolean snail;
        private String snailUrl = "http://api22.stelladb.com/snail";
        private String snailPlayer = "http://named.com/games/racing/view.php";

        // 신달팽이
        private boolean newSnail;
        private String newSnailUrl = "http://api22.stelladb.com/new_snail";
        private String newSnailPlayer = "http://ntry.com/scores/named_new_racing/live.php";

        private boolean power;
        private String powerUrl = "http://api22.stelladb.com/power";
        private String powerPlayer = "http://ntry.com/scores/powerball/live.php";
        private long powerBase = 1483492380000L;
        private int powerRound = 642047;
        private int powerDay = 28;

        private boolean powerLadder;
        private String powerLadderUrl = "http://api22.stelladb.com/power_ladder";
        private String powerLadderPlayer = "http://ntry.com/scores/power_ladder/live.php";

        // 키노사다리
        private boolean kenoLadder;
        private String kenoLadderUrl = "http://api22.stelladb.com/keno_ladder";
        private String kenoLadderPlayer = "http://ntry.com/scores/keno_ladder/live.php";

        private boolean aladdin;
        private String aladdinUrl = "http://api23.stelladb.com/aladdin";
        private String aladdinPlayer = "http://www.hafline.com/Theme/Ladder/Game.aspx";

        private boolean lowhi;
        private String lowhiUrl = "http://api23.stelladb.com/lowhi";
        private String lowhiPlayer = "http://www.hafline.com/Theme/LowHI/Game.aspx";

        private boolean oddeven;
        private boolean oddevenBasic = true;
        private String oddevenUrl = "http://api21.stelladb.com/oddeven";
        private String oddevenPlayer = "http://mgm.stelladb.com/live102.html";

        private boolean baccarat;
        private boolean baccaratBasic = true;
        private String baccaratUrl = "http://api21.stelladb.com/baccarat";
        private String baccaratPlayer = "http://mgm.stelladb.com/baccarat102.html";

        private boolean soccer;
        private String soccerUrl = "http://api21.stelladb.com/soccer2";
        private String soccerPlayer = "";

        private boolean dog;
        private String dogUrl = "http://api21.stelladb.com/dog";
        private String dogPlayer = "";

        private boolean luck;
        private String luckUrl = "http://api23.stelladb.com/luck";
        private String luckPlayer = "http://7luck-card.com/macau/";

        // crown 게임
        private boolean crownOddeven;
        private String crownOddevenUrl = "http://api26.stelladb.com/oddeven";
        private String crownOddevenPlayer = "http://crown-api.com/iframeoe/pc";

        private boolean crownBaccarat;
        private String crownBaccaratUrl = "http://api26.stelladb.com/baccarat";
        private String crownBaccaratPlayer = "http://crown-api.com/iframemb/skin1/pc";

        private boolean crownSutda;
        private String crownSutdaUrl = "http://api26.stelladb.com/sutda";
        private String crownSutdaPlayer = "http://crown-api.com/iframesutda/skin1/pc";

        // 게이트
        private boolean gate;

        public String getGnb() {
            if (this.ladder) {
                return "ladder";
            } else if (this.dari) {
                return "dari";
            } else if (this.snail) {
                return "snail";
            } else if (this.newSnail) {
                return "new_snail";
            } else if (this.power) {
                return "power";
            } else if (this.powerLadder) {
                return "power_ladder";
            } else if (this.kenoLadder) {
                return "keno_ladder";
            } else if (this.aladdin) {
                return "aladdin";
            } else if (this.lowhi) {
                return "lowhi";
            } else if (this.oddeven) {
                return "oddeven";
            } else if (this.baccarat) {
                return "baccarat";
            } else if (this.soccer) {
                return "soccer";
            } else if (this.dog) {
                return "dog";
            } else if (this.luck) {
                return "luck";
            } else {
                return "";
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public class Event {
        private boolean daily = true;
        private boolean lotto;
        @JsonIgnore
        public boolean isLottoEnabled() {
            return this.lotto && Config.getEventLottoConfig().isEnabled();
        }
    }
}
