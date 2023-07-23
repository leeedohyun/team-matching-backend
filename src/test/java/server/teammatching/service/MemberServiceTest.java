package server.teammatching.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.request.MemberRequestDto;
import server.teammatching.dto.response.MemberResponseDto;
import server.teammatching.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;

    @Test
    @Rollback
    @DisplayName(value = "회원 가입 테스트")
    public void 회원가입() throws Exception {
        //given
        MemberRequestDto request = MemberRequestDto.builder()
                .loginId("loginId")
                .email("qqqq@nnn.com")
                .nickName("member1")
                .password("1234")
                .university("홍익대학교")
                .build();

        //when
        MemberResponseDto join = memberService.join(request);

        //then
        assertEquals(join.getMessage(), "회원가입이 성공했습니다.");
    }

    @Test
    @Rollback
    @DisplayName(value = "아이디 중복 테스트")
    public void 아이디_중복_테스트() throws Exception {
        //given
        MemberRequestDto request1 = MemberRequestDto.builder()
                .loginId("loginId")
                .email("qqqq@nnn.com")
                .nickName("member1")
                .password("1234")
                .university("홍익대학교")
                .build();

        MemberRequestDto request2 = MemberRequestDto.builder()
                .loginId("loginId")
                .email("qedfq113@ngdn.com")
                .nickName("member2")
                .password("1234")
                .university("홍익대학교")
                .build();

        //when
        memberService.join(request1);


        //then
        assertThatThrownBy(() -> memberService.join(request2))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName(value = "닉네임 중복 테스트")
    public void 닉네임_중복_테스트() throws Exception {
        //given
        MemberRequestDto request1 = MemberRequestDto.builder()
                .loginId("loginId11")
                .email("qqqq@nnn.com")
                .nickName("member1")
                .password("1234")
                .university("홍익대학교")
                .build();

        MemberRequestDto request2 = MemberRequestDto.builder()
                .loginId("loginId")
                .email("qedfq113@ngdn.com")
                .nickName("member1")
                .password("1234")
                .university("홍익대학교")
                .build();

        //when
        memberService.join(request1);


        //then
        assertThatThrownBy(() -> memberService.join(request2))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName(value = "모든 회원 조회하는 기능 테스트")
    public void 모든_회원_조회_테스트() {
        //given
        MemberRequestDto request1 = MemberRequestDto.builder()
                .loginId("loginId1")
                .email("qqqq@nnn.com")
                .nickName("member1")
                .password("1234")
                .university("홍익대학교")
                .build();

        MemberRequestDto request2 = MemberRequestDto.builder()
                .loginId("loginId2")
                .email("qbbq@nnn.com")
                .nickName("member2")
                .password("1234")
                .university("홍익대학교")
                .build();

        MemberRequestDto request3 = MemberRequestDto.builder()
                .loginId("loginId3")
                .email("aaaq@nnn.com")
                .nickName("member3")
                .password("1234")
                .university("홍익대학교")
                .build();


        //when
        memberService.join(request1);
        memberService.join(request2);
        memberService.join(request3);

        List<Member> memberList = memberService.findAll();

        //then
        assertThat(memberList.size()).isEqualTo(3);
    }
}
