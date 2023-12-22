package server.teammatching.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import server.teammatching.entity.Member;

@DataJpaTest
class MemberRepositoryTest {


    private static Member member;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeAll
    static void setUp() {
        member = Member.builder()
                .loginId("hello")
                .password("1234")
                .nickName("lee")
                .university("홍익대학교")
                .email("abc@naver.com")
                .build();
    }

    @Test
    @DisplayName("아이디로 회원 조회")
    void findById() {
        // given
        final Member savedMember = memberRepository.save(member);

        // when
        final Member findMember = memberRepository.findById(savedMember.getId()).get();

        // then
        Assertions.assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    @DisplayName("로그인 아이디로 회원 조회")
    void findByLoginId() {
        // given
        final Member savedMember = memberRepository.save(member);

        // when
        final Member findMember = memberRepository.findByLoginId(savedMember.getLoginId()).get();

        // then
        Assertions.assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    @DisplayName("로그인 아이디가 존재하면 true를 반환한다.")
    void existsByLoginId() {
        // given
        final Member savedMember = memberRepository.save(member);

        // when
        final boolean existed = memberRepository.existsByLoginId(savedMember.getLoginId());

        // then
        Assertions.assertThat(existed).isTrue();
    }

    @DisplayName("로그인 아이디가 존재하지 않으면 false를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"hong", "kim", "park", "jang"})
    void notExistsByLoginId(final String loginId) {
        // given
        memberRepository.save(member);

        // when
        final boolean notExisted = memberRepository.existsByLoginId(loginId);

        // then
        Assertions.assertThat(notExisted).isFalse();
    }

    @Test
    @DisplayName("닉네임이 존재하면 true를 반환한다.")
    void existsByNickName() {
        // given
        final Member savedMember = memberRepository.save(member);

        // when
        final boolean existed = memberRepository.existsByNickName(savedMember.getNickName());

        // then
        Assertions.assertThat(existed).isTrue();
    }

    @DisplayName("닉네임이 존재하지 않으면 false를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"hong", "kim", "park", "jang"})
    void notExistsByNickName(final String nickName) {
        // given
        memberRepository.save(member);

        // when
        final boolean notExisted = memberRepository.existsByLoginId(nickName);

        // then
        Assertions.assertThat(notExisted).isFalse();
    }
}
