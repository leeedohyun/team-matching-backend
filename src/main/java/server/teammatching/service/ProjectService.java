package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.teammatching.dto.request.ProjectRequestDto;
import server.teammatching.dto.response.ProjectResponseDto;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;
import server.teammatching.repository.MemberRepository;
import server.teammatching.repository.PostRepository;

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
}
