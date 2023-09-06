package server.teammatching.dto.response;

import lombok.*;
import server.teammatching.entity.Application;
import server.teammatching.entity.ApplicationStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ApplicationResponse {

    private Long postId;
    private String title;
    private ApplicationStatus applicationStatus;

    public static ApplicationResponse from(Application application) {
        return ApplicationResponse.builder()
                .postId(application.getPost().getId())
                .title(application.getPost().getTitle())
                .applicationStatus(application.getStatus())
                .build();
    }
}
