package org.example.exception;

public class CanNotOpenFileException extends Exception {
    public CanNotOpenFileException() {
    }

    public CanNotOpenFileException(String message) {
        super(message);
    }

    public CanNotOpenFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public CanNotOpenFileException(Throwable cause) {
        super(cause);
    }
}
