package community.community.exception;

public class CustomException extends RuntimeException{
    private String message;
    private Integer code;

    public CustomException(ICustomExceptionEnum message) {
        this.code=message.getCode();
        this.message = message.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
