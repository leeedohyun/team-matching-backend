package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.request.ResumeDto;
import server.teammatching.auth.AuthenticationUtils;
import server.teammatching.dto.response.ApplicationResponse;
import server.teammatching.entity.Alarm;
import server.teammatching.entity.Application;
import server.teammatching.entity.ApplicationStatus;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;
import server.teammatching.entity.PostStatus;
import server.teammatching.entity.PostType;
import server.teammatching.entity.Recruitment;
import server.teammatching.exception.AlreadyApplicationException;
import server.teammatching.exception.ApplicationException;
import server.teammatching.exception.ApplicationNotFoundException;
import server.teammatching.exception.MemberNotFoundException;
import server.teammatching.exception.PostNotFoundException;
import server.teammatching.exception.RecruitNotFoundException;
import server.teammatching.exception.RecruitmentCompletedException;
import server.teammatching.repository.AlarmRepository;
import server.teammatching.repository.ApplicationRepository;
import server.teammatching.repository.MemberRepository;
import server.teammatching.repository.PostRepository;
import server.teammatching.repository.RecruitmentRepository;

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

    public ApplicationResponse applyProject(Long projectId, String memberId, ResumeDto resumeDto) {
        validateMyRecruitment(projectId, memberId);
        return getApplicationResponse(memberId, projectId, resumeDto.getResume(), PostType.PROJECT, "유효하지 않은 프로젝트 id 입니다.");
    }

    public ApplicationResponse applyStudy(Long studyId, String memberId, String resume) {
        return getApplicationResponse(memberId, studyId, resume, PostType.STUDY, "유효하지 않은 스터디 id 입니다.");
    }

    public ApplicationResponse applyTeam(Long teamId, String memberId, String resume) {
        return getApplicationResponse(memberId, teamId, resume, PostType.TEAM, "유효하지 않은 스터디 id 입니다.");
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

    private ApplicationResponse getApplicationResponse(String memberId,
                                                       Long postId,
                                                       String resume,
                                                       PostType type,
                                                       String message) {
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

        Application application = Application.apply(appliedMember, post, recruitment, resume);
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

    private void validateMyRecruitment(Long postId, String memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("존재하지 않는 ID입니다."));
        if (post.getLeader().getLoginId() == memberId) {
            throw new ApplicationException("본인이 생성한 팀에는 지원할 수 없습니다.");
        }
    }
}
