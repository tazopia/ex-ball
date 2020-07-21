package spoon.sale.domain;

import lombok.Data;

public class SaleDto {

    @Data
    public static class Command {
        private String agency1;
        private String agency2;
    }

    @Data
    public static class Payment {
        private String userid;
        private Long saleId;
        private long amount;
    }

}
