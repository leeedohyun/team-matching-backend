package server.teammatching.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String resourceName, String value) {
        super(resourceName + " " + value + "is already in use.");
    }
}
