package server.teammatching.dto.response;

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
    private Long memberId;
    private String title;
    private String techStack;
    private String content;
    private PostType type;
    private int designerNumber;
    private int frontendNumber;
    private int backendNumber;

    public static ProjectResponseDto from(Post project) {
        return ProjectResponseDto.builder()
                .postId(project.getId())
                .memberId(project.getLeader().getId())
                .title(project.getTitle())
                .content(project.getContent())
                .techStack(project.getTechStack())
                .type(project.getType())
                .designerNumber(project.getDesignerNumber())
                .frontendNumber(project.getFrontendNumber())
                .backendNumber(project.getBackendNumber())
                .build();
    }
}
