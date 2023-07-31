package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.response.ApplicationResponse;
import server.teammatching.entity.*;
import server.teammatching.repository.ApplicationRepository;
import server.teammatching.repository.MemberRepository;
import server.teammatching.repository.PostRepository;
import server.teammatching.repository.RecruitmentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final RecruitmentRepository recruitmentRepository;

    public ApplicationResponse applyProject(Long projectId, Long memberId) {
        return getApplicationResponse(memberId, projectId, PostType.PROJECT, "유효하지 않은 프로젝트 id 입니다.");
    }

    public ApplicationResponse applyStudy(Long studyId, Long memberId) {
        return getApplicationResponse(memberId, studyId, PostType.STUDY, "유효하지 않은 스터디 id 입니다.");
    }

    public ApplicationResponse applyTeam(Long teamId, Long memberId) {
        return getApplicationResponse(memberId, teamId, PostType.TEAM, "유효하지 않은 스터디 id 입니다.");
    }

    private ApplicationResponse getApplicationResponse(Long memberId, Long postId, PostType type, String message) {
        Member appliedMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 회원 id 입니다."));
        Post post = postRepository.findByIdAndType(postId, type)
                .orElseThrow(() -> new RuntimeException(message));
        Recruitment recruitment = recruitmentRepository.findByPost(post)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 게시글 id 입니다."));

        Application application = Application.apply(appliedMember, post, recruitment);
        applicationRepository.save(application);

        return ApplicationResponse.builder()
                .id(application.getPost().getId())
                .title(application.getPost().getTitle())
                .applicationStatus(application.getStatus())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> checkAllApplications(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 회원 id 입니다."));
        List<Application> appliedList = applicationRepository.findByAppliedMember(findMember);
        List<ApplicationResponse> appliedResponses = new ArrayList<>();

        for (Application application : appliedList) {
            ApplicationResponse response = ApplicationResponse.builder()
                    .title(application.getPost().getTitle())
                    .id(application.getPost().getId())
                    .applicationStatus(application.getStatus())
                    .build();
            appliedResponses.add(response);
        }
        return appliedResponses;
    }

    public void deleteApplication(Long applicationId) {
        Application findApplication = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Id 입니다"));
        applicationRepository.delete(findApplication);
    }
}
