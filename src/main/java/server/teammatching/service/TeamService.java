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
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public TeamAndStudyCreateResponseDto create(TeamAndStudyCreateRequestDto form , Long memberId) {
        Member leader = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 id 입니다."));

        Post team = Post.createTeam(form, leader);
        Post savedTeam = postRepository.save(team);
        return TeamAndStudyCreateResponseDto.builder()
                .title(savedTeam.getTitle())
                .content(savedTeam.getContent())
                .postId(savedTeam.getId())
                .memberId(savedTeam.getLeader().getId())
                .type(savedTeam.getType())
                .build();
    }

    public TeamAndStudyCreateResponseDto update(TeamAndStudyCreateRequestDto requestDto, Long postId) {
        Post findTeam = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 팀 id 입니다."));

        if (requestDto.getTitle() != null) {
            findTeam.updateTitle(requestDto.getTitle());
        }
        if (requestDto.getField() != null) {
            findTeam.updateField(requestDto.getField());
        }
        if (requestDto.getRecruitNumber() != 0) {
            findTeam.updateRecruitNumber(requestDto.getRecruitNumber());
        }
        if (requestDto.getContent() != null) {
            findTeam.updateContent(requestDto.getContent());
        }

        Post savedTeam = postRepository.save(findTeam);
        return TeamAndStudyCreateResponseDto.builder()
                .postId(savedTeam.getId())
                .memberId(savedTeam.getLeader().getId())
                .title(savedTeam.getTitle())
                .type(savedTeam.getType())
                .content(savedTeam.getContent())
                .build();
    }
}
