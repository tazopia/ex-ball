package spoon.seller.domain;

import lombok.Data;
import spoon.member.domain.Role;

@Data
public class Seller {

    private String userid;

    private String nickname;

    private Role role;

    private String agency2;

    private String agency1;

    private String rateCode;

    private double rateShare;

    private double rateSports;

    private double rateZone;

    private long inMoney;

    private long outMoney;

    private long regMember;

    private long joinMember;

    private long betSports;

    private long betZone;

}
