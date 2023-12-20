package server.teammatching.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.teammatching.auth.AuthenticationUtils;
import server.teammatching.dto.request.ProjectRequestDto;
import server.teammatching.dto.response.ProjectResponseDto;
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
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final RecruitmentRepository recruitmentRepository;

    public ProjectResponseDto create(String memberId, ProjectRequestDto requestDto) {
        Member leader = memberRepository.findByLoginId(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Post createdProject = Post.createProject(requestDto, leader);

        Recruitment recruitment = new Recruitment();
        createdProject.setRecruitment(recruitment);

        Post savedProject = postRepository.save(createdProject);
        recruitmentRepository.save(recruitment);

        return ProjectResponseDto.from(savedProject);
    }

    public ProjectResponseDto update(Long projectId, String memberId, ProjectRequestDto updateRequest) {
        Post findProject = postRepository.findById(projectId)
                .orElseThrow(PostNotFoundException::new);
        AuthenticationUtils.verifyLoggedInUser(memberId, findProject.getLeader().getLoginId());

        if (updateRequest.getTitle() != null) {
            findProject.updateTitle(updateRequest.getTitle());
        }
        if (updateRequest.getField() != null) {
            findProject.updateField(updateRequest.getField());
        }
        if (updateRequest.getRecruitNumber() != 0) {
            findProject.updateRecruitNumber(updateRequest.getRecruitNumber());
        }
        if (updateRequest.getFrontendNumber() != 0) {
            findProject.updateFrontendNumber(updateRequest.getFrontendNumber());
        }
        if (updateRequest.getBackendNumber() != 0) {
            findProject.updateBackendNumber(updateRequest.getBackendNumber());
        }
        if (updateRequest.getDesignerNumber() != 0) {
            findProject.updateDesignerNumber(updateRequest.getDesignerNumber());
        }
        if (updateRequest.getContent() != null) {
            findProject.updateContent(updateRequest.getContent());
        }

        Post savedProject = postRepository.save(findProject);
        return ProjectResponseDto.from(savedProject);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDto> checkAllProjects() {
        List<Post> findAllProjects = postRepository.findByType(PostType.PROJECT);

        return findAllProjects.stream()
                .map(ProjectResponseDto::from)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDto> checkMemberProjects(String memberId, String authenticatedId) {
        AuthenticationUtils.verifyLoggedInUser(memberId, authenticatedId);
        Member findLeader = memberRepository.findByLoginId(memberId)
                .orElseThrow(MemberNotFoundException::new);

        List<Post> findMemberProjects = postRepository.findByLeaderAndType(findLeader, PostType.PROJECT);

        return findMemberProjects.stream()
                .map(ProjectResponseDto::from)
                .collect(toList());
    }

    public void delete(Long postId, String memberId) {
        Post findProject = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        List<Application> projectApplications = applicationRepository.findByPost(findProject);
        postRepository.deleteByIdAndLeader_LoginId(postId, memberId);
        applicationRepository.deleteAll(projectApplications);
    }

    public ProjectResponseDto findOne(Long projectId) {
        Post post = postRepository.findById(projectId)
                .orElseThrow(PostNotFoundException::new);
        
        return ProjectResponseDto.from(post);
    }
}
