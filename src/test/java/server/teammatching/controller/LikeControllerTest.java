package server.teammatching.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import server.teammatching.auth.PrincipalDetails;
import server.teammatching.config.SecurityConfig;
import server.teammatching.dto.response.LikeResponseDto;
import server.teammatching.entity.Member;
import server.teammatching.exception.LikeNotFoundException;
import server.teammatching.exception.MemberNotFoundException;
import server.teammatching.exception.PostNotFoundException;
import server.teammatching.exception.UnauthorizedException;
import server.teammatching.service.LikeService;

@WebMvcTest(value = LikeController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
class LikeControllerTest {

    private static final String LIKES_BASE_URL = "/likes";
    private static final String POST_ID = "postId";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    private Member member;

    @BeforeEach
    void setUp() {
        member = initMember();
    }

    @Test
    void 관심_등록() throws Exception {
        // given
        final LikeResponseDto response = initResponse();
        given(likeService.generateLike(any(), any()))
                .willReturn(response);

        // when
        // then
        performRequestAndExpect(post(LIKES_BASE_URL), status().isOk());
    }

    @Test
    void 관심_등록_시_로그인이_되어_있지_않으면_둥록_실패() throws Exception {
        비인가_테스트(post(LIKES_BASE_URL));
    }

    @Test
    void 관심_등록_시_회원이_존재하지_않으면_등록_실패() throws Exception {
        // given
        given(likeService.generateLike(any(), any()))
                .willThrow(MemberNotFoundException.class);

        // when
        // then
        performRequestAndExpect(post(LIKES_BASE_URL), status().isNotFound());
    }

    @Test
    void 관심_등록_시_post가_존재하지_않으면_등록_실패() throws Exception {
        // given
        given(likeService.generateLike(any(), any()))
                .willThrow(PostNotFoundException.class);

        // when
        // then
        performRequestAndExpect(post(LIKES_BASE_URL), status().isNotFound());
    }

    @Test
    void 관심_목록_조회() throws Exception {
        // given
        final LikeResponseDto response1 = initResponse();
        final LikeResponseDto response2 = LikeResponseDto.builder()
                .postId(2L)
                .memberId(1L)
                .postTitle("제목")
                .build();
        given(likeService.checkLikes(any()))
                .willReturn(List.of(response1, response2));

        // when
        // then
        performRequestAndExpect(get(LIKES_BASE_URL), status().isOk());
    }

    @Test
    void 관심_목록_조회_시_로그인이_되어_있지_않으면_조회_실패() throws Exception {
        비인가_테스트(get(LIKES_BASE_URL));
    }

    @Test
    void 관심_목록_조회_시_회원이_존재하지_않으면_조회_실패() throws Exception {
        // given
        given(likeService.checkLikes(any()))
                .willThrow(MemberNotFoundException.class);

        // when
        // then
        performRequestAndExpect(get(LIKES_BASE_URL), status().isNotFound());
    }

    @Test
    void 관심_취소() throws Exception {
        // given
        final LikeResponseDto responseDto = initResponse();
        given(likeService.cancelLike(any(), any()))
                .willReturn(responseDto);

        // when
        // then
        performRequestAndExpect(delete(LIKES_BASE_URL), status().isOk());
    }

    @Test
    void 관심_취소_시_로그인이_되어_있지_않으면_취소_실패() throws Exception {
        // given
        given(likeService.cancelLike(any(), any()))
                .willThrow(UnauthorizedException.class);

        // when
        // then
        비인가_테스트(delete(LIKES_BASE_URL));
    }

    @Test
    void 관심_취소_시_회원이_존재하지_않으면_취소_실패() throws Exception {
        // given
        given(likeService.cancelLike(any(), any()))
                .willThrow(MemberNotFoundException.class);

        // when
        // then
        performRequestAndExpect(delete(LIKES_BASE_URL), status().isNotFound());
    }

    @Test
    void 관심_취소_시_post가_존재하지_않으면_취소_실패() throws Exception {
        // given
        given(likeService.cancelLike(any(), any()))
                .willThrow(PostNotFoundException.class);

        // when
        // then
        performRequestAndExpect(delete(LIKES_BASE_URL), status().isNotFound());
    }

    @Test
    void 관심_취소_시_관심을_등록하지_않았으면_취소_실패() throws Exception {
        // given
        given(likeService.cancelLike(any(), any()))
                .willThrow(LikeNotFoundException.class);

        // when
        // then
        performRequestAndExpect(delete(LIKES_BASE_URL), status().isNotFound());
    }

    private LikeResponseDto initResponse() {
        return LikeResponseDto.builder()
                .postId(1L)
                .memberId(1L)
                .postTitle("제목")
                .build();
    }

    private void 비인가_테스트(final MockHttpServletRequestBuilder requestBuilder) throws Exception {
        // given
        given(likeService.generateLike(any(), any()))
                .willThrow(UnauthorizedException.class);

        // when
        // then
        mockMvc.perform(requestBuilder
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(POST_ID, String.valueOf(1L)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    private void performRequestAndExpect(final MockHttpServletRequestBuilder requestBuilder,
                                         final ResultMatcher resultMatcher) throws Exception {
        mockMvc.perform(requestBuilder
                        .with(csrf())
                        .with(user(new PrincipalDetails(member)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(POST_ID, String.valueOf(1L)))
                .andExpect(resultMatcher)
                .andDo(print());
    }

    private Member initMember() {
        return Member.builder()
                .id(1L)
                .build();
    }
}
