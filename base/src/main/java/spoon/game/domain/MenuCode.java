package spoon.game.domain;

import spoon.config.domain.Config;

import java.util.ArrayList;
import java.util.List;

public enum MenuCode {

    MATCH(100, "승무패"),
    HANDICAP(200, "핸디캡"),
    CROSS(300, "크로스"),
    SPECIAL(600, "스페셜"),
    LIVE(700, "라이브"),
    SPORTS(999, "스포츠"),

    LADDER(10100, "사다리"),
    SNAIL(10200, "달팽이"),
    NEW_SNAIL(10250, "뉴달팽이"),
    DARI(10300, "다리다리"),

    POWER(11100, "파워볼"),
    POWER_LADDER(11200, "파워사다리"),
    KENO_LADDER(11300, "키노사다리"),

    ALADDIN(12100, "알라딘"),
    LOWHI(12200, "로하이"),

    SOCCER(20100, "가상축구"),
    DOG(20200, "개경주"),

    ODDEVEN(21100, "홀짝"),
    BACCARAT(21200, "바카라"),

    LUCK(22100, "세븐럭"),

    // CROWN
    CROWN_ODDEVEN(23100, "홀짝"),
    CROWN_BACCARAT(23200, "바카라"),
    CROWN_SUTDA(23300, "섰다"),

    NONE(99999, "삭제경기");

    private int value;

    private String name;

    MenuCode(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static boolean isSports(MenuCode menuCode) {
        return menuCode.getValue() <= MenuCode.SPORTS.getValue();
    }

    public static List<MenuCode> getZoneList() {
        List<MenuCode> list = new ArrayList<>();
        if (Config.getSysConfig().getZone().isCrownOddeven()) list.add(MenuCode.CROWN_ODDEVEN);
        if (Config.getSysConfig().getZone().isCrownBaccarat()) list.add(MenuCode.CROWN_BACCARAT);
        if (Config.getSysConfig().getZone().isCrownSutda()) list.add(MenuCode.CROWN_SUTDA);

        if (Config.getSysConfig().getZone().isLadder()) list.add(MenuCode.LADDER);
        if (Config.getSysConfig().getZone().isDari()) list.add(MenuCode.DARI);
        if (Config.getSysConfig().getZone().isSnail()) list.add(MenuCode.SNAIL);
        if (Config.getSysConfig().getZone().isNewSnail()) list.add(MenuCode.NEW_SNAIL);
        if (Config.getSysConfig().getZone().isPower()) list.add(MenuCode.POWER);
        if (Config.getSysConfig().getZone().isPowerLadder()) list.add(MenuCode.POWER_LADDER);
        if (Config.getSysConfig().getZone().isKenoLadder()) list.add(MenuCode.KENO_LADDER);
        if (Config.getSysConfig().getZone().isAladdin()) list.add(MenuCode.ALADDIN);
        if (Config.getSysConfig().getZone().isLowhi()) list.add(MenuCode.LOWHI);
        if (Config.getSysConfig().getZone().isOddeven()) list.add(MenuCode.ODDEVEN);
        if (Config.getSysConfig().getZone().isBaccarat()) list.add(MenuCode.BACCARAT);
        if (Config.getSysConfig().getZone().isSoccer()) list.add(MenuCode.SOCCER);
        if (Config.getSysConfig().getZone().isDog()) list.add(MenuCode.DOG);
        if (Config.getSysConfig().getZone().isLuck()) list.add(MenuCode.LUCK);

        return list;
    }

}
