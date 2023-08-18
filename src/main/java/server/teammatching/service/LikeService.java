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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public LikeResponseDto generateLike(String memberId, Long postId) {
        Member likedMember = memberRepository.findByLoginId(memberId)
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

    @Transactional(readOnly = true)
    public List<LikeResponseDto> checkLikes(String memberId) {
        Member findMember = memberRepository.findByLoginId(memberId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 id 입니다."));

        List<Like> likeList = likeRepository.findByLikedMember(findMember);
        List<LikeResponseDto> responseList = new ArrayList<>();

        for (Like like : likeList) {
            LikeResponseDto response = LikeResponseDto.builder()
                    .postId(like.getPost().getId())
                    .memberId(like.getLikedMember().getId())
                    .postTitle(like.getPost().getTitle())
                    .build();
            responseList.add(response);
        }
        return responseList;
    }

    public LikeResponseDto cancelLike(String  memberId, Long postId) {
        Member likedMember = memberRepository.findByLoginId(memberId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 id 입니다."));
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 id 입니다."));
        Like canceledLike = likeRepository.findByLikedMemberAndPost(likedMember, findPost)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 id 입니다."));

        likeRepository.delete(canceledLike);
        return LikeResponseDto.builder()
                .postId(canceledLike.getPost().getId())
                .postTitle(canceledLike.getPost().getTitle())
                .memberId(canceledLike.getLikedMember().getId())
                .build();
    }
}
