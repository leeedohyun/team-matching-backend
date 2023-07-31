package server.teammatching.dto.response;

import lombok.*;
import server.teammatching.entity.ApplicationStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RecruitmentResponse {

    private Long memberId;
    private Long postId;
    private String title;
    private ApplicationStatus applicationStatus;
}