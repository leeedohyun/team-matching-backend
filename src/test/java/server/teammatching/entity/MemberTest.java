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
        assertEmailUpdate("abc@gmail.com", null, "abc@gmail.com");
    }

    @Test
    @DisplayName("이메일이 빈 문자열이면 수정되지 않는다.")
    void notUpdateEmailByEmptyString() {
        assertEmailUpdate("abd@gmail.com", "", "abd@gmail.com");
    }

    @Test
    @DisplayName("이메일이 null이나 빈 문자열이 아니면 수정된다.")
    void updateEmail() {
        assertEmailUpdate("abd@gmail.com", "efg@naver.com", "efg@naver.com");
    }

    private void assertEmailUpdate(final String email, final String emailToUpdate, final String expected) {
        // given
        final MemberRequestDto dto = MemberRequestDto.builder()
                .password("1234")
                .email(email)
                .build();
        final Member member = Member.createMember(dto, passwordEncoder);

        // when
        member.updateEmail(emailToUpdate);

        // then
        assertThat(member.getEmail()).isEqualTo(expected);
    }
}
