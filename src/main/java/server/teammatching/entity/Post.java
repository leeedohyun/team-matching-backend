package server.teammatching.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import server.teammatching.dto.request.ProjectRequestDto;
import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;
import server.teammatching.exception.InsufficientMembersException;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int recruitNumber;

    @Column(nullable = false)
    private String field;

    private String techStack;

    @ColumnDefault("0")
    private int views;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    private int designerNumber;

    private int frontendNumber;

    private int backendNumber;

    @Enumerated(EnumType.STRING)
    private PostType type;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member leader;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @Builder
    public Post(String title, int recruitNumber, String field, PostStatus status, PostType type,
                String content) {
        this.title = title;
        this.recruitNumber = recruitNumber;
        this.field = field;
        this.status = status;
        this.type = type;
        this.content = content;
    }

    public void setLeader(Member leader) {
        this.leader = leader;
        leader.getPostList().add(this);
    }

    public void setRecruitment(Recruitment recruitment) {
        this.recruitment = recruitment;
        recruitment.setPost(this);
    }

    public static Post createTeam(TeamAndStudyCreateRequestDto form, Member member) {
        Post team = Post.builder()
                .title(form.getTitle())
                .field(form.getField())
                .recruitNumber(form.getRecruitNumber())
                .type(PostType.TEAM)
                .status(PostStatus.모집중)
                .content(form.getContent())
                .build();
        team.setLeader(member);
        return team;
    }

    public static Post createStudy(TeamAndStudyCreateRequestDto form, Member member) {
        Post study = Post.builder()
                .title(form.getTitle())
                .field(form.getField())
                .recruitNumber(form.getRecruitNumber())
                .type(PostType.STUDY)
                .status(PostStatus.모집중)
                .content(form.getContent())
                .build();
        study.setLeader(member);
        return study;
    }

    public static Post createProject(ProjectRequestDto requestDto, Member member) {
        if (requestDto.getFrontendNumber() + requestDto.getBackendNumber() +
                requestDto.getDesignerNumber() != requestDto.getRecruitNumber()) {
            throw new InsufficientMembersException("총 인원수와 일치하지 않습니다.");
        }

        Post project = Post.builder()
                .title(requestDto.getTitle())
                .field(requestDto.getField())
                .recruitNumber(requestDto.getRecruitNumber())
                .techStack(requestDto.getTechStack())
                .type(PostType.PROJECT)
                .status(PostStatus.모집중)
                .content(requestDto.getContent())
                .backendNumber(requestDto.getBackendNumber())
                .designerNumber(requestDto.getDesignerNumber())
                .frontendNumber(requestDto.getFrontendNumber())
                .build();
        project.setLeader(member);
        return project;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateField(String field) {
        this.field = field;
    }

    public void updateRecruitNumber(int recruitNumber) {
        this.recruitNumber = recruitNumber;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateDesignerNumber(int designerNumber) {
        this.designerNumber = designerNumber;
    }

    public void updateFrontendNumber(int frontendNumber) {
        this.frontendNumber = frontendNumber;
    }

    public void updateBackendNumber(int backendNumber) {
        this.backendNumber = backendNumber;
    }

    public void updatePostStatus(PostStatus status) {
        this.status = status;
    }
}
