package community.community.exception;

public class CustomException extends RuntimeException{
    private String message;

    public CustomException(ICustomExceptionEnum message) {
        this.message = message.getMessage();
    }

    public CustomException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
