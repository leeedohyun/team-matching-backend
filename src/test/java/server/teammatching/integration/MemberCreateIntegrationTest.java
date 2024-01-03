package server.teammatching.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import server.teammatching.dto.request.MemberRequestDto;

class MemberCreateIntegrationTest extends IntegrationTest {

    @Test
    void 회원_가입() {
        // given
        final MemberRequestDto 회원_가입_요청 = MemberRequestDto.builder()
                .loginId("test")
                .email("email@naver.com")
                .nickName("nickName")
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
                .loginId("test")
                .email(email)
                .nickName("nickName")
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
                .nickName("nickName")
                .password("password")
                .university("홍익대학교")
                .build();
        요청을_받는_회원_가입(회원_가입_요청);

        // when
        final ExtractableResponse<Response> 회원_가입_응답 = 요청을_받는_회원_가입(회원_가입_요청);

        // then
        assertThat(회원_가입_응답.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    private ExtractableResponse<Response> 요청을_받는_회원_가입(final MemberRequestDto 회원_가입_요청) {
        return given().log().all()
                .contentType("application/json")
                .when()
                .body(회원_가입_요청)
                .post("/members/new")
                .then()
                .log().all()
                .extract();
    }
}
