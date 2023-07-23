package server.teammatching.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import server.teammatching.service.MemberService;
import server.teammatching.dto.request.MemberRequestDto;
import server.teammatching.dto.response.MemberResponseDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired MockMvc mvc;
    @MockBean MemberService memberService;
    @InjectMocks MemberController memberController;

    @BeforeEach
    public void setup() {
        memberController = new MemberController(memberService);
        mvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    @DisplayName("회원가입 API 테스트")
    public void 회원가입_API_테스트() throws Exception {

        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .loginId("loginId")
                .email("qqqq@nnn.com")
                .nickName("member1")
                .password("1234")
                .university("홍익대학교")
                .build();

        MemberResponseDto responseDto = MemberResponseDto.builder()
                .memberId(1L)
                .loginId("loginId")
                .email("qqqq@nnn.com")
                .nickName("member1")
                .university("홍익대학교")
                .build();

        when(memberService.join(any(MemberRequestDto.class))).thenReturn(responseDto);

        Gson gson = new Gson();
        String requestJson = gson.toJson(memberRequestDto);

        mvc.perform(MockMvcRequestBuilders
                        .post("/members/new")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}