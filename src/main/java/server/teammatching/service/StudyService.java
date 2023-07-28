package server.teammatching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;
import server.teammatching.dto.response.TeamAndStudyCreateResponseDto;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;
import server.teammatching.entity.PostType;
import server.teammatching.repository.MemberRepository;
import server.teammatching.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public TeamAndStudyCreateResponseDto create(TeamAndStudyCreateRequestDto requestDto, Long memberId) {
        Member leader = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 사용자 id 입니다."));

        Post createdStudy = Post.createStudy(requestDto, leader);
        Post savedStudy = postRepository.save(createdStudy);
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
}
