package server.teammatching.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import server.teammatching.api.helper.InitIntegrationTest;

public class MemberReadApiTest extends InitIntegrationTest {


    @Test
    void 마이페이지_조회() {
        // given
        // when
        final ExtractableResponse<Response> 마이_페이지_조회_응답 = given().log().all()
                .when()
                .sessionId(기본_세션)
                .get("/members/{id}", 기본_회원_아이디)
                .then()
                .log().all()
                .extract();

        // then
        assertThat(마이_페이지_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 마이_페이지_조회_시_로그인이_되어_있지_않으면_조회_실패() {
        // given
        // when
        final ExtractableResponse<Response> 마이_페이지_조회_응답 = given().log().all()
                .when()
                .get("/members/{id}", 기본_회원_아이디)
                .then()
                .log().all()
                .extract();;

        // then
        assertThat(마이_페이지_조회_응답.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
