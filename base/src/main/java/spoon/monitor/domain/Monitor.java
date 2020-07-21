package spoon.monitor.domain;

import lombok.Data;
import spoon.gameZone.ZoneConfig;

@Data
public class Monitor {

    private long money;

    private long point;

    private long in;

    private long out;

    // 충전
    private long deposit;

    private long alarmDeposit;

    // 환전
    private long withdraw;

    private long alarmWithdraw;

    // 고객응대
    private long qna;

    private long alarmQna;

    // 블랙회원 베팅
    private long black;

    // 신규회원
    private long member;

    // 게시판
    private long board;

    // 결과처리
    private long cross;

    private long special;

    private long live;

    // 정산
    private long sports;
    private long sportsEnd;

    private long ladder;
    private long ladderEnd;

    private long dari;
    private long dariEnd;

    private long snail;
    private long snailEnd;

    private long newSnail;
    private long newSnailEnd;

    private long power;
    private long powerEnd;

    private long powerLadder;
    private long powerLadderEnd;

    private long kenoLadder;
    private long kenoLadderEnd;

    private long aladdin;
    private long aladdinEnd;

    private long lowhi;
    private long lowhiEnd;

    private long oddeven;
    private long oddevenEnd;

    private long baccarat;
    private long baccaratEnd;

    private long soccer;
    private long soccerEnd;

    private long dog;
    private long dogEnd;

    private long luck;
    private long luckEnd;

    private long crownOddeven;
    private long crownOddevenEnd;

    private long crownBaccarat;
    private long crownBaccaratEnd;

    private long crownSutda;
    private long crownSutdaEnd;

    // 게임존
    public long getLadderResult() {
        return ZoneConfig.getLadder().getResult();
    }

    public long getDariResult() {
        return ZoneConfig.getDari().getResult();
    }

    public long getSnailResult() {
        return ZoneConfig.getSnail().getResult();
    }

    public long getNewSnailResult() {
        return ZoneConfig.getNewSnail().getResult();
    }

    public long getPowerResult() {
        return ZoneConfig.getPower().getResult();
    }

    public long getPowerLadderResult() {
        return ZoneConfig.getPowerLadder().getResult();
    }

    public long getKenoLadderResult() {
        return ZoneConfig.getKenoLadder().getResult();
    }

    public long getAladdinResult() {
        return ZoneConfig.getAladdin().getResult();
    }

    public long getLowhiResult() {
        return ZoneConfig.getLowhi().getResult();
    }

    public long getOddevenResult() {
        return ZoneConfig.getOddeven().getResult();
    }

    public long getBaccaratResult() {
        return ZoneConfig.getBaccarat().getResult();
    }

    public long getSoccerResult() {
        return ZoneConfig.getSoccer().getResult();
    }

    public long getDogResult() {
        return ZoneConfig.getDog().getResult();
    }

    public long getLuckResult() {
        return ZoneConfig.getLuck().getResult();
    }

    public long getCrownOddevenResult() {
        return ZoneConfig.getCrownOddeven().getResult();
    }

//    public long getCrownBaccaratResult() {
//        return ZoneConfig.getCrownBaccarat().getResult();
//    }

}
