package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.auth.AuthenticationUtils;
import server.teammatching.dto.response.ApplicationResponse;
import server.teammatching.entity.*;
import server.teammatching.exception.*;
import server.teammatching.repository.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

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

        return appliedList.stream()
                .map(ApplicationResponse::from)
                .collect(toList());
    }

    public void deleteApplication(Long applicationId, String memberId) {
        applicationRepository.deleteByIdAndAppliedMember_LoginId(applicationId, memberId);
    }

    public ApplicationResponse approveApplication(Long applicationId, String memberId) {
        return getApplicationResponse(applicationId, memberId, ApplicationStatus.승인);
    }

    public ApplicationResponse rejectApplication(Long applicationId, String memberId) {
        return getApplicationResponse(applicationId, memberId, ApplicationStatus.거절);
    }

    private static void validateApplicationStatusWaiting(Application findApplication, ApplicationStatus applicationStatus) {
        if (findApplication.getStatus() == ApplicationStatus.대기중) {
            findApplication.updateStatus(applicationStatus);
        }
    }

    private static void validateRecruitStatusCompleted(Post post) {
        if (post.getStatus() == PostStatus.모집완료) {
            throw new RecruitmentCompletedException("모집이 완료된 게시글입니다.");
        }
    }

    private ApplicationResponse getApplicationResponse(String memberId, Long postId, PostType type, String message) {
        Member appliedMember = memberRepository.findByLoginId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("유효하지 않은 회원 id 입니다."));
        Post post = postRepository.findByIdAndType(postId, type)
                .orElseThrow(() -> new PostNotFoundException(message));
        Recruitment recruitment = recruitmentRepository.findByPost(post)
                .orElseThrow(() -> new RecruitNotFoundException("유효하지 않은 id 입니다."));
        Alarm alarm = Alarm.createAlarm(post.getLeader(), post);

        validateRecruitStatusCompleted(post);
        validateApplicationNotProcessed(appliedMember, post);
        validateRecruitCompleted(post);

        Application application = Application.apply(appliedMember, post, recruitment);
        applicationRepository.save(application);
        alarmRepository.save(alarm);

        return ApplicationResponse.from(application);
    }

    private static void validateRecruitCompleted(Post post) {
        if (post.getStatus() == PostStatus.모집완료) {
            throw new RecruitmentCompletedException("이미 모집이 완료되었습니다.");
        }
    }

    private ApplicationResponse getApplicationResponse(Long applicationId,
                                                       String memberId,
                                                       ApplicationStatus applicationStatus) {
        Application findApplication = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("유효하지 않은 지원입니다."));
        Alarm alarm = Alarm.createAlarm(findApplication.getAppliedMember(), findApplication.getPost());

        AuthenticationUtils.verifyLoggedInUser(memberId, findApplication.getPost().getLeader().getLoginId());
        validateApplicationStatusWaiting(findApplication, applicationStatus);

        alarmRepository.save(alarm);

        return ApplicationResponse.from(findApplication);
    }

    private void validateApplicationNotProcessed(Member appliedMember, Post post) {
        if (applicationRepository.findByAppliedMemberAndPost(appliedMember, post).isPresent()) {
            throw new AlreadyApplicationException("이미 지원한 상태입니다.");
        }
    }
}
