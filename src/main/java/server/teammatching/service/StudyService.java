package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;
import server.teammatching.dto.response.TeamAndStudyCreateResponseDto;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;
import server.teammatching.repository.MemberRepository;
import server.teammatching.repository.PostRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public TeamAndStudyCreateResponseDto create(TeamAndStudyCreateRequestDto requestDto, Long memberId) {
        Member leader = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 id 입니다."));

        Post createdStudy = Post.createStudy(requestDto, leader);
        Post savedStudy = postRepository.save(createdStudy);
        return TeamAndStudyCreateResponseDto.builder()
                .title(savedStudy.getTitle())
                .postId(savedStudy.getId())
                .memberId(leader.getId())
                .type(savedStudy.getType())
                .content(savedStudy.getContent())
                .build();
    }
}
