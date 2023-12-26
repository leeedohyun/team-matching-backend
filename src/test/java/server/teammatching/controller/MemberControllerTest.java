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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import server.teammatching.auth.PrincipalDetails;
import server.teammatching.config.SecurityConfig;
import server.teammatching.dto.request.MemberRequestDto;
import server.teammatching.dto.request.MemberUpdateRequestDto;
import server.teammatching.dto.response.MemberResponseDto;
import server.teammatching.dto.response.MemberUpdateResponseDto;
import server.teammatching.entity.Member;
import server.teammatching.exception.DuplicateResourceException;
import server.teammatching.exception.MemberNotFoundException;
import server.teammatching.exception.UnauthorizedException;
import server.teammatching.service.MemberService;

@WebMvcTest(controllers = MemberController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("회원가입")
    @WithMockUser
    void join() throws Exception {
        // given
        final MemberRequestDto requestDto = MemberRequestDto.builder()
                .loginId("test")
                .password("1234")
                .nickName("hello")
                .email("abc@naver.com")
                .university("홍익대학교")
                .build();

        final MemberResponseDto responseDto = MemberResponseDto.builder()
                .memberId(1L)
                .loginId(requestDto.getLoginId())
                .email(requestDto.getEmail())
                .nickName(requestDto.getNickName())
                .university(requestDto.getUniversity())
                .build();

        given(memberService.join(any()))
                .willReturn(responseDto);

        // when
        // then
        mockMvc.perform(post("/members/new")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 시 아이디가 빈 값인 경우 예외 발생")
    @WithMockUser
    void joinFailByLoginId() throws Exception {
        // given
        final MemberRequestDto requestDto = MemberRequestDto.builder()
                .loginId("")
                .password("1234")
                .nickName("hello")
                .email("abc@naver.com")
                .university("홍익대학교")
                .build();

        // when
        // then
        mockMvc.perform(post("/members/new")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "ab%.co", "gkdke", "eog@.com"})
    @DisplayName("회원가입 시 이메일의 형식이 맞지 않은 경우 예외 발생")
    @WithMockUser
    void joinFailByLoginId(final String email) throws Exception {
        // given
        final MemberRequestDto requestDto = MemberRequestDto.builder()
                .loginId("test")
                .password("1234")
                .nickName("hello")
                .email(email)
                .university("홍익대학교")
                .build();

        // when
        // then
        mockMvc.perform(post("/members/new")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 시 닉네임이 빈 값인 경우 예외 발생")
    @WithMockUser
    void joinFailByNickName() throws Exception {
        // given
        final MemberRequestDto requestDto = MemberRequestDto.builder()
                .loginId("test")
                .password("1234")
                .nickName("")
                .email("abc@naver.com")
                .university("홍익대학교")
                .build();

        // when
        // then
        mockMvc.perform(post("/members/new")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 시 대학교가 빈 값인 경우 예외 발생")
    @WithMockUser
    void joinFailByUniversity() throws Exception {
        // given
        final MemberRequestDto requestDto = MemberRequestDto.builder()
                .loginId("test")
                .password("1234")
                .nickName("ji")
                .email("abc@naver.com")
                .university("")
                .build();

        // when
        // then
        mockMvc.perform(post("/members/new")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 시 중복된 아이디인 경우 예외 발생")
    @WithMockUser
    void joinFailByDuplicateId() throws Exception {
        // given
        final MemberRequestDto requestDto = MemberRequestDto.builder()
                .loginId("test")
                .password("1234")
                .nickName("hello")
                .email("abc@naver.com")
                .university("홍익대학교")
                .build();

        doThrow(new DuplicateResourceException("id", requestDto.getLoginId()))
                .when(memberService)
                .join(any());

        // when
        // then
        mockMvc.perform(post("/members/new")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 시 중복된 닉네임인 경우 예외 발생")
    @WithMockUser
    void joinFailByDuplicateNickName() throws Exception {
        // given
        final MemberRequestDto requestDto = MemberRequestDto.builder()
                .loginId("test")
                .password("1234")
                .nickName("hello")
                .email("abc@naver.com")
                .university("홍익대학교")
                .build();

        doThrow(new DuplicateResourceException("id", requestDto.getLoginId()))
                .when(memberService)
                .join(any());

        // when
        // then
        mockMvc.perform(post("/members/new")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @DisplayName("회원이 회원 정보 수정을 할 때 로그인이 안 된 경우 예외 발생")
    void updateFailByUnauthorized() throws Exception {
        // given
        doThrow(new UnauthorizedException("Invalid member authentication"))
                .when(memberService)
                .update(any(), any());

        // when
        // then
        final String id = "hello";
        mockMvc.perform(patch("/members/{id}/edit", id)
                        .with(csrf()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인한 회원이 회원 정보 수정을 할 때 존재하지 않는 회원인 경우 예외 발생")
    void updateFail() throws Exception {
        // given
        final Member member = Member.builder()
                .nickName("jack")
                .university("홍익대학교")
                .email("ab@nate.com")
                .build();

        doThrow(new MemberNotFoundException())
                .when(memberService)
                .update(any(), any());

        // when
        // then
        final String id = "hello";
        final MemberUpdateRequestDto updateRequestDto = MemberUpdateRequestDto.builder()
                .updatedEmail("aer@hanmail.net")
                .updatedNickName("tom")
                .updatedUniversity("hongik")
                .build();

        mockMvc.perform(patch("/members/{id}/edit", id)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .content(objectMapper.writeValueAsString(updateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("닉네임 변경")
    void updateByNickName() throws Exception {
        // given
        final Member member = Member.builder()
                .nickName("jack")
                .build();

        final MemberUpdateResponseDto responseDto = MemberUpdateResponseDto.builder()
                .updatedNickName("tom")
                .build();

        given(memberService.update(any(), any()))
                .willReturn(responseDto);

        // when
        // then
        final String id = "hello";
        final MemberUpdateRequestDto updateRequestDto = MemberUpdateRequestDto.builder()
                .updatedNickName("tom")
                .build();

        mockMvc.perform(patch("/members/{id}/edit", id)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .content(objectMapper.writeValueAsString(updateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)))
                .andDo(print());
    }

    @Test
    @DisplayName("대힉교 변경")
    void updateByUniversity() throws Exception {
        // given
        final Member member = Member.builder()
                .university("홍익대학교")
                .build();

        final MemberUpdateResponseDto responseDto = MemberUpdateResponseDto.builder()
                .updatedUniversity("hongik")
                .build();

        given(memberService.update(any(), any()))
                .willReturn(responseDto);

        // when
        // then
        final String id = "hello";
        final MemberUpdateRequestDto updateRequestDto = MemberUpdateRequestDto.builder()
                .updatedUniversity("hongik")
                .build();

        mockMvc.perform(patch("/members/{id}/edit", id)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .content(objectMapper.writeValueAsString(updateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)))
                .andDo(print());
    }

    @Test
    @DisplayName("이메일 변경")
    void updateByEmail() throws Exception {
        // given
        final Member member = Member.builder()
                .email("ab@nate.com")
                .build();

        final MemberUpdateResponseDto responseDto = MemberUpdateResponseDto.builder()
                .updatedEmail("aer@hanmail.net")
                .build();

        given(memberService.update(any(), any()))
                .willReturn(responseDto);

        // when
        // then
        final String id = "hello";
        final MemberUpdateRequestDto updateRequestDto = MemberUpdateRequestDto.builder()
                .updatedEmail("aer@hanmail.net")
                .build();

        mockMvc.perform(patch("/members/{id}/edit", id)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .content(objectMapper.writeValueAsString(updateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)))
                .andDo(print());
    }

    @Test
    @DisplayName("회원이 회원 탈퇴를 할 때 로그인이 안 된 경우 예외 발생")
    void withdrawFailByUnauthorized() throws Exception {
        // given
        doThrow(new UnauthorizedException("Invalid member authentication"))
                .when(memberService)
                .delete(any());

        // when
        // then
        final String id = "hello";
        mockMvc.perform(delete("/members/{id}/withdrawal", id)
                        .with(csrf()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인한 회원이 회원 탈퇴를 할 때 존재하지 않는 회원인 경우 예외 발생")
    void withdrawFailByMemberNotFound() throws Exception {
        // given
        final Member member = Member.builder()
                .id(1L)
                .loginId("hi")
                .password("1234")
                .nickName("jack")
                .university("홍익대학교")
                .email("ab@nate.com")
                .build();

        doThrow(new MemberNotFoundException())
                .when(memberService)
                .delete(any());

        // when
        // then
        final String id = "hi";
        mockMvc.perform(delete("/members/{id}/withdrawal", id)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member))))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 탈퇴")
    void withdraw() throws Exception {
        // given
        final Member member = Member.builder()
                .id(1L)
                .loginId("hi")
                .password("1234")
                .nickName("jack")
                .university("홍익대학교")
                .email("ab@nate.com")
                .build();

        doNothing().when(memberService).delete(any());

        // when
        // then
        final String id = "hi";
        mockMvc.perform(delete("/members/{id}/withdrawal", id)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member))))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("마이 페이지 조회 시 로그인이 안 되어 있으면 예외 발생")
    void failMemberInfoByUnauthorized() throws Exception {
        // given
        given(memberService.findOne(any()))
                .willThrow(new UnauthorizedException("Invalid member authentication"));

        // when
        // then
        final String id = "hello";
        mockMvc.perform(get("/members/{id}", id)
                        .with(csrf()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("마이 페이지 조회 시 존재하지 않는 회원이면 예외 발생")
    void failMemberInfoByMemberNotFound() throws Exception {
        // given
        final Member member = Member.builder()
                .id(1L)
                .loginId("hi")
                .password("1234")
                .nickName("jack")
                .university("홍익대학교")
                .email("ab@nate.com")
                .build();

        given(memberService.findOne(any()))
                .willThrow(new MemberNotFoundException());

        // when
        // then
        final String id = "hi";
        mockMvc.perform(get("/members/{id}", id)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member))))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("마이 페이지 조회")
    void getMemberInfo() throws Exception {
        // given
        final Member member = Member.builder()
                .id(1L)
                .loginId("hi")
                .password("1234")
                .nickName("jack")
                .university("홍익대학교")
                .email("ab@nate.com")
                .build();

        final MemberResponseDto responseDto = MemberResponseDto.builder()
                .memberId(member.getId())
                .loginId(member.getLoginId())
                .university(member.getUniversity())
                .nickName(member.getNickName())
                .email(member.getEmail())
                .build();

        given(memberService.findOne(any()))
                .willReturn(responseDto);

        // when
        // then
        final String id = "hi";
        mockMvc.perform(get("/members/{id}", id)
                        .with(csrf())
                        .with(user(new PrincipalDetails(member))))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)))
                .andDo(print());
    }
}
