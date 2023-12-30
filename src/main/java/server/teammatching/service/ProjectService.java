package server.teammatching.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
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

    public ProjectResponseDto create(final String memberId, final ProjectRequestDto requestDto) {
        final Member leader = memberRepository.findByLoginId(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final Post createdProject = create(requestDto, leader);
        final Recruitment recruitment = new Recruitment();
        createdProject.setRecruitment(recruitment);

        final Post savedProject = postRepository.save(createdProject);
        recruitmentRepository.save(recruitment);

        return ProjectResponseDto.from(savedProject);
    }

    public ProjectResponseDto update(final Long projectId, final ProjectRequestDto updateRequest) {
        final Post findProject = postRepository.findById(projectId)
                .orElseThrow(PostNotFoundException::new);
        
        findProject.updateTitle(updateRequest.getTitle());
        findProject.updateField(updateRequest.getField());
        findProject.updateRecruitNumber(updateRequest.getRecruitNumber());
        findProject.updateDesignerNumber(updateRequest.getDesignerNumber());
        findProject.updateFrontendNumber(updateRequest.getFrontendNumber());
        findProject.updateBackendNumber(updateRequest.getBackendNumber());
        findProject.updateContent(updateRequest.getContent());

        final Post savedProject = postRepository.save(findProject);
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
    public ProjectResponseDto findOne(final Long projectId) {
        final Post post = postRepository.findById(projectId)
                .orElseThrow(PostNotFoundException::new);

        return ProjectResponseDto.from(post);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDto> checkMemberProjects(final String memberId) {
        final Member findLeader = memberRepository.findByLoginId(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final List<Post> findMemberProjects = postRepository.findByLeaderAndType(findLeader, PostType.PROJECT);

        return findMemberProjects.stream()
                .map(ProjectResponseDto::from)
                .collect(toList());
    }

    public void delete(final Long postId) {
        final Post findProject = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        final List<Application> projectApplications = applicationRepository.findByPost(findProject);
        postRepository.deleteById(postId);
        applicationRepository.deleteAll(projectApplications);
    }

    private Post create(final ProjectRequestDto requestDto, final Member leader) {
        return Post.createProject(requestDto.getTitle(), requestDto.getField(), requestDto.getTechStack(),
                requestDto.getContent(), requestDto.getRecruitNumber(), requestDto.getDesignerNumber(),
                requestDto.getFrontendNumber(), requestDto.getBackendNumber(), leader);
    }
}
