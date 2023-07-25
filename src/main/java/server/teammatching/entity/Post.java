package server.teammatching.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import server.teammatching.dto.request.TeamForm;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @ColumnDefault("0")
    private int views;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @ColumnDefault("0")
    private int likeNumber;

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

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
    @Setter
    private Recruitment recruitment;

    @Builder
    public Post(String title, int recruitNumber, String field, PostStatus status, PostType type,
                String content, Member leader) {
        this.title = title;
        this.recruitNumber = recruitNumber;
        this.field = field;
        this.status = status;
        this.type = type;
        this.content = content;
        this.leader = leader;
    }

    public static Post createTeam(TeamForm form, Member member) {
        return Post.builder()
                .title(form.getTitle())
                .field(form.getField())
                .recruitNumber(form.getRecruitNumber())
                .type(PostType.TEAM)
                .status(PostStatus.모집중)
                .leader(member)
                .content(form.getContent())
                .build();
    }
}
