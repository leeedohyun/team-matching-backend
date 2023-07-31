package server.teammatching.dto.response;

import lombok.*;
import server.teammatching.entity.ApplicationStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ApplicationResponse {

    private Long id;
    private String title;
    private ApplicationStatus applicationStatus;
}
