package server.teammatching.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Alarm(Member member, Post post) {
        this.post = post;
        setMember(member);
    }

    public void setMember(Member member) {
        this.member = member;
        member.getAlarms().add(this);
    }

    public static Alarm createAlarm(Member member, Post post) {
        return Alarm.builder()
                .member(member)
                .post(post)
                .build();
    }
}
