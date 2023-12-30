package server.teammatching.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class PostTest {

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
                Post.createProject("제목", "분야", "기술스텍", "내용", recruitNumber,
                        designer, frontend, backend, leader));
    }

    @Test
    void 프로젝트_생성() {
        assertDoesNotThrow(
                () -> Post.createProject("제목", "분야", "기술스텍", "내용", 4,
                        2, 1, 1, leader));
    }

    @Test
    void 게시글을_수정할_때_제목이_null이면_수정이_안_된다() {
        // given
        final Post post = Post.builder()
                .title("제목")
                .build();

        // when
        post.updateTitle(null);

        // then
        assertThat(post.getTitle()).isEqualTo("제목");
    }

    @Test
    void 게시글을_수정할_때_제목이_null이_아니면_수정된다() {
        // given
        final Post post = Post.builder()
                .title("제목")
                .build();
        final String updatedTitle = "변경된 제목";

        // when
        post.updateTitle(updatedTitle);

        // then
        assertThat(post.getTitle()).isEqualTo(updatedTitle);
    }

    @Test
    void 게시글을_수정할_때_모집_분야가_null이면_수정이_안_된다() {
        // given
        final Post post = Post.builder()
                .field("모집 분야")
                .build();

        // when
        post.updateField(null);

        // then
        assertThat(post.getField()).isEqualTo("모집 분야");
    }

    @Test
    void 게시글을_수정할_때_모집_분야가_null이_아니면_수정된다() {
        // given
        final Post post = Post.builder()
                .field("모집 분야")
                .build();
        final String updatedField = "변경된 모집 분야";

        // when
        post.updateField(updatedField);

        // then
        assertThat(post.getField()).isEqualTo(updatedField);
    }

    @Test
    void 게시글을_수정할_때_본문이_null이면_수정이_안_된다() {
        // given
        final Post post = Post.builder()
                .content("본문")
                .build();

        // when
        post.updateContent(null);

        // then
        assertThat(post.getContent()).isEqualTo("본문");
    }

    @Test
    void 게시글을_수정할_때_본문이_null이_아니면_수정된다() {
        // given
        final Post post = Post.builder()
                .content("본문")
                .build();
        final String updatedContent = "변경된 본문";

        // when
        post.updateContent(updatedContent);

        // then
        assertThat(post.getContent()).isEqualTo(updatedContent);
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
