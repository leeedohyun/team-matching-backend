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
public class RecruitmentResponse {

    private Long memberId;
    private Long postId;
    private Long applicationId;
    private String nickname;
    private String title;
    private ApplicationStatus applicationStatus;

    public static RecruitmentResponse from(Application application) {
        return RecruitmentResponse.builder()
                .memberId(application.getAppliedMember().getId())
                .postId(application.getPost().getId())
                .applicationId(application.getId())
                .nickname(application.getAppliedMember().getNickName())
                .title(application.getPost().getTitle())
                .applicationStatus(application.getStatus())
                .build();
    }
}
