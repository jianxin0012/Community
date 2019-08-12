package community.community.exception;

public enum CustomExceptionEnum implements ICustomExceptionEnum {
    QUESTION_NOT_FOUND("找的问题不在了");

    private String message;

    CustomExceptionEnum(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
