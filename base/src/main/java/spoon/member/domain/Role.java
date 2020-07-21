package spoon.member.domain;

public enum Role {

    NONE(0, "에러"),
    DUMMY(10, "NPC"),
    USER(20, "회원"),
    AGENCY1(50, "대리점"),
    AGENCY2(60, "총판"),
    ADMIN(110, "관리자"),
    SUPER(120, "슈퍼관리자"),
    GOD(9999, "시스템운영자");

    private int value;
    private String name;

    Role(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

}
