package community.community.exception;

public enum CustomExceptionEnum implements ICustomExceptionEnum {
    QUESTION_NOT_FOUND(2001,"找的问题不在了"),
    NO_LOGIN(2002,"未登录，请登录"),
    TARGET_NOT_FOUND(2003,"问题不存在了"),
    SERVER_ERROR(2004,"服务器内部错误"),
    TYPE_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"评论不存在了"),
    COMMENT_IS_EMPTY(2007,"评论不能为空"),
    CAN_NOT_EDIT_QUESTION(2008,"不能编辑别人的问题"),
    READ_NOTIFICATION_FAIL(2009,"不能读别人的通知"),
    NOTIFICATION_NOT_FOUND(2010,"找的通知不在了")
    ;

    private String message;
    private Integer code;


    CustomExceptionEnum(Integer code, String message) {
        this.code=code;
        this.message=message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
