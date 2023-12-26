package server.teammatching.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class ProjectTest {

    private final Member leader = createLeader();

    @Test
    void 각_분야별_인원_수가_전체_모집_인원_수와_다르면_예외_발생() {
        // given
        final int frontend = 1;
        final int backend = 2;
        final int designer = 1;
        final int recruitNumber = 6;

        // when
        // then
        assertThatThrownBy(() ->
                Post.createProject(1L, "제목", "분야", "기술스텍", "내용", recruitNumber,
                        designer, frontend, backend, leader));
    }

    @Test
    void 프로젝트_생성() {
        assertDoesNotThrow(
                () -> Post.createProject(1L, "제목", "분야", "기술스텍", "내용", 4,
                2, 1, 1, leader));
    }



    private Member createLeader() {
        return Member.builder()
                .id(1L)
                .loginId("hello")
                .password(new BCryptPasswordEncoder().encode("1234"))
                .email("ab@naver.com")
                .nickName("gi")
                .university("홍익대학교")
                .build();
    }
}
