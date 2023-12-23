package server.teammatching.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import server.teammatching.dto.request.MemberRequestDto;
import server.teammatching.dto.response.MemberResponseDto;
import server.teammatching.entity.Member;
import server.teammatching.exception.DuplicateResourceException;
import server.teammatching.exception.MemberNotFoundException;
import server.teammatching.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 시 로그인 아이디가 중복되는 경우 예외가 발생한다.")
    void notJoinDuplicateLoginId() {
        // given
        final MemberRequestDto requestDto = MemberRequestDto.builder()
                .loginId("qwer")
                .password("1234")
                .university("홍익대학교")
                .email("abc@hanmail.net")
                .nickName("hello")
                .build();

        given(memberRepository.existsByLoginId(any()))
                .willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> memberService.join(requestDto))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    @DisplayName("회원가입 시 닉네임이 중복되는 경우 예외가 발생한다.")
    void notJoinDuplicateNickName() {
        // given
        final MemberRequestDto requestDto = MemberRequestDto.builder()
                .loginId("qwer")
                .password("1234")
                .university("홍익대학교")
                .email("abc@hanmail.net")
                .nickName("hello")
                .build();

        given(memberRepository.existsByNickName(any()))
                .willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> memberService.join(requestDto))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    @DisplayName("회원가입을 진행한다.")
    void join() {
        // given
        final MemberRequestDto requestDto = MemberRequestDto.builder()
                .loginId("qwer")
                .password("1234")
                .university("홍익대학교")
                .email("abc@hanmail.net")
                .nickName("hello")
                .build();

        final Member member = Member.builder()
                .id(1L)
                .loginId("qwer")
                .password(passwordEncoder.encode("1234"))
                .university("홍익대학교")
                .email("abc@hanmail.net")
                .nickName("hello")
                .build();

        given(memberRepository.existsByNickName(any()))
                .willReturn(false);
        given(memberRepository.save(any()))
                .willReturn(member);

        // when
        final MemberResponseDto responseDto = memberService.join(requestDto);

        // then
        assertAll(
                () -> assertThat(responseDto.getMemberId()).isEqualTo(1L),
                () -> assertThat(responseDto.getNickName()).isEqualTo("hello"),
                () -> assertThat(responseDto.getEmail()).isEqualTo("abc@hanmail.net"),
                () -> assertThat(responseDto.getUniversity()).isEqualTo("홍익대학교"),
                () -> assertThat(responseDto.getLoginId()).isEqualTo("qwer")
        );
    }

    @Test
    @DisplayName("회원 탈퇴 시 존재하지 않는 아이디이면 예외가 발생한다.")
    void withdrawException() {
        // given
        final Member member = Member.builder()
                .id(1L)
                .loginId("qwer")
                .password(passwordEncoder.encode("1234"))
                .university("홍익대학교")
                .email("abc@hanmail.net")
                .nickName("hello")
                .build();

        given(memberRepository.findByLoginId(any()))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> memberService.delete(member.getLoginId()))
                .isInstanceOf(MemberNotFoundException.class);
    }
}
