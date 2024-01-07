package server.teammatching.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import server.teammatching.dto.response.LikeResponseDto;
import server.teammatching.entity.Like;
import server.teammatching.entity.LikeStatus;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;
import server.teammatching.exception.LikeNotFoundException;
import server.teammatching.exception.MemberNotFoundException;
import server.teammatching.exception.PostNotFoundException;
import server.teammatching.repository.LikeRepository;
import server.teammatching.repository.MemberRepository;
import server.teammatching.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private LikeService likeService;

    @Test
    void 관심_목록_등록() {
        // given
        final Member likedMember = 회원_생성(2L);
        final Post post = 게시글_생성(1L);
        final Like like = 관심_생성(likedMember, post);

        given(memberRepository.findByLoginId(any()))
                .willReturn(Optional.of(likedMember));
        given(postRepository.findById(any()))
                .willReturn(Optional.of(post));
        given(likeRepository.save(any()))
                .willReturn(like);

        // when
        final LikeResponseDto likeResponseDto = likeService.generateLike(likedMember.getLoginId(), post.getId());

        // then
        assertThat(likeResponseDto).usingRecursiveComparison()
                .isEqualTo(LikeResponseDto.from(like));
    }

    @Test
    void 관심_목록_생성_시_회원이_존재히지_않으면_예외_발생() {
        // given
        given(memberRepository.findByLoginId(any()))
                .willThrow(MemberNotFoundException.class);

        // when
        // then
        assertThatThrownBy(() -> likeService.generateLike("test", 1L))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 관심_목록_생성_시_post가_존재히지_않으면_예외_발생() {
        // given
        final Member likedMember = 회원_생성(2L);

        given(memberRepository.findByLoginId(any()))
                .willReturn(Optional.of(likedMember));
        given(postRepository.findById(any()))
                .willThrow(PostNotFoundException.class);

        // when
        // then
        assertThatThrownBy(() -> likeService.generateLike(likedMember.getLoginId(), 1L))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void 관심_목록_생성_시_이미_관심_목록에_등록되어_있으면_예외_발생() {
        // given
        final Member likedMember = 회원_생성(2L);
        final Post post = 게시글_생성(1L);

        given(memberRepository.findByLoginId(any()))
                .willReturn(Optional.of(likedMember));
        given(postRepository.findById(any()))
                .willReturn(Optional.of(post));
        given(likeRepository.findByLikedMemberAndPost(any(), any()))
                .willThrow(LikeNotFoundException.class);

        // when
        // then
        assertThatThrownBy(() -> likeService.generateLike(likedMember.getLoginId(), post.getId()))
                .isInstanceOf(LikeNotFoundException.class);
    }

    @Test
    void 관심_목록_조회() {
        // given
        final Member likedMember = 회원_생성(2L);
        final Post post1 = 게시글_생성(1L);
        final Post post2 = 게시글_생성(2L);
        final Like like1 = 관심_생성(likedMember, post1);
        final Like like2 = 관심_생성(likedMember, post2);

        given(memberRepository.findByLoginId(any()))
                .willReturn(Optional.of(likedMember));
        given(likeRepository.findByLikedMember(any()))
                .willReturn(List.of(like1, like2));

        // when
        final List<LikeResponseDto> likeResponseDtoList = likeService.checkLikes(likedMember.getLoginId());

        // then
        assertThat(likeResponseDtoList).usingRecursiveComparison()
                .isEqualTo(List.of(LikeResponseDto.from(like1), LikeResponseDto.from(like2)));
    }

    @Test
    void 관심_목록_조회_시_회원이_존재하지_않으면_예외_발생() {
        // given
        given(memberRepository.findByLoginId(any()))
                .willThrow(MemberNotFoundException.class);

        // when
        // then
        assertThatThrownBy(() -> likeService.checkLikes("test"))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 관심_목록_삭제() {
        // given
        final Member likedMember = 회원_생성(2L);
        final Post post = 게시글_생성(1L);
        final Like like = 관심_생성(likedMember, post);

        given(memberRepository.findByLoginId(any()))
                .willReturn(Optional.of(likedMember));
        given(postRepository.findById(any()))
                .willReturn(Optional.of(post));
        given(likeRepository.findByLikedMemberAndPost(any(), any()))
                .willReturn(Optional.of(like));
        doNothing().when(likeRepository).delete(any());

        // when
        final LikeResponseDto likeResponseDto = likeService.cancelLike(likedMember.getLoginId(), post.getId());

        // then
        assertThat(likeResponseDto).usingRecursiveComparison()
                .isEqualTo(LikeResponseDto.from(like));
    }

    @Test
    void 관심_목록_삭제_시_회원이_존재히지_않으면_예외_발생() {
        // given
        given(memberRepository.findByLoginId(any()))
                .willThrow(MemberNotFoundException.class);

        // when
        // then
        assertThatThrownBy(() -> likeService.generateLike("test", 1L))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 관심_목록_삭제_시_post가_존재히지_않으면_예외_발생() {
        // given
        final Member likedMember = 회원_생성(2L);

        given(memberRepository.findByLoginId(any()))
                .willReturn(Optional.of(likedMember));
        given(postRepository.findById(any()))
                .willThrow(PostNotFoundException.class);

        // when
        // then
        assertThatThrownBy(() -> likeService.generateLike(likedMember.getLoginId(), 1L))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void 관심_목록_삭제_시_이미_관심_목록에_등록되어_있으면_예외_발생() {
        // given
        final Member likedMember = 회원_생성(2L);
        final Post post = 게시글_생성(1L);

        given(memberRepository.findByLoginId(any()))
                .willReturn(Optional.of(likedMember));
        given(postRepository.findById(any()))
                .willReturn(Optional.of(post));
        given(likeRepository.findByLikedMemberAndPost(any(), any()))
                .willThrow(LikeNotFoundException.class);

        // when
        // then
        assertThatThrownBy(() -> likeService.generateLike(likedMember.getLoginId(), post.getId()))
                .isInstanceOf(LikeNotFoundException.class);
    }

    private Member 회원_생성(final Long id) {
        return Member.builder()
                .id(id)
                .loginId("test")
                .password("test")
                .email("my@email.com")
                .university("홍익대학교")
                .nickName("tom")
                .build();
    }

    private Post 게시글_생성(final Long id) {
        final Member leader = 회원_생성(1L);
        return Post.builder()
                .id(id)
                .leader(leader)
                .title("title")
                .content("content")
                .build();
    }

    private Like 관심_생성(final Member likedMember, final Post post) {
        return new Like(1L, LikeStatus.DEFAULT, likedMember, post);
    }
}
