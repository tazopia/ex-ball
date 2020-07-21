package spoon.customer.domain;

import lombok.Data;

public class QnaDto {

    @Data
    public static class Command {
        private String username = "";
        private boolean match;
        private String searchType = "";
        private String searchValue = "";
    }

    @Data
    public static class Add {
        private String title;
        private String contents;
    }

    @Data
    public static class Reply {
        private Long id;
        private String reTitle;
        private String reply;
    }

    @Data
    public static class Auto {
        private Long id;
        private String userid;
        private String nickname;
    }
}
