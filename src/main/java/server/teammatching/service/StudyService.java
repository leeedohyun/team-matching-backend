package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;
import server.teammatching.dto.response.TeamAndStudyCreateResponseDto;
import server.teammatching.entity.*;
import server.teammatching.exception.MemberNotFoundException;
import server.teammatching.exception.PostNotFoundException;
import server.teammatching.repository.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final RecruitmentRepository recruitmentRepository;

    public TeamAndStudyCreateResponseDto create(TeamAndStudyCreateRequestDto requestDto, String memberId) {
        Member leader = memberRepository.findByLoginId(memberId)
                .orElseThrow(() -> new MemberNotFoundException("유효하지 않은 사용자 id 입니다."));

        Post createdStudy = Post.createStudy(requestDto, leader);

        Recruitment recruitment = new Recruitment();
        createdStudy.setRecruitment(recruitment);

        Post savedStudy = postRepository.save(createdStudy);
        recruitmentRepository.save(recruitment);

        return TeamAndStudyCreateResponseDto.builder()
                .title(savedStudy.getTitle())
                .postId(savedStudy.getId())
                .nickName(leader.getNickName())
                .type(savedStudy.getType())
                .content(savedStudy.getContent())
                .build();
    }

    public TeamAndStudyCreateResponseDto update(Long studyId, String memberId, TeamAndStudyCreateRequestDto updateRequest) {
        Post findStudy = postRepository.findById(studyId)
                .orElseThrow(() -> new PostNotFoundException("유효하지 않은 팀 id 입니다."));

        if (!memberId.equals(findStudy.getLeader().getLoginId())) {
            throw new RuntimeException("Invalid");
        }
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
                .nickName(savedStudy.getLeader().getNickName())
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
                    .nickName(findStudy.getLeader().getNickName())
                    .title(findStudy.getTitle())
                    .type(findStudy.getType())
                    .content(findStudy.getContent())
                    .build();
            allStudies.add(study);
        }
        return allStudies;
    }

    @Transactional(readOnly = true)
    public List<TeamAndStudyCreateResponseDto> checkMemberStudies(String memberId, String authenticatedId) {
        if (!memberId.equals(authenticatedId)) {
            throw new RuntimeException("Invalid");
        }
        
        Member findLeader = memberRepository.findByLoginId(memberId)            .
                orElseThrow(() -> new MemberNotFoundException("유효하지 않은 사용자 id 입니다."));
        List<Post> findMemberStudies = postRepository.findByLeaderAndType(findLeader, PostType.STUDY);
        List<TeamAndStudyCreateResponseDto> allMemberStudies = new ArrayList<>();

        for (Post findMemberStudy : findMemberStudies) {
            TeamAndStudyCreateResponseDto study = TeamAndStudyCreateResponseDto.builder()
                    .postId(findMemberStudy.getId())
                    .nickName(findMemberStudy.getLeader().getNickName())
                    .title(findMemberStudy.getTitle())
                    .type(findMemberStudy.getType())
                    .content(findMemberStudy.getContent())
                    .build();
            allMemberStudies.add(study);
        }
        return allMemberStudies;
    }

    public void delete(Long studyId, String memberId) {
        Post findStudy = postRepository.findById(studyId)
                .orElseThrow(() -> new PostNotFoundException("유효하지 않은 스터디 id 입니다."));
        List<Application> studyApplications = applicationRepository.findByPost(findStudy);
        postRepository.deleteByIdAndLeader_LoginId(studyId, memberId);
        applicationRepository.deleteAll(studyApplications);
    }
}
