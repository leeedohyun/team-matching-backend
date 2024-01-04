package server.teammatching.entity;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.ColumnDefault;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.teammatching.exception.InsufficientMembersException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseTimeEntity {

    private static final String INSUFFICIENT_MEMBER_EXCEPTION_MESSAGE = "총 인원수와 일치하지 않습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member leader;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alarm> alarms = new ArrayList<>();

    public static Post createTeam(final String title, final String content, final int recruitNumber,
                                  final Member leader) {
        final Post team = Post.builder()
                .title(title)
                .recruitNumber(recruitNumber)
                .type(PostType.TEAM)
                .status(PostStatus.모집중)
                .content(content)
                .build();
        team.setLeader(leader);
        return team;
    }

    public static Post createStudy(final String title, final String content, final int recruitNumber,
                                   final Member leader) {
        final Post study = Post.builder()
                .title(title)
                .recruitNumber(recruitNumber)
                .type(PostType.STUDY)
                .status(PostStatus.모집중)
                .content(content)
                .build();
        study.setLeader(leader);
        return study;
    }

    public static Post createProject(final String title, final String field, final String techStack,
                                     final String content, final int recruitNumber, final int designerNumber,
                                     final int frontendNumber, final int backendNumber, final Member leader) {
        validateRecruitMember(recruitNumber, designerNumber, frontendNumber, backendNumber);

        final Post project = Post.builder()
                .title(title)
                .field(field)
                .recruitNumber(recruitNumber)
                .techStack(techStack)
                .type(PostType.PROJECT)
                .status(PostStatus.모집중)
                .content(content)
                .backendNumber(backendNumber)
                .designerNumber(designerNumber)
                .frontendNumber(frontendNumber)
                .build();
        project.setLeader(leader);
        return project;
    }

    public void updateTitle(final String title) {
        if (title != null) {
            this.title = title;
        }
    }

    public void updateField(final String field) {
        if (field != null) {
            this.field = field;
        }
    }

    public void updateRecruitNumber(final int recruitNumber) {
        validateRecruitMember(recruitNumber, designerNumber, frontendNumber, backendNumber);
        this.recruitNumber = recruitNumber;
    }

    public void updateContent(final String content) {
        if (content != null) {
            this.content = content;
        }
    }

    public void updateDesignerNumber(final int designerNumber) {
        validateRecruitMember(recruitNumber, designerNumber, frontendNumber, backendNumber);
        this.designerNumber = designerNumber;
    }

    public void updateFrontendNumber(final int frontendNumber) {
        validateRecruitMember(recruitNumber, designerNumber, frontendNumber, backendNumber);
        this.frontendNumber = frontendNumber;
    }

    public void updateBackendNumber(final int backendNumber) {
        validateRecruitMember(recruitNumber, designerNumber, frontendNumber, backendNumber);
        this.backendNumber = backendNumber;
    }

    public void updatePostStatus(final PostStatus status) {
        this.status = status;
    }

    public void setRecruitment(Recruitment recruitment) {
        this.recruitment = recruitment;
        recruitment.setPost(this);
    }

    private void setLeader(final Member leader) {
        this.leader = leader;
        leader.getPostList().add(this);
    }

    private static void validateRecruitMember(final int recruitNumber, final int designerNumber,
                                              final int frontendNumber,
                                              final int backendNumber) {
        if (frontendNumber + backendNumber + designerNumber != recruitNumber) {
            throw new InsufficientMembersException(INSUFFICIENT_MEMBER_EXCEPTION_MESSAGE);
        }
    }
}
