package server.teammatching.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import server.teammatching.auth.PrincipalDetails;
import server.teammatching.config.SecurityConfig;
import server.teammatching.dto.request.ProjectRequestDto;
import server.teammatching.dto.response.ProjectResponseDto;
import server.teammatching.entity.Member;
import server.teammatching.entity.PostType;
import server.teammatching.exception.InsufficientMembersException;
import server.teammatching.exception.MemberNotFoundException;
import server.teammatching.exception.PostNotFoundException;
import server.teammatching.service.ProjectService;

@WebMvcTest(controllers = ProjectController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
)
class ProjectControllerTest {

    private static final String PROJECT_BASE_URL = "/projects";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;

    @Test
    void 프로젝트_생성_시_로그인이_되어있지_않으면_예외_발생() throws Exception {
        performAuthorization(PROJECT_BASE_URL + "/new");
    }

    @Test
    void 프로젝트_생성_시_리더가_존재하지_않으면_예외_발생() throws Exception {
        // given
        given(projectService.create(any(), any()))
                .willThrow(MemberNotFoundException.class);

        // when
        // then
        final Member member = Member.builder()
                .id(1L)
                .loginId("myId")
                .email("email@hello.com")
                .nickName("nick")
                .university("홍익대학교")
                .build();

        final ProjectRequestDto requestDto = ProjectRequestDto.builder()
                .title("프로젝트 모집합니다.")
                .field("백엔드")
                .recruitNumber(1)
                .designerNumber(0)
                .frontendNumber(0)
                .backendNumber(1)
                .content("함께 재미있게 프로젝트 진행해봐요!")
                .build();

        mockMvc.perform(post(PROJECT_BASE_URL + "/new")
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void 프로젝트_생성_시_모집_인원이_맞지_않으면_예외_발생() throws Exception {
        // given
        given(projectService.create(any(), any()))
                .willThrow(InsufficientMembersException.class);

        // when
        // then
        final Member member = Member.builder()
                .id(1L)
                .loginId("myId")
                .email("email@hello.com")
                .nickName("nick")
                .university("홍익대학교")
                .build();

        final ProjectRequestDto requestDto = ProjectRequestDto.builder()
                .title("프로젝트 모집합니다.")
                .field("백엔드")
                .recruitNumber(2)
                .designerNumber(1)
                .frontendNumber(1)
                .backendNumber(1)
                .content("함께 재미있게 프로젝트 진행해봐요!")
                .build();

        mockMvc.perform(post(PROJECT_BASE_URL + "/new")
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 프로젝트_생성() throws Exception {
        // given
        final ProjectRequestDto requestDto = ProjectRequestDto.builder()
                .title("프로젝트 모집합니다.")
                .field("백엔드")
                .recruitNumber(1)
                .designerNumber(0)
                .frontendNumber(0)
                .backendNumber(1)
                .content("함께 재미있게 프로젝트 진행해봐요!")
                .build();

        final ProjectResponseDto responseDto = ProjectResponseDto.builder()
                .postId(1L)
                .title(requestDto.getTitle())
                .field(requestDto.getField())
                .type(PostType.PROJECT)
                .recruitNumber(requestDto.getRecruitNumber())
                .frontendNumber(requestDto.getFrontendNumber())
                .designerNumber(requestDto.getDesignerNumber())
                .backendNumber(requestDto.getBackendNumber())
                .nickName("nick")
                .content(requestDto.getContent())
                .build();

        given(projectService.create(any(), any()))
                .willReturn(responseDto);

        // when
        // then
        final Member member = Member.builder()
                .id(1L)
                .loginId("myId")
                .nickName("nick")
                .build();

        mockMvc.perform(post(PROJECT_BASE_URL + "/new")
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void 프로젝트_정보_수정_시_로그인이_되지_않으면_예외_발생() throws Exception {
        performAuthorization(PROJECT_BASE_URL + "/{id}", 1L);
    }

    @Test
    void 프로젝트_정보_수정_시_모집_인원이_맞지_않으면_예외_발생() throws Exception {
        // given
        given(projectService.update(any(), any()))
                .willThrow(InsufficientMembersException.class);

        // when
        // then
        final ProjectRequestDto requestDto = ProjectRequestDto.builder()
                .build();

        final Member member = Member.builder()
                .id(1L)
                .loginId("hello")
                .build();

        mockMvc.perform(patch(PROJECT_BASE_URL + "/{id}", 1L)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 프로젝트_제목_수정() throws Exception {
        // given
        final ProjectResponseDto responseDto = ProjectResponseDto.builder()
                .title("제목입니다")
                .type(PostType.PROJECT)
                .build();

        given(projectService.update(any(), any()))
                .willReturn(responseDto);

        // when
        // then
        final ProjectRequestDto requestDto = ProjectRequestDto.builder()
                .title("제목입니다")
                .build();

        final Member member = Member.builder()
                .id(1L)
                .loginId("hello")
                .build();

        mockMvc.perform(patch(PROJECT_BASE_URL + "/{id}", 1L)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 모집_분야_변경() throws Exception {
        // given
        final ProjectResponseDto responseDto = ProjectResponseDto.builder()
                .field("모집분야")
                .type(PostType.PROJECT)
                .build();

        given(projectService.update(any(), any()))
                .willReturn(responseDto);

        // when
        // then
        final Member member = Member.builder()
                .id(1L)
                .loginId("hello")
                .build();

        final ProjectRequestDto requestDto = ProjectRequestDto.builder()
                .field("모집분야")
                .build();

        mockMvc.perform(patch(PROJECT_BASE_URL + "/{id}", 1L)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 인원_수_변경() throws Exception {
        // given
        final ProjectResponseDto responseDto = ProjectResponseDto.builder()
                .recruitNumber(1)
                .type(PostType.PROJECT)
                .build();

        given(projectService.update(any(), any()))
                .willReturn(responseDto);

        // when
        // then
        final ProjectRequestDto requestDto = ProjectRequestDto.builder()
                .recruitNumber(1)
                .build();

        final Member member = Member.builder()
                .id(1L)
                .loginId("hello")
                .build();

        mockMvc.perform(patch(PROJECT_BASE_URL + "/{id}", 1L)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 본문_변경() throws Exception {
        // given
        final ProjectResponseDto responseDto = ProjectResponseDto.builder()
                .content("본문입니다")
                .type(PostType.PROJECT)
                .build();

        given(projectService.update(any(), any()))
                .willReturn(responseDto);

        // when
        // then
        final ProjectRequestDto requestDto = ProjectRequestDto.builder()
                .content("본문입니다")
                .build();

        final Member member = Member.builder()
                .id(1L)
                .loginId("hello")
                .build();

        mockMvc.perform(patch(PROJECT_BASE_URL + "/{id}", 1L)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 프로젝트_상세_조회_시_프로젝트가_존재하지_않는_경우_예외_발생() throws Exception {
        // given
        given(projectService.findOne(any()))
                .willThrow(PostNotFoundException.class);

        // when
        // then
        final Member member = Member.builder()
                .id(1L)
                .loginId("hello")
                .nickName("my")
                .build();

        mockMvc.perform(get(PROJECT_BASE_URL + "/check/{postId}", 1L)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member))))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void 프로젝트_상세_조회() throws Exception {
        // given


        final ProjectResponseDto responseDto = ProjectResponseDto.builder()
                .postId(1L)
                .title("프로젝트 모집합니다.")
                .field("디자이너")
                .type(PostType.PROJECT)
                .techStack("디자인툴")
                .recruitNumber(1)
                .frontendNumber(0)
                .designerNumber(1)
                .backendNumber(0)
                .nickName("my")
                .content("열심히 함께 해보아요~")
                .build();

        given(projectService.findOne(any()))
                .willReturn(responseDto);

        // when
        // then
        final Member member = Member.builder()
                .id(1L)
                .loginId("hello")
                .nickName("my")
                .build();

        mockMvc.perform(get(PROJECT_BASE_URL + "/check/{postId}", 1L)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 프로젝트가_없는_경우_모든_프로젝트_조회() throws Exception {
        // given
        given(projectService.checkAllProjects())
                .willReturn(Collections.emptyList());

        // when
        // then
        final Member member = Member.builder()
                .id(1L)
                .loginId("hello")
                .nickName("my")
                .build();

        mockMvc.perform(get(PROJECT_BASE_URL)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 회원이_생성한_프로젝트_조회_시_로그인이_되지_않은_상태이면_예외_발생() throws Exception {
        performAuthorization(PROJECT_BASE_URL + "{id}", "memberId");
    }

    @Test
    void 회원이_생성한_프로젝트_조회_시_회원이_존재하지_않으면_예외_발생() throws Exception {
        // given
        given(projectService.checkMemberProjects(any()))
                .willThrow(MemberNotFoundException.class);

        // when
        // then
        final Member member = Member.builder()
                .id(1L)
                .loginId("hello")
                .nickName("my")
                .build();

        mockMvc.perform(get(PROJECT_BASE_URL + "/{id}", "memberId")
                        .with(csrf())
                        .with(user(new PrincipalDetails(member))))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void 회원이_생성한_프로젝트_조회() throws Exception {
        // given
        final String nickName = "name";

        final ProjectResponseDto responseDto1 = ProjectResponseDto.builder()
                .postId(1L)
                .nickName(nickName)
                .build();

        final ProjectResponseDto responseDto2 = ProjectResponseDto.builder()
                .postId(2L)
                .nickName(nickName)
                .build();

        given(projectService.checkMemberProjects(any()))
                .willReturn(List.of(responseDto1, responseDto2));

        // when
        // then
        final Member member = Member.builder()
                .id(1L)
                .loginId("hello")
                .nickName("my")
                .build();

        mockMvc.perform(get(PROJECT_BASE_URL + "/{id}", "memberId")
                        .with(csrf())
                        .with(user(new PrincipalDetails(member))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 프로젝트_삭제_시_로그인이_되어_있지_않으면_예외_발생() throws Exception {
        performAuthorization(PROJECT_BASE_URL + "/{postId}/delete", 1L);
    }

    @Test
    void 프로젝트_삭제_시_프로젝트가_존재하지_않으면_예외_발생() throws Exception {
        // given
        doThrow(new PostNotFoundException())
                .when(projectService)
                .delete(any());

        // when
        // then
        final Member member = Member.builder()
                .id(1L)
                .loginId("hello")
                .nickName("my")
                .build();

        mockMvc.perform(delete(PROJECT_BASE_URL + "/{id}/delete", 1L)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member))))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void 프로젝트_삭제() throws Exception {
        // given
        doNothing()
                .when(projectService)
                .delete(any());

        // when
        // then
        final Member member = Member.builder()
                .id(1L)
                .loginId("hello")
                .nickName("my")
                .build();

        mockMvc.perform(delete(PROJECT_BASE_URL + "/{id}/delete", 1L)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    private void performAuthorization(String urlTemplate, Object... uriVariables) throws Exception {
        mockMvc.perform(delete(urlTemplate, uriVariables)
                        .with(csrf()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}
