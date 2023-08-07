package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.response.LikeResponseDto;
import server.teammatching.entity.Like;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;
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

    public LikeResponseDto generateLike(Long memberId, Long postId) {
        Member likedMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 id 입니다."));
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 id 입니다."));

        if (likeRepository.findByLikedMemberAndPost(likedMember, findPost).isPresent()) {
            throw new RuntimeException();
        }

        Like generatedLike = Like.getnerateLike(likedMember, findPost);

        Like savedLike = likeRepository.save(generatedLike);

        return LikeResponseDto.builder()
                .memberId(savedLike.getLikedMember().getId())
                .postId(savedLike.getPost().getId())
                .postTitle(savedLike.getPost().getTitle())
                .build();
    }
}
