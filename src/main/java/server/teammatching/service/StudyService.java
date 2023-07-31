package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;
import server.teammatching.dto.response.TeamAndStudyCreateResponseDto;
import server.teammatching.entity.*;
import server.teammatching.repository.ApplicationRepository;
import server.teammatching.repository.MemberRepository;
import server.teammatching.repository.PostRepository;
import server.teammatching.repository.RecruitmentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final ApplicationRepository applicationRepository;

    public TeamAndStudyCreateResponseDto create(TeamAndStudyCreateRequestDto requestDto, Long memberId) {
        Member leader = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 id 입니다."));

        Post createdStudy = Post.createStudy(requestDto, leader);

        Recruitment recruitment = new Recruitment();
        createdStudy.setRecruitment(recruitment);

        Post savedStudy = postRepository.save(createdStudy);
        recruitmentRepository.save(recruitment);

        return TeamAndStudyCreateResponseDto.builder()
                .title(savedStudy.getTitle())
                .postId(savedStudy.getId())
                .memberId(leader.getId())
                .type(savedStudy.getType())
                .content(savedStudy.getContent())
                .build();
    }

    public TeamAndStudyCreateResponseDto update(TeamAndStudyCreateRequestDto updateRequest, Long studyId) {
        Post findStudy = postRepository.findById(studyId)
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 팀 id 입니다."));

        if (updateRequest.getTitle() != "") {
            findStudy.updateTitle(updateRequest.getTitle());
        }
        if (updateRequest.getField() != "") {
            findStudy.updateField(updateRequest.getField());
        }
        if (updateRequest.getRecruitNumber() != 0) {
            findStudy.updateRecruitNumber(updateRequest.getRecruitNumber());
        }
        if (updateRequest.getContent() != "") {
            findStudy.updateContent(updateRequest.getContent());
        }

        Post savedStudy = postRepository.save(findStudy);
        return TeamAndStudyCreateResponseDto.builder()
                .postId(savedStudy.getId())
                .memberId(savedStudy.getLeader().getId())
                .title(savedStudy.getTitle())
                .type(savedStudy.getType())
                .content(savedStudy.getContent())
                .build();
    }

    @Transactional(readOnly = true)
    public List<TeamAndStudyCreateResponseDto> checkAllStudies() {
        List<Post> findStudies = postRepository.findByType(PostType.STUDY);
        List<TeamAndStudyCreateResponseDto> allStudies = new ArrayList<>();

        for (Post findStudy : findStudies) {
            TeamAndStudyCreateResponseDto study = TeamAndStudyCreateResponseDto.builder()
                    .postId(findStudy.getId())
                    .memberId(findStudy.getLeader().getId())
                    .title(findStudy.getTitle())
                    .type(findStudy.getType())
                    .content(findStudy.getContent())
                    .build();
            allStudies.add(study);
        }
        return allStudies;
    }

    @Transactional(readOnly = true)
    public List<TeamAndStudyCreateResponseDto> checkMemberStudies(Long memberId) {
        Member findLeader = memberRepository.findById(memberId)            .
                orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 id 입니다."));
        List<Post> findMemberStudies = postRepository.findByLeaderAndType(findLeader, PostType.STUDY);
        List<TeamAndStudyCreateResponseDto> allMemberStudies = new ArrayList<>();

        for (Post findMemberStudy : findMemberStudies) {
            TeamAndStudyCreateResponseDto study = TeamAndStudyCreateResponseDto.builder()
                    .postId(findMemberStudy.getId())
                    .memberId(findMemberStudy.getLeader().getId())
                    .title(findMemberStudy.getTitle())
                    .type(findMemberStudy.getType())
                    .content(findMemberStudy.getContent())
                    .build();
            allMemberStudies.add(study);
        }
        return allMemberStudies;
    }

    public void delete(Long studyId) {
        Post findStudy = postRepository.findById(studyId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 스터디 id 입니다."));
        List<Application> studyApplications = applicationRepository.findByPost(findStudy);
        postRepository.delete(findStudy);
        applicationRepository.deleteAll(studyApplications);
    }
}
