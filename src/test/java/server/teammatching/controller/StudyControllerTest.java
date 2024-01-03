package server.teammatching.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;
import server.teammatching.dto.response.TeamAndStudyCreateResponseDto;
import server.teammatching.entity.Member;
import server.teammatching.exception.MemberNotFoundException;
import server.teammatching.exception.PostNotFoundException;
import server.teammatching.service.StudyService;

@WebMvcTest(value = StudyController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
class StudyControllerTest {

    private static final String STUDY_BASIC_URL = "/studies";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudyService studyService;

    @Test
    void 스터디_생성() throws Exception {
        // given
        final TeamAndStudyCreateResponseDto responseDto = TeamAndStudyCreateResponseDto.builder()
                .postId(1L)
                .title("스터디 제목")
                .recruitNumber(5)
                .content("스터디 내용")
                .nickName("스터디장 닉네임")
                .build();

        given(studyService.create(any(), any()))
                .willReturn(responseDto);

        // when
        // then
        final Member leader = Member.builder()
                .id(1L)
                .nickName("스터디장 닉네임")
                .build();

        final TeamAndStudyCreateRequestDto requestDto = TeamAndStudyCreateRequestDto.builder()
                .title(responseDto.getTitle())
                .recruitNumber(responseDto.getRecruitNumber())
                .content(responseDto.getContent())
                .build();

        mockMvc.perform(post(STUDY_BASIC_URL + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(user(new PrincipalDetails(leader)))
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void 스터디_생성_시_로그인이_안_되어_있으면_예외_발생() throws Exception {
        mockMvc.perform(post(STUDY_BASIC_URL + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void 스터디_생성_시_회원이_존재하지_않으면_예외_발생() throws Exception {
        // given
        given(studyService.create(any(), any()))
                .willThrow(MemberNotFoundException.class);

        // when
        // then
        final Member leader = Member.builder()
                .id(1L)
                .nickName("스터디장 닉네임")
                .build();

        final TeamAndStudyCreateRequestDto requestDto = TeamAndStudyCreateRequestDto.builder()
                .title("스터디 제목")
                .recruitNumber(5)
                .content("스터디 내용")
                .build();

        mockMvc.perform(post(STUDY_BASIC_URL + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(user(new PrincipalDetails(leader)))
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void 모든_스터디_조회() throws Exception {
        // given
        final List<TeamAndStudyCreateResponseDto> result = List.of(
                TeamAndStudyCreateResponseDto.builder()
                        .postId(1L)
                        .title("스터디 제목")
                        .recruitNumber(5)
                        .content("스터디 내용")
                        .nickName("스터디장 닉네임1")
                        .build(),
                TeamAndStudyCreateResponseDto.builder()
                        .postId(2L)
                        .title("스터디 제목")
                        .recruitNumber(5)
                        .content("스터디 내용")
                        .nickName("스터디장 닉네임2")
                        .build(),
                TeamAndStudyCreateResponseDto.builder()
                        .postId(3L)
                        .title("스터디 제목")
                        .recruitNumber(5)
                        .content("스터디 내용")
                        .nickName("스터디장 닉네임3")
                        .build());

        given(studyService.checkAllStudies())
                .willReturn(result);

        // when
        // then
        final Member leader = Member.builder()
                .id(1L)
                .build();

        mockMvc.perform(get(STUDY_BASIC_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(user(new PrincipalDetails(leader))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 스터디_상세_조회() throws Exception {
        // given
        final TeamAndStudyCreateResponseDto responseDto = TeamAndStudyCreateResponseDto.builder()
                .postId(1L)
                .title("스터디 제목")
                .recruitNumber(5)
                .content("스터디 내용")
                .nickName("스터디장 닉네임")
                .build();

        given(studyService.findOne(any()))
                .willReturn(responseDto);

        // when
        // then
        final Member leader = Member.builder()
                .id(1L)
                .build();

        mockMvc.perform(get(STUDY_BASIC_URL + "/check/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(new PrincipalDetails(leader)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 스터디_상세_조회_시_스터디가_존재하지_않으면_예외_발생() throws Exception {
        // given
        given(studyService.findOne(any()))
                .willThrow(PostNotFoundException.class);

        // when
        // then
        final Member leader = Member.builder()
                .id(1L)
                .build();

        mockMvc.perform(get(STUDY_BASIC_URL + "/check/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(new PrincipalDetails(leader)))
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void 회원이_생성한_스터디_조회() throws Exception {
        // given
        final List<TeamAndStudyCreateResponseDto> result = List.of(
                TeamAndStudyCreateResponseDto.builder()
                        .postId(1L)
                        .title("스터디 제목")
                        .recruitNumber(2)
                        .content("스터디 내용")
                        .nickName("스터디장 닉네임1")
                        .build(),
                TeamAndStudyCreateResponseDto.builder()
                        .postId(2L)
                        .title("스터디 제목")
                        .recruitNumber(1)
                        .content("스터디 내용")
                        .nickName("스터디장 닉네임1")
                        .build());

        given(studyService.checkMemberStudies(any()))
                .willReturn(result);

        // when
        // then
        final Member leader = Member.builder()
                .id(1L)
                .loginId("test")
                .build();

        mockMvc.perform(get(STUDY_BASIC_URL + "/{id}", leader.getLoginId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(new PrincipalDetails(leader)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 회원이_생성한_스터디_조회_시_로그인이_되어_있지_않으면_예외_발생() throws Exception {
        mockMvc.perform(get(STUDY_BASIC_URL + "/{id}", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void 스터디_삭제() throws Exception {
        // given
        doNothing().when(studyService)
                .delete(any());

        // when
        // then
        final Member leader = Member.builder()
                .id(1L)
                .build();

        mockMvc.perform(delete(STUDY_BASIC_URL + "/{id}/delete", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(new PrincipalDetails(leader)))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 스터디_삭제_시_로그인이_되어_있지_않으면_예외_발생() throws Exception {
        mockMvc.perform(delete(STUDY_BASIC_URL + "/{id}/delete", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}
