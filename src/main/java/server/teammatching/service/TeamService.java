package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.List;

import static java.util.stream.Collectors.toList;

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

        return TeamAndStudyCreateResponseDto.from(savedTeam);
    }

    public TeamAndStudyCreateResponseDto update(Long postId, TeamAndStudyCreateRequestDto requestDto, String memberId) {
        Post findTeam = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("유효하지 않은 팀 id 입니다."));
        AuthenticationUtils.verifyLoggedInUser(memberId, findTeam.getLeader().getLoginId());

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
                .orElseThrow(() -> new MemberNotFoundException("유효하지 않은 사용자 id 입니다."));
        List<Post> findMemberTeams = postRepository.findByLeaderAndType(findLeader, PostType.TEAM);

        return findMemberTeams.stream()
                .map(TeamAndStudyCreateResponseDto::from)
                .collect(toList());
    }

    public void delete(Long teamId, String memberId) {
        Post findTeam = postRepository.findById(teamId)
                .orElseThrow(() -> new PostNotFoundException("유효하지 않은 스터디 id 입니다."));
        List<Application> teamApplications = applicationRepository.findByPost(findTeam);
        postRepository.deleteByIdAndLeader_LoginId(teamId,memberId);
        applicationRepository.deleteAll(teamApplications);
    }

    public TeamAndStudyCreateResponseDto findOne(Long teamId) {
        Post findTeam = postRepository.findById(teamId)
                .orElseThrow(() -> new PostNotFoundException("유효하지 않은 스터디 id 입니다."));

        return TeamAndStudyCreateResponseDto.from(findTeam);
    }
}
