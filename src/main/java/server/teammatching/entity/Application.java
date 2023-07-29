package server.teammatching.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member appliedMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Application(ApplicationStatus status, Member appliedMember, Post post) {
        this.status = status;
        this.appliedMember = appliedMember;
        this.post = post;
    }

    public static Application applyProject(Member appliedMember, Post project) {
        return Application.builder()
                .status(ApplicationStatus.대기중)
                .appliedMember(appliedMember)
                .post(project)
                .build();
    }
}
