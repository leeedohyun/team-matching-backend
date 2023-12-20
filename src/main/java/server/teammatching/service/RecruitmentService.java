package server.teammatching.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.teammatching.dto.response.RecruitmentResponse;
import server.teammatching.entity.Application;
import server.teammatching.entity.Post;
import server.teammatching.entity.PostStatus;
import server.teammatching.entity.Recruitment;
import server.teammatching.exception.PostNotFoundException;
import server.teammatching.exception.RecruitNotFoundException;
import server.teammatching.repository.PostRepository;
import server.teammatching.repository.RecruitmentRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruitmentService {

    private final PostRepository postRepository;
    private final RecruitmentRepository recruitmentRepository;

    public void save(Recruitment recruitment) {
        recruitmentRepository.save(recruitment);
    }

    public String closeRecruitment(Long postId, String loginId) {
        Post findPost = postRepository.findByIdAndLeader_LoginId(postId, loginId)
                .orElseThrow(PostNotFoundException::new);

        findPost.updatePostStatus(PostStatus.모집완료);
        return findPost.getTitle();
    }

    @Transactional(readOnly = true)
    public List<RecruitmentResponse> checkApplications(Long postId, String memberId) {
        Post findPost = postRepository.findByIdAndLeader_LoginId(postId, memberId)
                .orElseThrow(PostNotFoundException::new);
        Recruitment findRecruitment = recruitmentRepository.findByPost(findPost)
                .orElseThrow(() -> new RecruitNotFoundException("유효하지 않은 채용입니다"));
        List<Application> applicationList = findRecruitment.getApplicationList();

        return applicationList.stream()
                .map(RecruitmentResponse::from)
                .collect(toList());
    }
}
