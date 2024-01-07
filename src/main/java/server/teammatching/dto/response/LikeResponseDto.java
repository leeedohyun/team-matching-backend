package server.teammatching.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.teammatching.entity.Like;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LikeResponseDto {

    private Long memberId;
    private Long postId;
    private String postTitle;

    public static LikeResponseDto from(final Like like) {
        return LikeResponseDto.builder()
                .memberId(like.getLikedMember().getId())
                .postId(like.getPost().getId())
                .postTitle(like.getPost().getTitle())
                .build();
    }
}
