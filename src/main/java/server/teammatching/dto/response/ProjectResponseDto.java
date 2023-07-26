package server.teammatching.dto.response;

import lombok.*;
import server.teammatching.entity.PostType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProjectResponseDto {

    private Long postId;
    private Long memberId;
    private String title;
    private String content;
    private PostType type;
    private int designerNumber;
    private int frontendNumber;
    private int backendNumber;
}
