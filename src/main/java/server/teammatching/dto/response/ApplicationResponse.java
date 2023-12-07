package server.teammatching.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.teammatching.entity.Application;
import server.teammatching.entity.ApplicationStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ApplicationResponse {

    private Long postId;
    private String nickname;
    private String title;
    private ApplicationStatus applicationStatus;
    private String resume;

    public static ApplicationResponse from(Application application) {
        return ApplicationResponse.builder()
                .postId(application.getPost().getId())
                .nickname(application.getAppliedMember().getNickName())
                .title(application.getPost().getTitle())
                .applicationStatus(application.getStatus())
                .resume(application.getResume())
                .build();
    }
}
