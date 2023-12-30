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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import server.teammatching.dto.request.ProjectRequestDto;
import server.teammatching.dto.response.ProjectResponseDto;
import server.teammatching.entity.Member;
import server.teammatching.entity.Post;
import server.teammatching.entity.PostType;
import server.teammatching.exception.InsufficientMembersException;
import server.teammatching.exception.MemberNotFoundException;
import server.teammatching.exception.PostNotFoundException;
import server.teammatching.repository.ApplicationRepository;
import server.teammatching.repository.MemberRepository;
import server.teammatching.repository.PostRepository;
import server.teammatching.repository.RecruitmentRepository;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    private static final Member LEADER = createMember();
    private static final List<Post> PROJECTS = createProjects();

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private RecruitmentRepository recruitmentRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void 프로젝트_생성_시_존재하지_않는_회원인_경우_예외_발생() {
        // given
        final String memberId = "hello";

        given(memberRepository.findByLoginId(any())).willThrow(MemberNotFoundException.class);

        // when
        // then
        final ProjectRequestDto requestDto = ProjectRequestDto.builder().title("프로젝트 모집합니다.").backendNumber(1)
                .field("백엔드, 디자이너").designerNumber(1).frontendNumber(0).recruitNumber(2)
                .content("안녕하세요. 프로젝트 기획을 하게 되었는데, 같이 할 팀원 구합히다.,").build();

        assertThatThrownBy(() -> projectService.create(memberId, requestDto)).isInstanceOf(
                MemberNotFoundException.class);
    }

    @Test
    void 프로젝트_생성_시_전체_인원과_분야별_인원_수의_합이_일치하지_않으면_예외_발생() {
        // given
        given(memberRepository.findByLoginId(any())).willReturn(Optional.of(LEADER));

        // when
        // then
        final ProjectRequestDto requestDto = ProjectRequestDto.builder().backendNumber(1).designerNumber(1)
                .frontendNumber(0).recruitNumber(8).build();

        assertThatThrownBy(() -> projectService.create(LEADER.getLoginId(), requestDto)).isInstanceOf(
                InsufficientMembersException.class);
    }

    @Test
    void 프로젝트_생성() {
        // given
        final ProjectRequestDto requestDto = ProjectRequestDto.builder().title("프로젝트 모집합니다.").backendNumber(1)
                .field("백엔드, 디자이너").designerNumber(1).frontendNumber(0).recruitNumber(2)
                .content("안녕하세요. 프로젝트 기획을 하게 되었는데, 같이 할 팀원 구합히다.,").build();

        final Post project = Post.createProject(requestDto.getTitle(), requestDto.getField(), requestDto.getTechStack(),
                requestDto.getContent(), requestDto.getRecruitNumber(), requestDto.getDesignerNumber(),
                requestDto.getFrontendNumber(), requestDto.getBackendNumber(), LEADER);

        given(memberRepository.findByLoginId(any())).willReturn(Optional.ofNullable((LEADER)));
        given(postRepository.save(any())).willReturn(project);

        // when
        final ProjectResponseDto responseDto = projectService.create(LEADER.getLoginId(), requestDto);

        // then
        조회한_응답_검증(responseDto, project);
    }

    @Test
    void 프로젝트_삭제_시_존재하지_않는_프로젝트이면_예외_발생() {
        // given
        given(postRepository.findById(any())).willThrow(PostNotFoundException.class);

        // when
        // then
        assertThatThrownBy(() -> projectService.delete(1L));
    }

    @Test
    void 프로젝트_조회_시_존재하지_않는_프로젝트이면_예외_발생() {
        // given
        given(postRepository.findById(any())).willThrow(PostNotFoundException.class);

        // when
        // then
        assertThatThrownBy(() -> projectService.findOne(any())).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void 프로젝트_조회() {
        // given
        final Post project = Post.createProject("제목", "분야", "기술스택", "내용", 3, 1, 1, 1, LEADER);

        given(postRepository.findById(any()))
                .willReturn(Optional.of(project));

        // when
        ProjectResponseDto responseDto = projectService.findOne(1L);

        // then
        조회한_응답_검증(responseDto, project);
    }

    @Test
    void 회원이_생성한_모든_프로젝트_조회_시_존재하지_않는_회원이면_예외_발생() {
        // given
        given(memberRepository.findByLoginId(any()))
                .willThrow(MemberNotFoundException.class);

        // when
        // then
        assertThatThrownBy(() -> projectService.checkMemberProjects(any()))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 회원이_생성한_모든_프로젝트_조회() {
        // given
        given(memberRepository.findByLoginId(any()))
                .willReturn(Optional.of(LEADER));

        given(postRepository.findByLeaderAndType(LEADER, PostType.PROJECT))
                .willReturn(PROJECTS);

        // when
        final List<ProjectResponseDto> responseDtos = projectService.checkMemberProjects(LEADER.getLoginId());

        // then
        assertThat(responseDtos).hasSize(2);
    }

    private void 조회한_응답_검증(final ProjectResponseDto responseDto, final Post project) {
        assertAll(
                () -> assertThat(responseDto.getTitle()).isEqualTo(project.getTitle()),
                () -> assertThat(responseDto.getContent()).isEqualTo(project.getContent()),
                () -> assertThat(responseDto.getField()).isEqualTo(project.getField()),
                () -> assertThat(responseDto.getField()).isEqualTo(project.getField()),
                () -> assertThat(responseDto.getDesignerNumber()).isEqualTo(project.getDesignerNumber()),
                () -> assertThat(responseDto.getRecruitNumber()).isEqualTo(project.getRecruitNumber()),
                () -> assertThat(responseDto.getFrontendNumber()).isEqualTo(project.getFrontendNumber()),
                () -> assertThat(responseDto.getBackendNumber()).isEqualTo(project.getBackendNumber())
        );
    }

    private static Member createMember() {
        return Member.builder().loginId("hello").password(new BCryptPasswordEncoder().encode("1234"))
                .university("홍익대학교").nickName("gi").email("akd@dbk.com").build();
    }

    private static List<Post> createProjects() {
        final Post project1 = Post.createProject("프로젝트 모집", "백엔드", "자바, 스프링", "안녕하세요. 프로젝트 팀원 모집합니다.", 2, 0, 2, 0, LEADER);
        final Post project2 = Post.createProject("프로젝트 모집", "디자이너", "피그마", "안녕하세요. 프로젝트 팀원 모집합니다.", 1, 1, 0, 0, LEADER);
        return List.of(project1, project2);
    }
}
