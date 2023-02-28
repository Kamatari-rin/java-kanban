package exceptions;

import java.io.IOException;

public class FileWriteException extends RuntimeException {

    public FileWriteException(final String message) {
        super(message);
    }
}
