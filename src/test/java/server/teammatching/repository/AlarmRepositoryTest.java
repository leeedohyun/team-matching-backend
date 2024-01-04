package server.teammatching.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import server.teammatching.entity.Alarm;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;

@DataJpaTest
class AlarmRepositoryTest {

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    private Member member;
    private Post post;

    @BeforeEach
    void setUp() {
        member = initMember();
        post = initPost();
    }

    @Test
    void 회원의_알람_조회() {
        // given
        final Alarm alarm = Alarm.createAlarm(member, post);
        alarmRepository.save(alarm);

        // when
        final List<Alarm> alarms = alarmRepository.findByMember(member);

        // then
        assertThat(alarms).hasSize(1);
    }

    private Member initMember() {
        final Member member = Member.builder()
                .email("email@email.cpom")
                .loginId("loginId")
                .nickName("nickName")
                .password("password")
                .university("홍익대학교")
                .build();
        return memberRepository.save(member);
    }

    private Post initPost() {
        final Post post = Post.createProject("제목", "분야", "기술스택", "내용", 4,
                2, 1, 1, member);

        return postRepository.save(post);
    }
}
