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
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final RecruitmentRepository recruitmentRepository;

    public TeamAndStudyCreateResponseDto create(TeamAndStudyCreateRequestDto requestDto, String memberId) {
        Member leader = memberRepository.findByLoginId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("유효하지 않은 사용자 id 입니다."));

        Post createdStudy = Post.createStudy(requestDto, leader);

        Recruitment recruitment = new Recruitment();
        createdStudy.setRecruitment(recruitment);

        Post savedStudy = postRepository.save(createdStudy);
        recruitmentRepository.save(recruitment);

        return TeamAndStudyCreateResponseDto.from(savedStudy);
    }

    public TeamAndStudyCreateResponseDto update(Long studyId, String memberId, TeamAndStudyCreateRequestDto updateRequest) {
        Post findStudy = postRepository.findById(studyId)
                .orElseThrow(() -> new PostNotFoundException("유효하지 않은 팀 id 입니다."));
        AuthenticationUtils.verifyLoggedInUser(memberId, findStudy.getLeader().getLoginId());

        if (!updateRequest.getTitle().isEmpty()) {
            findStudy.updateTitle(updateRequest.getTitle());
        }
        if (updateRequest.getRecruitNumber() != 0) {
            findStudy.updateRecruitNumber(updateRequest.getRecruitNumber());
        }
        if (!updateRequest.getContent().isEmpty()) {
            findStudy.updateContent(updateRequest.getContent());
        }

        Post savedStudy = postRepository.save(findStudy);
        return TeamAndStudyCreateResponseDto.from(savedStudy);
    }

    @Transactional(readOnly = true)
    public List<TeamAndStudyCreateResponseDto> checkAllStudies() {
        List<Post> findStudies = postRepository.findByType(PostType.STUDY);

        return findStudies.stream()
                .map(TeamAndStudyCreateResponseDto::from)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<TeamAndStudyCreateResponseDto> checkMemberStudies(String memberId, String authenticatedId) {
        AuthenticationUtils.verifyLoggedInUser(memberId, authenticatedId);
        Member findLeader = memberRepository.findByLoginId(memberId)            .
                orElseThrow(() -> new MemberNotFoundException("유효하지 않은 사용자 id 입니다."));
        List<Post> findMemberStudies = postRepository.findByLeaderAndType(findLeader, PostType.STUDY);

        return findMemberStudies.stream()
                .map(TeamAndStudyCreateResponseDto::from)
                .collect(toList());
    }

    public void delete(Long studyId, String memberId) {
        Post findStudy = postRepository.findById(studyId)
                .orElseThrow(() -> new PostNotFoundException("유효하지 않은 스터디 id 입니다."));
        List<Application> studyApplications = applicationRepository.findByPost(findStudy);
        postRepository.deleteByIdAndLeader_LoginId(studyId, memberId);
        applicationRepository.deleteAll(studyApplications);
    }

    public TeamAndStudyCreateResponseDto findOne(Long studyId) {
        Post findStudy = postRepository.findById(studyId)
                .orElseThrow(() -> new PostNotFoundException("유효하지 않은 스터디 id 입니다."));

        return TeamAndStudyCreateResponseDto.from(findStudy);
    }
}
