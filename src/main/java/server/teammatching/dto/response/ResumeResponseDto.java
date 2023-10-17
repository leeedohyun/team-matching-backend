package server.teammatching.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.teammatching.entity.Application;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResumeResponseDto {

    private Long memberId;
    private Long postId;
    private String loginId;
    private String resume;

    public static ResumeResponseDto from(Application application) {
        return ResumeResponseDto.builder()
                .memberId(application.getAppliedMember().getId())
                .resume(application.getResume())
                .loginId(application.getAppliedMember().getLoginId())
                .postId(application.getPost().getId())
                .build();
    }
}
