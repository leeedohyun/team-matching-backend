package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;
import server.teammatching.dto.response.TeamAndStudyCreateResponseDto;
import server.teammatching.entity.*;
import server.teammatching.exception.MemberNotFoundException;
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

    public TeamAndStudyCreateResponseDto create(TeamAndStudyCreateRequestDto form , String memberId) {
        Member leader = memberRepository.findByLoginId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("유효하지 않은 사용자 id 입니다."));

        Post createdTeam = Post.createTeam(form, leader);

        Recruitment recruitment = new Recruitment();
        createdTeam.setRecruitment(recruitment);

        Post savedTeam = postRepository.save(createdTeam);
        recruitmentRepository.save(recruitment);

        return TeamAndStudyCreateResponseDto.builder()
                .title(savedTeam.getTitle())
                .content(savedTeam.getContent())
                .postId(savedTeam.getId())
                .nickName(savedTeam.getLeader().getNickName())
                .type(savedTeam.getType())
                .build();
    }

    public TeamAndStudyCreateResponseDto update(Long postId, TeamAndStudyCreateRequestDto requestDto, String memberId) {
        Post findTeam = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 팀 id 입니다."));

        if (!memberId.equals(findTeam.getLeader().getLoginId())) {
            throw new RuntimeException("Invalid");
        }
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
                .nickName(savedTeam.getLeader().getNickName())
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
                    .nickName(findTeam.getLeader().getNickName())
                    .title(findTeam.getTitle())
                    .type(findTeam.getType())
                    .content(findTeam.getContent())
                    .build();
            allTeams.add(team);
        }
        return allTeams;
    }

    @Transactional(readOnly = true)
    public List<TeamAndStudyCreateResponseDto> checkMemberTeams(String memberId, String authenticatedId) {
        if (!memberId.equals(authenticatedId)) {
            throw new RuntimeException("Invalid");
        }

        Member findLeader = memberRepository.findByLoginId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("유효하지 않은 사용자 id 입니다."));
        List<Post> findMemberTeams = postRepository.findByLeaderAndType(findLeader, PostType.TEAM);
        List<TeamAndStudyCreateResponseDto> allMemberTeams = new ArrayList<>();

        for (Post findMemberTeam : findMemberTeams) {
            TeamAndStudyCreateResponseDto team = TeamAndStudyCreateResponseDto.builder()
                    .postId(findMemberTeam.getId())
                    .nickName(findMemberTeam.getLeader().getNickName())
                    .title(findMemberTeam.getTitle())
                    .type(findMemberTeam.getType())
                    .content(findMemberTeam.getContent())
                    .build();
            allMemberTeams.add(team);
        }
        return allMemberTeams;
    }

    public void delete(Long teamId, String memberId) {
        Post findTeam = postRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 스터디 id 입니다."));
        List<Application> teamApplications = applicationRepository.findByPost(findTeam);
        postRepository.deleteByIdAndLeader_LoginId(teamId,memberId);
        applicationRepository.deleteAll(teamApplications);
    }
}
