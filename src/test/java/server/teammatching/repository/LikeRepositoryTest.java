package server.teammatching.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import server.teammatching.entity.Like;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class LikeRepositoryTest {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public LikeRepositoryTest(final LikeRepository likeRepository, final MemberRepository memberRepository,
                              final PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
    }

    @Test
    void 회원과_게시글로_관심_목록_찾기() {
        // given
        final Member likedMember = 회원_생성();
        final Post post = 게시글_생성();
        final Like like = 관심_생성(likedMember, post);

        // when
        final Like findlike = likeRepository.findByLikedMemberAndPost(likedMember, post).get();

        // then
        assertThat(findlike).isEqualTo(like);
    }

    @Test
    void 회원으로_관심_목록_조회() {
        // given
        final Member likedMember = 회원_생성();
        final Post post1 = 게시글_생성();
        final Post post2= 게시글_생성();
        final Like like1 = 관심_생성(likedMember, post1);
        final Like like2 = 관심_생성(likedMember, post2);


        // when
        final List<Like> likes = likeRepository.findByLikedMember(likedMember);

        // then
        assertThat(likes).hasSize(2)
                .contains(like1, like2);
    }

    private Member 회원_생성() {
        final Member member = Member.builder()
                .loginId("test")
                .password("test")
                .email("my@email.com")
                .university("홍익대학교")
                .nickName("tom")
                .build();
        return memberRepository.save(member);
    }

    private Post 게시글_생성() {
        final Member leader = 회원_생성();
        final Post post = Post.builder()
                .leader(leader)
                .title("title")
                .content("content")
                .build();
        return postRepository.save(post);
    }

    private Like 관심_생성(final Member likedMember, final Post post) {
        final Like like = Like.create(likedMember, post);
        return likeRepository.save(like);
    }
}
