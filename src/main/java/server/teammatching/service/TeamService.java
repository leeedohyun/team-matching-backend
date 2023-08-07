package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;
import server.teammatching.dto.response.TeamAndStudyCreateResponseDto;
import server.teammatching.entity.*;
import server.teammatching.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final RecruitmentRepository recruitmentRepository;

    public TeamAndStudyCreateResponseDto create(TeamAndStudyCreateRequestDto form , Long memberId) {
        Member leader = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 id 입니다."));

        Post createdTeam = Post.createTeam(form, leader);

        Recruitment recruitment = new Recruitment();
        createdTeam.setRecruitment(recruitment);

        Post savedTeam = postRepository.save(createdTeam);
        recruitmentRepository.save(recruitment);

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

    @Transactional(readOnly = true)
    public List<TeamAndStudyCreateResponseDto> checkAllTeams() {
        List<Post> findTeams = postRepository.findByType(PostType.TEAM);
        List<TeamAndStudyCreateResponseDto> allTeams = new ArrayList<>();

        for (Post findTeam : findTeams) {
            TeamAndStudyCreateResponseDto team = TeamAndStudyCreateResponseDto.builder()
                    .postId(findTeam.getId())
                    .memberId(findTeam.getLeader().getId())
                    .title(findTeam.getTitle())
                    .type(findTeam.getType())
                    .content(findTeam.getContent())
                    .build();
            allTeams.add(team);
        }
        return allTeams;
    }

    @Transactional(readOnly = true)
    public List<TeamAndStudyCreateResponseDto> checkMemberTeams(Long memberId) {
        Member findLeader = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 id 입니다."));
        List<Post> findMemberTeams = postRepository.findByLeaderAndType(findLeader, PostType.TEAM);
        List<TeamAndStudyCreateResponseDto> allMemberTeams = new ArrayList<>();

        for (Post findMemberTeam : findMemberTeams) {
            TeamAndStudyCreateResponseDto team = TeamAndStudyCreateResponseDto.builder()
                    .postId(findMemberTeam.getId())
                    .memberId(findMemberTeam.getLeader().getId())
                    .title(findMemberTeam.getTitle())
                    .type(findMemberTeam.getType())
                    .content(findMemberTeam.getContent())
                    .build();
            allMemberTeams.add(team);
        }
        return allMemberTeams;
    }

    public void delete(Long teamId) {
        Post findTeam = postRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 스터디 id 입니다."));
        List<Application> teamApplications = applicationRepository.findByPost(findTeam);
        postRepository.delete(findTeam);
        applicationRepository.deleteAll(teamApplications);
    }
}
