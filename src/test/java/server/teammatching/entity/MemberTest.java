package server.teammatching.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import server.teammatching.dto.request.MemberRequestDto;

class MemberTest {

    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    }

    @Test
    @DisplayName("이메일이 null이면 수정되지 않는다.")
    void notUpdateEmailByNull() {
        // given
        final MemberRequestDto dto = MemberRequestDto.builder()
                .password("1234")
                .email("abd@gmail.com")
                .build();
        final Member member = Member.createMember(dto, passwordEncoder);

        // when
        member.updateEmail(null);

        // then
        assertThat(member.getEmail()).isEqualTo("abd@gmail.com");
    }

    @Test
    @DisplayName("이메일이 빈 문자열이면 수정되지 않는다.")
    void notUpdateEmailByEmptyString() {
        // given
        final MemberRequestDto dto = MemberRequestDto.builder()
                .password("1234")
                .email("abd@gmail.com")
                .build();
        final Member member = Member.createMember(dto, passwordEncoder);

        // when
        member.updateEmail("");

        // then
        assertThat(member.getEmail()).isEqualTo("abd@gmail.com");
    }

    @Test
    @DisplayName("이메일이 null이나 빈 문자열이 아니면 수정된다.")
    void updateEmail() {
        // given
        final MemberRequestDto dto = MemberRequestDto.builder()
                .password("1234")
                .email("abd@gmail.com")
                .build();
        final Member member = Member.createMember(dto, passwordEncoder);

        // when
        member.updateEmail("efg@naver.com");

        // then
        assertThat(member.getEmail()).isEqualTo("efg@naver.com");
    }
}
