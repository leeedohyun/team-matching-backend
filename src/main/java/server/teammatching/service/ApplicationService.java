package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.teammatching.dto.response.ApplicationResponse;
import server.teammatching.entity.Application;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;
import server.teammatching.entity.PostType;
import server.teammatching.repository.ApplicationRepository;
import server.teammatching.repository.MemberRepository;
import server.teammatching.repository.PostRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public ApplicationResponse applyProject(Long projectId, Long memberId) {
        Member appliedMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 회원 id 입니다."));
        Post project = postRepository.findByIdAndType(projectId, PostType.PROJECT)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 프로젝트 id 입니다."));

        Application application = Application.applyProject(appliedMember, project);
        applicationRepository.save(application);

        return ApplicationResponse.builder()
                .id(application.getPost().getId())
                .title(application.getPost().getTitle())
                .build();
    }

    public ApplicationResponse applyStudy(Long studyId, Long memberId) {
        Member appliedMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 회원 id 입니다."));
        Post study = postRepository.findByIdAndType(studyId, PostType.STUDY)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 스터디 id 입니다."));

        Application application = Application.applyProject(appliedMember, study);
        applicationRepository.save(application);

        return ApplicationResponse.builder()
                .id(application.getPost().getId())
                .title(application.getPost().getTitle())
                .build();
    }
}
