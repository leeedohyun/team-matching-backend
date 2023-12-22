package server.teammatching.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
}
