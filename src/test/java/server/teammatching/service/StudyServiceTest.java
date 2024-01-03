package server.teammatching.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;
import server.teammatching.dto.response.TeamAndStudyCreateResponseDto;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;
import server.teammatching.exception.MemberNotFoundException;
import server.teammatching.exception.PostNotFoundException;
import server.teammatching.repository.ApplicationRepository;
import server.teammatching.repository.MemberRepository;
import server.teammatching.repository.PostRepository;
import server.teammatching.repository.RecruitmentRepository;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private RecruitmentRepository recruitmentRepository;

    @InjectMocks
    private StudyService studyService;

    @Test
    void 스터디_생성() {
        // given
        final Member member = Member.builder()
                .id(1L)
                .loginId("hello")
                .build();

        given(memberRepository.findByLoginId(any()))
                .willReturn(Optional.of(member));

        final Post study = Post.createStudy("title", "content", 1, member);

        given(postRepository.save(any()))
                .willReturn(study);

        // when
        // then
        final TeamAndStudyCreateRequestDto requestDto = TeamAndStudyCreateRequestDto.builder()
                .title("title")
                .recruitNumber(1)
                .content("content")
                .build();

        final TeamAndStudyCreateResponseDto responseDto = studyService.create(requestDto, member.getLoginId());

        assertThat(responseDto).usingRecursiveComparison()
                .ignoringFields("postId")
                .isEqualTo(TeamAndStudyCreateResponseDto.from(study));
    }

    @Test
    void 스터디_생성_시_회원이_존재하지_않으면_예외_발생() {
        // given
        given(memberRepository.findByLoginId(any()))
                .willThrow(MemberNotFoundException.class);

        // when
        // then
        final TeamAndStudyCreateRequestDto requestDto = TeamAndStudyCreateRequestDto.builder()
                .title("title")
                .recruitNumber(1)
                .content("content")
                .build();

        assertThatThrownBy(() -> studyService.create(requestDto, "memberId"))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 모든_스터디_조회() {
        // given
        final Member leader = Member.builder()
                .id(1L)
                .loginId("hello")
                .build();

        final List<Post> studies = List.of(
                Post.builder()
                        .title("title1")
                        .content("content1")
                        .recruitNumber(1)
                        .leader(leader)
                        .build(),
                Post.builder()
                        .title("title2")
                        .content("content2")
                        .recruitNumber(2)
                        .leader(leader)
                        .build()
        );

        given(postRepository.findByType(any()))
                .willReturn(studies);

        // when
        final List<TeamAndStudyCreateResponseDto> responseDtos = studyService.checkAllStudies();

        // then
        assertThat(responseDtos).hasSize(2);
    }

    @Test
    void 회원이_생성한_모든_스터디_조회() {
        final Member leader = Member.builder()
                .id(1L)
                .loginId("hello")
                .build();

        final List<Post> studies = List.of(
                Post.builder()
                        .title("title1")
                        .content("content1")
                        .recruitNumber(1)
                        .leader(leader)
                        .build(),
                Post.builder()
                        .title("title2")
                        .content("content2")
                        .recruitNumber(2)
                        .leader(leader)
                        .build()
        );

        given(memberRepository.findByLoginId(any()))
                .willReturn(Optional.of(leader));

        given(postRepository.findByLeaderAndType(any(), any()))
                .willReturn(studies);

        // when
        final List<TeamAndStudyCreateResponseDto> responseDtos = studyService.checkMemberStudies(leader.getLoginId());

        // then
        assertAll(
                () -> assertThat(responseDtos).hasSize(2),
                () -> assertThat(responseDtos.get(0).getNickName()).isEqualTo(leader.getNickName()),
                () -> assertThat(responseDtos.get(1).getNickName()).isEqualTo(leader.getNickName())
        );
    }

    @Test
    void 회원이_생성한_스터디_조회_시_회원이_존재하지_않는_경우_예외_발생() {
        // given
        given(memberRepository.findByLoginId(any()))
                .willThrow(MemberNotFoundException.class);

        // when
        // then
        assertThatThrownBy(() -> studyService.checkMemberStudies("memberId"))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 스터디_상세_조회() {
        // given
        final Member leader = Member.builder()
                .id(1L)
                .loginId("hello")
                .build();

        final Post study = Post.builder()
                .title("title")
                .content("content")
                .leader(leader)
                .recruitNumber(1)
                .build();

        given(postRepository.findById(any()))
                .willReturn(Optional.of(study));

        // when
        final TeamAndStudyCreateResponseDto responseDto = studyService.findOne(1L);

        // then
        assertThat(responseDto).usingRecursiveComparison()
                .ignoringFields("postId")
                .isEqualTo(TeamAndStudyCreateResponseDto.from(study));
    }

    @Test
    void 스터디_상세_조회_시_예외_발생() {
        // given
        given(postRepository.findById(any()))
                .willThrow(PostNotFoundException.class);

        // when
        // then
        assertThatThrownBy(() -> studyService.findOne(1L))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void 스터디_삭제_시_스터디가_존재하지_않으면_예외_발생() {
        // given
        given(postRepository.findById(any()))
                .willThrow(PostNotFoundException.class);

        // when
        // then
        assertThatThrownBy(() -> studyService.delete(1L))
                .isInstanceOf(PostNotFoundException.class);
    }
}
