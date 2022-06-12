package org.example.exception;

import java.io.IOException;

public class CanNotCreateLogFileException extends IOException {

    public CanNotCreateLogFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
