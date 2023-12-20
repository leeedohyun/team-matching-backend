package server.teammatching.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException() {
        super("This member does not exist.");
    }
}
