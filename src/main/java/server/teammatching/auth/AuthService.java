package server.teammatching.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.request.LoginRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;

    public String login(LoginRequest request) throws Exception {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        return principal.getUsername();
    }
}
