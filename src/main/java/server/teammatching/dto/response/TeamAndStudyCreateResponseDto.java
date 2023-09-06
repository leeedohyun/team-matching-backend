package server.teammatching.dto.response;

import lombok.*;
import server.teammatching.entity.Post;
import server.teammatching.entity.PostType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TeamAndStudyCreateResponseDto {

    private Long postId;
    private String nickName;
    private String title;
    private String content;
    private PostType type;

    public static TeamAndStudyCreateResponseDto from(Post teamAndStudy) {
        return TeamAndStudyCreateResponseDto.builder()
                .title(teamAndStudy.getTitle())
                .content(teamAndStudy.getContent())
                .postId(teamAndStudy.getId())
                .nickName(teamAndStudy.getLeader().getNickName())
                .type(teamAndStudy.getType())
                .build();
    }
}
