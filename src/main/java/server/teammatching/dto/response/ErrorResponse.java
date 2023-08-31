package server.teammatching.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ErrorResponse {

    private HttpStatus status;
    private String message;
}
