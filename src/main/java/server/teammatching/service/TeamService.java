package server.teammatching.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.teammatching.auth.AuthenticationUtils;
import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;
import server.teammatching.dto.response.TeamAndStudyCreateResponseDto;
import server.teammatching.entity.Application;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;
import server.teammatching.entity.PostType;
import server.teammatching.entity.Recruitment;
import server.teammatching.exception.MemberNotFoundException;
import server.teammatching.exception.PostNotFoundException;
import server.teammatching.repository.ApplicationRepository;
import server.teammatching.repository.MemberRepository;
import server.teammatching.repository.PostRepository;
import server.teammatching.repository.RecruitmentRepository;

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
                .orElseThrow(MemberNotFoundException::new);

        Post createdTeam = Post.createTeam(form, leader);

        Recruitment recruitment = new Recruitment();
        createdTeam.setRecruitment(recruitment);

        Post savedTeam = postRepository.save(createdTeam);
        recruitmentRepository.save(recruitment);

        return TeamAndStudyCreateResponseDto.from(savedTeam);
    }

    public TeamAndStudyCreateResponseDto update(Long postId, TeamAndStudyCreateRequestDto requestDto, String memberId) {
        Post findTeam = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        AuthenticationUtils.verifyLoggedInUser(memberId, findTeam.getLeader().getLoginId());

        if (requestDto.getTitle() != null) {
            findTeam.updateTitle(requestDto.getTitle());
        }
        if (requestDto.getRecruitNumber() != 0) {
            findTeam.updateRecruitNumber(requestDto.getRecruitNumber());
        }
        if (requestDto.getContent() != null) {
            findTeam.updateContent(requestDto.getContent());
        }

        Post savedTeam = postRepository.save(findTeam);
        return TeamAndStudyCreateResponseDto.from(savedTeam);
    }

    @Transactional(readOnly = true)
    public List<TeamAndStudyCreateResponseDto> checkAllTeams() {
        List<Post> findTeams = postRepository.findByType(PostType.TEAM);

        return findTeams.stream()
                .map(TeamAndStudyCreateResponseDto::from)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<TeamAndStudyCreateResponseDto> checkMemberTeams(String memberId, String authenticatedId) {
        AuthenticationUtils.verifyLoggedInUser(memberId, authenticatedId);
        Member findLeader = memberRepository.findByLoginId(memberId)
                .orElseThrow(MemberNotFoundException::new);
        List<Post> findMemberTeams = postRepository.findByLeaderAndType(findLeader, PostType.TEAM);

        return findMemberTeams.stream()
                .map(TeamAndStudyCreateResponseDto::from)
                .collect(toList());
    }

    public void delete(Long teamId, String memberId) {
        Post findTeam = postRepository.findById(teamId)
                .orElseThrow(PostNotFoundException::new);
        List<Application> teamApplications = applicationRepository.findByPost(findTeam);
        postRepository.deleteByIdAndLeader_LoginId(teamId,memberId);
        applicationRepository.deleteAll(teamApplications);
    }

    public TeamAndStudyCreateResponseDto findOne(Long teamId) {
        Post findTeam = postRepository.findById(teamId)
                .orElseThrow(PostNotFoundException::new);

        return TeamAndStudyCreateResponseDto.from(findTeam);
    }
}
