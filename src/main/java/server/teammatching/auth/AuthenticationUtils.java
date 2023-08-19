package server.teammatching.auth;

import server.teammatching.exception.UnauthorizedException;

public class AuthenticationUtils {

    public static void validateAuthentication(Object principal) {
        if (principal == null) {
            throw new UnauthorizedException("Invalid member authentication");
        }
    }

    public static void verifyLoggedInUser(String loginId, String authenticatedId) {
        if (!loginId.equals(authenticatedId)) {
            throw new UnauthorizedException("Failed to verify the identity of the logged-in user.");
        }
    }
}
