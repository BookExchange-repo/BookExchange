package ee.ttu.BookExchange.exceptions;

public class APIException extends RuntimeException {
    public APIException(String message) {
        super(message);
    }
}
