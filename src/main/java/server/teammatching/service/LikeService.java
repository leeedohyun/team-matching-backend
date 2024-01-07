package server.teammatching.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.teammatching.dto.response.LikeResponseDto;
import server.teammatching.entity.Like;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;
import server.teammatching.exception.LikeNotFoundException;
import server.teammatching.exception.MemberNotFoundException;
import server.teammatching.exception.PostNotFoundException;
import server.teammatching.repository.LikeRepository;
import server.teammatching.repository.MemberRepository;
import server.teammatching.repository.PostRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public LikeResponseDto generateLike(final String memberId, final Long postId) {
        final Member likedMember = memberRepository.findByLoginId(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final Post findPost = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        if (likeRepository.findByLikedMemberAndPost(likedMember, findPost).isPresent()) {
            throw new LikeNotFoundException("Not Found");
        }

        final Like generatedLike = Like.create(likedMember, findPost);

        final Like savedLike = likeRepository.save(generatedLike);

        return LikeResponseDto.from(savedLike);
    }

    @Transactional(readOnly = true)
    public List<LikeResponseDto> checkLikes(final String memberId) {
        final Member findMember = memberRepository.findByLoginId(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final List<Like> likeList = likeRepository.findByLikedMember(findMember);

        return likeList.stream()
                .map(LikeResponseDto::from)
                .collect(toList());
    }

    public LikeResponseDto cancelLike(final String  memberId, final Long postId) {
        final Member likedMember = memberRepository.findByLoginId(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final Post findPost = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        final Like canceledLike = likeRepository.findByLikedMemberAndPost(likedMember, findPost)
                .orElseThrow(() -> new LikeNotFoundException("유효하지 않은 id 입니다."));

        likeRepository.delete(canceledLike);

        return LikeResponseDto.from(canceledLike);
    }
}
