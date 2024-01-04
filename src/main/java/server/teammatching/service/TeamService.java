package server.teammatching.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
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

    public TeamAndStudyCreateResponseDto create(final TeamAndStudyCreateRequestDto request, final String memberId) {
        final Member leader = memberRepository.findByLoginId(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final Post createdTeam = Post.createTeam(request.getTitle(), request.getContent(), request.getRecruitNumber(),
                leader);
        final Recruitment recruitment = new Recruitment();
        createdTeam.setRecruitment(recruitment);

        final Post savedTeam = postRepository.save(createdTeam);
        recruitmentRepository.save(recruitment);

        return TeamAndStudyCreateResponseDto.from(savedTeam);
    }

    @Transactional(readOnly = true)
    public TeamAndStudyCreateResponseDto findOne(final Long teamId) {
        final Post findTeam = postRepository.findById(teamId)
                .orElseThrow(PostNotFoundException::new);
        return TeamAndStudyCreateResponseDto.from(findTeam);
    }

    public TeamAndStudyCreateResponseDto update(final Long postId, final TeamAndStudyCreateRequestDto requestDto) {
        final Post findTeam = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        findTeam.updateTitle(requestDto.getTitle());
        findTeam.updateContent(requestDto.getContent());
        findTeam.updateRecruitNumber(requestDto.getRecruitNumber());

        final Post savedTeam = postRepository.save(findTeam);
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
    public List<TeamAndStudyCreateResponseDto> checkMemberTeams(final String memberId) {
        final Member findLeader = memberRepository.findByLoginId(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final List<Post> findMemberTeams = postRepository.findByLeaderAndType(findLeader, PostType.TEAM);
        return findMemberTeams.stream()
                .map(TeamAndStudyCreateResponseDto::from)
                .collect(toList());
    }

    public void delete(final Long teamId) {
        final Post findTeam = postRepository.findById(teamId)
                .orElseThrow(PostNotFoundException::new);
        final List<Application> teamApplications = applicationRepository.findByPost(findTeam);
        postRepository.deleteById(teamId);
        applicationRepository.deleteAll(teamApplications);
    }
}
