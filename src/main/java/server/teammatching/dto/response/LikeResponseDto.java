package server.teammatching.dto.response;

import lombok.*;
import server.teammatching.entity.Like;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LikeResponseDto {

    private Long memberId;
    private Long postId;
    private String postTitle;

    public static LikeResponseDto from(Like like) {
        return LikeResponseDto.builder()
                .memberId(like.getLikedMember().getId())
                .postId(like.getPost().getId())
                .postTitle(like.getPost().getTitle())
                .build();
    }
}
