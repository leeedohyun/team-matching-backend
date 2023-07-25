package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.request.TeamForm;
import server.teammatching.dto.response.TeamResponse;
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

    public TeamResponse create(TeamForm form , Long memberId) {
        Member leader = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 id 입니다."));

        Post team = Post.createTeam(form, leader);
        Post savedTeam = postRepository.save(team);
        return TeamResponse.builder()
                .title(savedTeam.getTitle())
                .content(savedTeam.getContent())
                .postId(savedTeam.getId())
                .memberId(leader.getId())
                .type(savedTeam.getType())
                .build();
    }
}
