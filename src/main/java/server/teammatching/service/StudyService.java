package server.teammatching.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;
import server.teammatching.dto.response.TeamAndStudyCreateResponseDto;
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
public class StudyService {

    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final RecruitmentRepository recruitmentRepository;

    public TeamAndStudyCreateResponseDto create(final TeamAndStudyCreateRequestDto requestDto, final String memberId) {
        final Member leader = memberRepository.findByLoginId(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final Post createdStudy = Post.createStudy(requestDto.getTitle(), requestDto.getContent(),
                requestDto.getRecruitNumber(), leader);

        final Recruitment recruitment = new Recruitment();
        createdStudy.setRecruitment(recruitment);

        final Post savedStudy = postRepository.save(createdStudy);
        recruitmentRepository.save(recruitment);

        return TeamAndStudyCreateResponseDto.from(savedStudy);
    }

    public TeamAndStudyCreateResponseDto update(final Long studyId, final TeamAndStudyCreateRequestDto updateRequest) {
        final Post findStudy = postRepository.findById(studyId)
                .orElseThrow(PostNotFoundException::new);

        findStudy.updateTitle(updateRequest.getTitle());
        findStudy.updateContent(updateRequest.getContent());
        findStudy.updateRecruitNumber(updateRequest.getRecruitNumber());

        final Post savedStudy = postRepository.save(findStudy);
        return TeamAndStudyCreateResponseDto.from(savedStudy);
    }

    @Transactional(readOnly = true)
    public TeamAndStudyCreateResponseDto findOne(final Long studyId) {
        final Post findStudy = postRepository.findById(studyId)
                .orElseThrow(PostNotFoundException::new);
        return TeamAndStudyCreateResponseDto.from(findStudy);
    }

    @Transactional(readOnly = true)
    public List<TeamAndStudyCreateResponseDto> checkAllStudies() {
        final List<Post> findStudies = postRepository.findByType(PostType.STUDY);
        return findStudies.stream()
                .map(TeamAndStudyCreateResponseDto::from)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<TeamAndStudyCreateResponseDto> checkMemberStudies(final String memberId) {
        final Member findLeader = memberRepository.findByLoginId(memberId)            .
                orElseThrow(MemberNotFoundException::new);
        final List<Post> findMemberStudies = postRepository.findByLeaderAndType(findLeader, PostType.STUDY);

        return findMemberStudies.stream()
                .map(TeamAndStudyCreateResponseDto::from)
                .collect(toList());
    }

    public void delete(final Long studyId) {
        final Post findStudy = postRepository.findById(studyId)
                .orElseThrow(PostNotFoundException::new);
        final List<Application> studyApplications = applicationRepository.findByPost(findStudy);
        postRepository.deleteById(studyId);
        applicationRepository.deleteAll(studyApplications);
    }
}
