package server.teammatching.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Builder
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

    public static Application applyProject(Member appliedMember, Post project) {
        Application application = Application.builder()
                .status(ApplicationStatus.대기중)
                .post(project)
                .build();
        application.setAppliedMember(appliedMember);
        return application;
    }

    private void setAppliedMember(Member appliedMember) {
        this.appliedMember = appliedMember;
    }
}
