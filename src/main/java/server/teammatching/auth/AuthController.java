package server.teammatching.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.teammatching.dto.request.LoginRequest;

@RestController
@RequiredArgsConstructor
@Api(tags = {"로그인/로그아웃 API"})
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value = "로그인 API")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @ApiOperation(value = "로그아웃 API")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal PrincipalDetails principal) {
        if (principal != null) {
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok("로그아웃이 정상적으로 처리되었습니다.");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이미 로그아웃된 상태입니다.");
    }
}
