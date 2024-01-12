package server.teammatching.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static server.teammatching.api.steps.MemberSteps.요청을_받는_회원_가입;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import server.teammatching.dto.request.MemberRequestDto;
import server.teammatching.api.helper.InitIntegrationTest;

class MemberCreateApiTest extends InitIntegrationTest {

    @Test
    void 회원_가입() {
        // given
        final MemberRequestDto 회원_가입_요청 = MemberRequestDto.builder()
                .loginId("hello")
                .email("email@naver.com")
                .nickName("tom")
                .password("password")
                .university("홍익대학교")
                .build();

        // when
        final ExtractableResponse<Response> 회원_가입_응답 = 요청을_받는_회원_가입(회원_가입_요청);

        // then
        assertThat(회원_가입_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @ParameterizedTest
    @ValueSource(strings = {"email#email.com", "gell"})
    void 이메일이_형식에_맞지_않는_경우_회원_가입_실패(final String email) {
        // given
        final MemberRequestDto 회원_가입_요청 = MemberRequestDto.builder()
                .loginId("hello")
                .email(email)
                .nickName("tom")
                .password("password")
                .university("홍익대학교")
                .build();

        // when
        final ExtractableResponse<Response> 회원_가입_응답 = 요청을_받는_회원_가입(회원_가입_요청);

        // then
        assertThat(회원_가입_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 아이디가_중복인_경우_회원_가입_실패() {
        // given
        final MemberRequestDto 회원_가입_요청 = MemberRequestDto.builder()
                .loginId("test")
                .email("email@naver.com")
                .nickName("tom")
                .password("password")
                .university("홍익대학교")
                .build();

        // when
        final ExtractableResponse<Response> 회원_가입_응답 = 요청을_받는_회원_가입(회원_가입_요청);

        // then
        assertThat(회원_가입_응답.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    void 닉네임이_중복인_경우_회원_가입_실패() {
        // given
        final MemberRequestDto 회원_가입_요청 = MemberRequestDto.builder()
                .loginId("hello")
                .email("email@naver.com")
                .nickName("nickName")
                .password("password")
                .university("홍익대학교")
                .build();

        // when
        final ExtractableResponse<Response> 회원_가입_응답 = 요청을_받는_회원_가입(회원_가입_요청);

        // then
        assertThat(회원_가입_응답.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    void 회원_탈퇴() {
        // given
        // when
        final ExtractableResponse<Response> 회원_탈퇴_응답 = given().log().all()
                .when()
                .sessionId(기본_세션)
                .delete("/members/{id}/withdrawal", 기본_회원_아이디)
                .then()
                .log().all()
                .extract();

        // then
        assertThat(회원_탈퇴_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 로그인하지_않은_사용자가_회원_탈퇴_시_실패() {
        // given
        // when
        final ExtractableResponse<Response> 회원_탈퇴_응답 = given().log().all()
                .when()
                .delete("/members/{id}/withdrawal", 기본_회원_아이디)
                .then()
                .log().all()
                .extract();

        // then
        assertThat(회원_탈퇴_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
