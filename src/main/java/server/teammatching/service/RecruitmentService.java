package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.response.RecruitmentResponse;
import server.teammatching.entity.Application;
import server.teammatching.entity.Post;
import server.teammatching.entity.Recruitment;
import server.teammatching.exception.MemberNotFoundException;
import server.teammatching.repository.PostRepository;
import server.teammatching.repository.RecruitmentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitmentService {

    private final PostRepository postRepository;
    private final RecruitmentRepository recruitmentRepository;

    public void save(Recruitment recruitment) {
        recruitmentRepository.save(recruitment);
    }

    public List<RecruitmentResponse> checkApplications(Long postId, String memberId) {
        Post findPost = postRepository.findByIdAndLeader_LoginId(postId, memberId)
                .orElseThrow(() -> new MemberNotFoundException("유효하지 않은 게시글 입니다"));
        Recruitment findRecruitment = recruitmentRepository.findByPost(findPost)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 채용입니다"));
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
