package server.teammatching.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
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
        this.member = member;
        this.post = post;
    }

    public static Alarm createAlarm(Member member, Post post) {
        return Alarm.builder()
                .member(member)
                .post(post)
                .build();
    }
}
