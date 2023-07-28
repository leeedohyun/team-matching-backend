package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.teammatching.dto.request.ProjectRequestDto;
import server.teammatching.dto.response.ProjectResponseDto;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;
import server.teammatching.entity.PostType;
import server.teammatching.repository.MemberRepository;
import server.teammatching.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public ProjectResponseDto create(ProjectRequestDto requestDto, Long memberId) {
        Member leader = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 id 입니다."));

        Post project = Post.createProject(requestDto, leader);
        Post savedProject = postRepository.save(project);
        return ProjectResponseDto.builder()
                .postId(savedProject.getId())
                .memberId(leader.getId())
                .title(savedProject.getTitle())
                .type(savedProject.getType())
                .designerNumber(savedProject.getDesignerNumber())
                .frontendNumber(savedProject.getFrontendNumber())
                .backendNumber(savedProject.getBackendNumber())
                .content(savedProject.getContent())
                .build();
    }

    public ProjectResponseDto update(ProjectRequestDto updateRequest, Long projectId) {
        Post findProject = postRepository.findById(projectId)
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 팀 id 입니다."));

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
        return ProjectResponseDto.builder()
                .postId(savedProject.getId())
                .memberId(savedProject.getLeader().getId())
                .title(savedProject.getTitle())
                .designerNumber(savedProject.getDesignerNumber())
                .backendNumber(savedProject.getBackendNumber())
                .frontendNumber(savedProject.getFrontendNumber())
                .content(savedProject.getContent())
                .type(savedProject.getType())
                .build();
    }

    public List<ProjectResponseDto> checkAllProjects() {
        List<ProjectResponseDto> allProjects = new ArrayList<>();
        List<Post> findAllProjects = postRepository.findByType(PostType.PROJECT);

        for (Post findProject : findAllProjects) {
            ProjectResponseDto project = ProjectResponseDto.builder()
                    .postId(findProject.getId())
                    .memberId(findProject.getLeader().getId())
                    .title(findProject.getTitle())
                    .designerNumber(findProject.getDesignerNumber())
                    .backendNumber(findProject.getBackendNumber())
                    .frontendNumber(findProject.getFrontendNumber())
                    .content(findProject.getContent())
                    .type(findProject.getType())
                    .build();
            allProjects.add(project);
        }
        return allProjects;
    }

    public List<ProjectResponseDto> checkMemberProjects(Long memberId) {
        Member findLeader = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 id 입니다."));

        List<Post> findMemberProjects = postRepository.findByLeaderAndType(findLeader, PostType.PROJECT);
        List<ProjectResponseDto> memberProjects = new ArrayList<>();

        for (Post findMemberProject : findMemberProjects) {
            ProjectResponseDto project = ProjectResponseDto.builder()
                    .postId(findMemberProject.getId())
                    .memberId(findMemberProject.getLeader().getId())
                    .title(findMemberProject.getTitle())
                    .designerNumber(findMemberProject.getDesignerNumber())
                    .backendNumber(findMemberProject.getBackendNumber())
                    .frontendNumber(findMemberProject.getFrontendNumber())
                    .content(findMemberProject.getContent())
                    .type(findMemberProject.getType())
                    .build();
            memberProjects.add(project);
        }
        return memberProjects;
    }
}
