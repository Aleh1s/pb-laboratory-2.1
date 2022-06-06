package org.example.exception;

public class InvalidTeamCountException extends Exception {

    public InvalidTeamCountException() {
    }

    public InvalidTeamCountException(Throwable cause) {
        super(cause);
    }

    public InvalidTeamCountException(String message) {
        super(message);
    }

    public InvalidTeamCountException(String message, Throwable cause) {
        super(message, cause);
    }
}
