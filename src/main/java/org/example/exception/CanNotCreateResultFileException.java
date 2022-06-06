package org.example.exception;

public class CanNotCreateResultFileException extends Exception {
    public CanNotCreateResultFileException() {
    }

    public CanNotCreateResultFileException(String message) {
        super(message);
    }

    public CanNotCreateResultFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public CanNotCreateResultFileException(Throwable cause) {
        super(cause);
    }
}
