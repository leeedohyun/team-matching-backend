package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.response.ApplicationResponse;
import server.teammatching.entity.*;
import server.teammatching.exception.MemberNotFoundException;
import server.teammatching.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {

    private final AlarmRepository alarmRepository;
    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final RecruitmentRepository recruitmentRepository;

    public ApplicationResponse applyProject(Long projectId, String memberId) {
        return getApplicationResponse(memberId, projectId, PostType.PROJECT, "유효하지 않은 프로젝트 id 입니다.");
    }

    public ApplicationResponse applyStudy(Long studyId, String memberId) {
        return getApplicationResponse(memberId, studyId, PostType.STUDY, "유효하지 않은 스터디 id 입니다.");
    }

    public ApplicationResponse applyTeam(Long teamId, String memberId) {
        return getApplicationResponse(memberId, teamId, PostType.TEAM, "유효하지 않은 스터디 id 입니다.");
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponse> checkAllApplications(String memberId) {
        Member findMember = memberRepository.findByLoginId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("유효하지 않은 회원 id 입니다."));
        List<Application> appliedList = applicationRepository.findByAppliedMember(findMember);
        List<ApplicationResponse> appliedResponses = new ArrayList<>();

        for (Application application : appliedList) {
            ApplicationResponse response = ApplicationResponse.builder()
                    .title(application.getPost().getTitle())
                    .postId(application.getPost().getId())
                    .applicationStatus(application.getStatus())
                    .build();
            appliedResponses.add(response);
        }
        return appliedResponses;
    }

    public void deleteApplication(Long applicationId, String memberId) {
        applicationRepository.deleteByIdAndAppliedMember_LoginId(applicationId, memberId);
    }

    public ApplicationResponse approveApplication(Long applicationId, String memberId) {
        return getApplicationResponse(applicationId, ApplicationStatus.승인);
    }

    public ApplicationResponse rejectApplication(Long applicationId, String memberId) {
        return getApplicationResponse(applicationId, ApplicationStatus.거절);
    }

    private ApplicationResponse getApplicationResponse(String memberId, Long postId, PostType type, String message) {
        Member appliedMember = memberRepository.findByLoginId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("유효하지 않은 회원 id 입니다."));
        Post post = postRepository.findByIdAndType(postId, type)
                .orElseThrow(() -> new RuntimeException(message));
        Recruitment recruitment = recruitmentRepository.findByPost(post)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 id 입니다."));
        Alarm alarm = Alarm.createAlarm(post.getLeader(), post);

        if (post.getStatus() == PostStatus.모집완료) {
            throw new RuntimeException("모집이 완료된 게시글입니다.");
        }

        Application application = Application.apply(appliedMember, post, recruitment);
        applicationRepository.save(application);
        alarmRepository.save(alarm);

        return ApplicationResponse.builder()
                .postId(application.getPost().getId())
                .title(application.getPost().getTitle())
                .applicationStatus(application.getStatus())
                .build();
    }

    private ApplicationResponse getApplicationResponse(
            Long applicationId,
            ApplicationStatus applicationStatus) {
        Application findApplication = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 지원입니다."));
        Alarm alarm = Alarm.createAlarm(findApplication.getAppliedMember(), findApplication.getPost());

        if (findApplication.getStatus() == ApplicationStatus.대기중) {
            findApplication.updateStatus(applicationStatus);
        }

        alarmRepository.save(alarm);

        return ApplicationResponse.builder()
                .title(findApplication.getPost().getTitle())
                .postId(findApplication.getPost().getId())
                .applicationStatus(findApplication.getStatus())
                .build();
    }
}
