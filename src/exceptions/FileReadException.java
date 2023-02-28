package exceptions;

import java.io.IOException;

public class FileReadException extends RuntimeException {

    public FileReadException(final String message) {
        super(message);
    }
}
