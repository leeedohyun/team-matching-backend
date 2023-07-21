package server.teammatching.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.entity.Member;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Transactional
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원추가() {
        //given
        Member member = Member.builder()
                .loginId("abcd")
                .password("eke33")
                .email("dfd@dkck.com")
                .nickName("member")
                .university("홍익대학교")
                .build();

        //when
        Member save = memberRepository.save(member);

        //then
        assertThat(save.getId()).isNotNull();
        assertThat(save.getLoginId()).isEqualTo("abcd");
        assertThat(save.getPassword()).isEqualTo("eke33");
        assertThat(save.getEmail()).isEqualTo("dfd@dkck.com");
        assertThat(save.getNickName()).isEqualTo("member");
        assertThat(save.getUniversity()).isEqualTo("홍익대학교");
    }
}