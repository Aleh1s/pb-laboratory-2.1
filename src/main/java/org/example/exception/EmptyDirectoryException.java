package org.example.exception;

public class EmptyDirectoryException extends Exception {
    public EmptyDirectoryException() {
    }

    public EmptyDirectoryException(String message) {
        super(message);
    }

    public EmptyDirectoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyDirectoryException(Throwable cause) {
        super(cause);
    }
}
