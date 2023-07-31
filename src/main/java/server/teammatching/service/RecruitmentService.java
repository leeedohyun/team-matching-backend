package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.response.RecruitmentResponse;
import server.teammatching.entity.Application;
import server.teammatching.entity.Post;
import server.teammatching.entity.Recruitment;
import server.teammatching.repository.PostRepository;
import server.teammatching.repository.RecruitmentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;
    private final PostRepository postRepository;

    public void save(Recruitment recruitment) {
        recruitmentRepository.save(recruitment);
    }

    public List<RecruitmentResponse> checkApplications(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 게시글 id 입니다"));
        Recruitment findRecruitment = recruitmentRepository.findByPost(findPost)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 게시글입니다"));
        List<Application> applicationList = findRecruitment.getApplicationList();
        List<RecruitmentResponse> recruitmentResponses = new ArrayList<>();

        for (Application application : applicationList) {
            RecruitmentResponse recruitmentResponse = RecruitmentResponse.builder()
                    .postId(application.getPost().getId())
                    .title(application.getPost().getTitle())
                    .memberId(application.getAppliedMember().getId())
                    .applicationStatus(application.getStatus())
                    .build();
            recruitmentResponses.add(recruitmentResponse);
        }
        return recruitmentResponses;
    }
}
