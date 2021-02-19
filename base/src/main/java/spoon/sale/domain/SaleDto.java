package spoon.sale.domain;

import lombok.Data;
import spoon.common.utils.DateUtils;

import java.util.Date;

public class SaleDto {

    @Data
    public static class Command {
        private String userid;
        private String agency1;
        private String agency2;
        private String sdate = DateUtils.format(DateUtils.beforeDays(7));
        private String edate = DateUtils.format(DateUtils.beforeDays(1));

        public Date getEnd() {
            return DateUtils.end(this.edate);
        }

        public Date getStart() {
            return DateUtils.start(this.sdate);
        }
    }

    @Data
    public static class Payment {
        private String userid;
        private Long saleId;
        private long amount;
    }

}
