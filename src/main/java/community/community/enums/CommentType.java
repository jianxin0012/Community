package community.community.enums;

public enum CommentType {
    QUESTION(1),
    COMMENT(2);

    private Integer type;

    CommentType(Integer i) {
        this.type = i;
    }

    public static boolean contain(Integer type) {
        for (CommentType commentType : CommentType.values()) {
            if (commentType.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }
}
