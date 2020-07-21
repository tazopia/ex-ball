package spoon.support.web;

import org.springframework.web.util.HtmlUtils;
import spoon.bet.entity.BetItem;
import spoon.common.utils.DateUtils;
import spoon.common.utils.WebUtils;
import spoon.config.domain.Config;
import spoon.member.domain.Role;

import java.math.BigDecimal;
import java.util.Date;

public final class CustomTag {

    // String tag
    public static String onlyBr(String value) {
        if (value == null) return "";
        value = HtmlUtils.htmlEscape(value);
        return value.replaceAll(System.getProperty("line.separator"), "<br/>");
    }

    public static String colorBr(String value) {
        if (value == null) return "";
        return color(value)
                .replaceAll(System.getProperty("line.separator"), "<br/>");
    }

    public static String color(String value) {
        if (value == null) return "";
        return value
                .replaceAll("\\{\\{\\{", "<em class=\"color03\">").replaceAll("}}}", "</em>")
                .replaceAll("\\{\\{", "<em class=\"color02\">").replaceAll("}}", "</em>")
                .replaceAll("\\{", "<em class=\"color01\">").replaceAll("}", "</em>");
    }

    public static String country(String ip) {
        return WebUtils.country(ip);
    }

    // Flag
    public static String sportsFlag(BetItem item) {
        switch (item.getMenuCode()) {
            case MATCH:
            case HANDICAP:
            case CROSS:
            case SPECIAL:
            case LIVE:
            case SPORTS:
                return "/images/sports/" + Config.getSportsMap().get(item.getSports().toLowerCase()).getSportsFlag();
            default:
                return "/images/zone/flag-" + item.getMenuCode().toString().toLowerCase() + ".png";
        }
    }

    public static String leagueFlag(BetItem item) {
        switch (item.getMenuCode()) {
            case MATCH:
            case HANDICAP:
            case CROSS:
            case SPECIAL:
            case LIVE:
            case SPORTS:
                return "/images/league/" + Config.getLeagueMap().get((item.getSports() + "-" + item.getLeague()).toLowerCase()).getLeagueFlag();
            default:
                return "/images/zone/flag-" + item.getMenuCode().toString().toLowerCase() + ".png";
        }
    }

    // Date tag
    public static String dayWeekTime(Date date) {
        if (date == null) return "-";
        return DateUtils.format(date, "MM/dd(E) ") + "<em class=\"color02\">" + DateUtils.format(date, "HH:mm") + "</em>";
    }

    public static String dayWeekTimes(Date date) {
        if (date == null) return "-";
        return DateUtils.format(date, "MM/dd(E) ") + "<em class=\"color02\">" + DateUtils.format(date, "HH:mm:ss") + "</em>";
    }

    public static String dayWeek(Date date) {
        if (date == null) return "-";
        return DateUtils.format(date, "MM/dd(E)");
    }

    public static String dateWeek(Date date) {
        if (date == null) return "-";
        return DateUtils.format(date, "yyyy.MM.dd ") + "<em class=\"color02\">" + DateUtils.format(date, "(E)") + "</em>";
    }

    public static String titleDate(Date date) {
        if (date == null) return "-";
        return DateUtils.format(date, "yyyy.MM.dd(E) HH:mm:ss");
    }

    public static String fullDate(Date date) {
        if (date == null) return "-";
        return "<em class=\"color04\">" + DateUtils.format(date, "yyyy.MM.dd") + "</em><em class=\"color01\">" + DateUtils.format(date, " (E) ") + "</em>" + DateUtils.format(date, "HH:mm:ss");
    }

    public static String betDate(Date date) {
        if (date == null) return "-";
        return DateUtils.format(date, "dd(E) ") + "<em class=\"color02\">" + DateUtils.format(date, "HH:mm:ss") + "</em>";
    }

    // Number Tag
    public static String num(Long num) {
        if (num == null) return "0";
        return String.format("%,d", num);
    }

    public static int numVal(BigDecimal num) {
        return num == null ? 0 : num.intValue();
    }

    public static String handicap(double num) {
        return String.format("%,.1f", num);
    }

    public static String odds(double num) {
        return String.format("%,.2f", num);
    }

    public static String odds(double odds, double handicap) {
        if (odds == 0D && handicap == 0D) {
            return "VS";
        } else if (odds > 0) {
            return odds(odds);
        } else {
            return handicap(handicap);
        }
    }

    public static String round(int round, int size) {
        return String.format("%0" + size + "d", round);
    }

    // role
    public static boolean isAdmin(Role role) {
        return role.getValue() >= Role.ADMIN.getValue();
    }

    // recommRate
    public static double recommOddsSports(int level) {
        return Config.getGameConfig().getNoHitSportsRecommOdds()[level];
    }

    public static double recommOddsZone(int level) {
        return Config.getGameConfig().getNoHitZoneRecommOdds()[level];
    }

}
