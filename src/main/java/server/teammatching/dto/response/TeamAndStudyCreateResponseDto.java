package server.teammatching.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.teammatching.entity.Post;
import server.teammatching.entity.PostType;

import java.time.LocalDateTime;

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
    private int recruitNumber;
    private LocalDateTime createdAt;

    public static TeamAndStudyCreateResponseDto from(Post teamAndStudy) {
        return TeamAndStudyCreateResponseDto.builder()
                .title(teamAndStudy.getTitle())
                .content(teamAndStudy.getContent())
                .postId(teamAndStudy.getId())
                .nickName(teamAndStudy.getLeader().getNickName())
                .type(teamAndStudy.getType())
                .recruitNumber(teamAndStudy.getRecruitNumber())
                .createdAt(teamAndStudy.getCreatedAt())
                .build();
    }
}
