package server.teammatching.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long id;

    @Lob
    private String resume;

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
    public Application(ApplicationStatus status, Member appliedMember, Post post, String resume) {
        this.status = status;
        this.appliedMember = appliedMember;
        this.post = post;
        this.resume = resume;
    }

    public void setRecruitment(Recruitment recruitment) {
        this.recruitment = recruitment;
        recruitment.getApplicationList().add(this);
    }

    public static Application apply(Member appliedMember, Post post, Recruitment recruitment, String resume) {
        Application application = Application.builder()
                .status(ApplicationStatus.대기중)
                .appliedMember(appliedMember)
                .post(post)
                .resume(resume)
                .build();
        application.setRecruitment(recruitment);
        return application;
    }

    public void updateStatus(ApplicationStatus status) {
        this.status = status;
    }
}
