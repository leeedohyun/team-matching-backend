package server.teammatching.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import server.teammatching.entity.Member;
import server.teammatching.entity.Post;
import server.teammatching.entity.PostType;

@DataJpaTest
class PostRepositoryTest {

    private Member leader;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        final Member member = Member.builder()
                .loginId("hello")
                .password(new BCryptPasswordEncoder().encode("1234"))
                .email("ab@naver.com")
                .nickName("gi")
                .university("홍익대학교")
                .build();
        leader = memberRepository.save(member);
    }

    @Test
    void 프로젝트_저장() {
        // given
        final Post project = Post.createProject("제목", "분야", "기술스텍", "내용", 4,
                2, 1, 1, leader);

        // when
        final Post savedProject = postRepository.save(project);

        // then
        assertThat(savedProject).usingRecursiveComparison().isEqualTo(project);
    }

    @Test
    void 프로젝트_조회() {
        // given
        final Post project = Post.createProject("제목", "분야", "기술스텍", "내용", 4,
                2, 1, 1, leader);
        postRepository.save(project);

        // when
        final List<Post> projects = postRepository.findByType(PostType.PROJECT);

        // then
        assertThat(projects).hasSize(1)
                .contains(project);
    }

    @Test
    void 리더로_프로젝트_조회() {
        // given
        final Post project = Post.createProject("제목", "분야", "기술스텍", "내용", 4,
                2, 1, 1, leader);
        postRepository.save(project);

        // when
        final List<Post> projects = postRepository.findByLeaderAndType(leader, PostType.PROJECT);

        // then
        assertAll(
                () -> assertThat(projects).hasSize(1)
                        .contains(project),
                () -> assertThat(projects.get(0).getLeader()).isEqualTo(leader)
        );
    }

    @Test
    void 게시글_아이디로_프로젝트_조회() {
        // given
        final Post project = Post.createProject("제목", "분야", "기술스텍", "내용", 4,
                2, 1, 1, leader);
        postRepository.save(project);

        // when
        final Post findProject = postRepository.findByIdAndType(1L, PostType.PROJECT).get();

        // then
        assertThat(findProject).isEqualTo(project);
    }

    @Test
    void 리더의_아이디로_프로젝트_조회() {
        // given
        final Post project = Post.createProject("제목", "분야", "기술스텍", "내용", 4,
                2, 1, 1, leader);
        postRepository.save(project);

        // when
        final Post foundProject = postRepository.findByIdAndLeader_LoginId(1L, leader.getLoginId()).get();

        // then
        assertThat(foundProject).isEqualTo(project);
    }
}
