package server.teammatching.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException() {
        super("This post does not exist.");
    }
}
