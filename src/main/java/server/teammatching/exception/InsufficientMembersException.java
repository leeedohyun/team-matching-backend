package server.teammatching.exception;

public class InsufficientMembersException extends RuntimeException {

    public InsufficientMembersException(String message) {
        super(message);
    }
}
