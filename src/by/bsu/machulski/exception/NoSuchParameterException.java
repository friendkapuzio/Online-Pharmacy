package by.bsu.machulski.exception;

public class NoSuchParameterException extends Exception {
    public NoSuchParameterException() {
    }

    public NoSuchParameterException(String message) {
        super(message);
    }

    public NoSuchParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchParameterException(Throwable cause) {
        super(cause);
    }
}
