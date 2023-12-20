package server.teammatching.exception;

public class ApplicationNotFoundException extends RuntimeException {

    public ApplicationNotFoundException() {
        super("This application does not exist.");
    }
}
