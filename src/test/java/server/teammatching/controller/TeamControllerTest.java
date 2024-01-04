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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
import server.teammatching.service.TeamService;

@WebMvcTest(controllers = TeamController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
class TeamControllerTest {

    private static final String TEAM_BASE_URL = "/teams";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TeamService teamService;

    private Member member;
    private TeamAndStudyCreateRequestDto requestDto;

    @BeforeEach
    void setUp() {
        member = initMember();
        requestDto = initRequestDto();
    }

    @Test
    void 팀_생성() throws Exception {
        // given
        final TeamAndStudyCreateResponseDto responseDto = TeamAndStudyCreateResponseDto.builder()
                .postId(1L)
                .title("title")
                .content("content")
                .recruitNumber(1)
                .build();

        given(teamService.create(any(), any()))
                .willReturn(responseDto);

        // when
        // then
        mockMvc.perform(post(TEAM_BASE_URL + "/new")
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andDo(print());

    }

    @Test
    void 팀_생성_시_로그인이_되어_있지_않으면_생성_실패() throws Exception {
        mockMvc.perform(post(TEAM_BASE_URL + "/new")
                        .with(csrf()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void 팀_생성_시_존재하지_않는_회원이면_생성_실패() throws Exception {
        // given
        given(teamService.create(any(), any()))
                .willThrow(MemberNotFoundException.class);

        // when
        // then
        mockMvc.perform(post(TEAM_BASE_URL + "/new")
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void 모든_팀_조회() throws Exception {
        // given
        final TeamAndStudyCreateResponseDto responseDto1 = TeamAndStudyCreateResponseDto.builder()
                .postId(1L)
                .title("title")
                .content("content")
                .recruitNumber(1)
                .build();
        final TeamAndStudyCreateResponseDto responseDto2 = TeamAndStudyCreateResponseDto.builder()
                .postId(2L)
                .title("title")
                .content("content")
                .recruitNumber(1)
                .build();

        given(teamService.checkAllTeams())
                .willReturn(List.of(responseDto1, responseDto2));

        // when
        // then
        mockMvc.perform(get(TEAM_BASE_URL)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 팀_상세_조회() throws Exception {
        // given
        final TeamAndStudyCreateResponseDto responseDto = TeamAndStudyCreateResponseDto.builder()
                .postId(1L)
                .title("title")
                .content("content")
                .recruitNumber(1)
                .build();
        given(teamService.findOne(any()))
                .willReturn(responseDto);

        // when
        // then
        mockMvc.perform(get(TEAM_BASE_URL + "/check/{id}", 1L)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 팀_상세_조회_시_팀이_존재하지_않으면_조회_실패() throws Exception {
        // given
        given(teamService.findOne(any()))
                .willThrow(MemberNotFoundException.class);

        // when
        // then
        mockMvc.perform(get(TEAM_BASE_URL + "/check/{id}", 1L)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void 회원이_생성한_팀_조회() throws Exception {
        // given
        final TeamAndStudyCreateResponseDto response1 = TeamAndStudyCreateResponseDto.builder()
                .postId(1L)
                .title("title")
                .content("content")
                .recruitNumber(1)
                .build();
        given(teamService.checkMemberTeams(any()))
                .willReturn(List.of(response1));

        // when
        // then
        mockMvc.perform(get(TEAM_BASE_URL + "/{id}", "test")
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 회원이_생성한_팀_조회_시_로그인이_되어_있지_않으면_조회_실패() throws Exception {
        mockMvc.perform(get(TEAM_BASE_URL + "/{id}", "test")
                        .with(csrf()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    void 팀_삭제() throws Exception {
        // given
        doNothing().when(teamService)
                .delete(any());

        // when
        // then
        mockMvc.perform(delete(TEAM_BASE_URL + "/{id}/delete", 1L)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("정상적으로 삭제되었습니다."))
                .andDo(print());
    }

    @Test
    void 팀_삭제_시_로그인이_되어_있지_않으면_삭제_실패() throws Exception {
        mockMvc.perform(delete(TEAM_BASE_URL + "/{id}/delete", 1L)
                        .with(csrf()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    private Member initMember() {
        return Member.builder()
                .id(1L)
                .loginId("test")
                .nickName("nick")
                .build();
    }

    private TeamAndStudyCreateRequestDto initRequestDto() {
        return TeamAndStudyCreateRequestDto.builder()
                .title("title")
                .content("content")
                .recruitNumber(1)
                .build();
    }
}
