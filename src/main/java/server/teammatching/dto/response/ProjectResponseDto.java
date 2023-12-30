package server.teammatching.dto.response;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.teammatching.entity.Post;
import server.teammatching.entity.PostType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProjectResponseDto {

    private Long postId;
    private String nickName;
    private String title;
    private String techStack;
    private String field;
    private String content;
    private PostType type;
    private int recruitNumber;
    private int designerNumber;
    private int frontendNumber;
    private int backendNumber;
    private LocalDateTime createdAt;

    public static ProjectResponseDto from(Post project) {
        return ProjectResponseDto.builder()
                .postId(project.getId())
                .nickName(project.getLeader().getNickName())
                .title(project.getTitle())
                .content(project.getContent())
                .techStack(project.getTechStack())
                .type(project.getType())
                .field(project.getField())
                .recruitNumber(project.getRecruitNumber())
                .designerNumber(project.getDesignerNumber())
                .frontendNumber(project.getFrontendNumber())
                .backendNumber(project.getBackendNumber())
                .createdAt(project.getCreatedAt())
                .build();
    }
}
