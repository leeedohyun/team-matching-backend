package server.teammatching.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import server.teammatching.dto.MemberRequest;
import server.teammatching.entity.Member;

@EnableJpaAuditing
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberServiceTest {

    @Autowired MemberService memberService;

    @Test
    @Rollback(value = false)
    public void a() {
        MemberRequest request = MemberRequest.builder()
                .loginId("loginId")
                .email("qqqq@nnn.com")
                .nickName("member1")
                .password("1234")
                .university("홍익대학교")
                .build();

        Member join = memberService.join(request);
        System.out.println(join);

    }
}