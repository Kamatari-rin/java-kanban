package exceptions;

public class FileCreateException extends RuntimeException {

    public FileCreateException(final String message) {
        super(message);
    }
}
