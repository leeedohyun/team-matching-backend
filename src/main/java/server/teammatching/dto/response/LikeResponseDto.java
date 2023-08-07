package server.teammatching.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LikeResponseDto {

    private Long memberId;
    private Long postId;
    private String postTitle;
}
