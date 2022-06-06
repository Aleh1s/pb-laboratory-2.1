package org.example.exception;

public class InvalidTeamDataException extends Exception {
    public InvalidTeamDataException() {
    }
    public InvalidTeamDataException(String message) {
        super(message);
    }

    public InvalidTeamDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTeamDataException(Throwable cause) {
        super(cause);
    }
}
