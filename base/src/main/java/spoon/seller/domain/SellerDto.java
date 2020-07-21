package spoon.seller.domain;

import lombok.Data;

public class SellerDto {

    @Data
    public static class Command {
        private String agency1;
        private String agency2;
    }

    @Data
    public static class Update {
        private String userid;
        private String rateCode;
        private double rateShare;
        private double rateSports;
        private double rateZone;
    }
}
