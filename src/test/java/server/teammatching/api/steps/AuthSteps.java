package server.teammatching.api.steps;

import static io.restassured.RestAssured.given;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import server.teammatching.dto.request.LoginRequest;

public class AuthSteps {

    public static String 로그인(final LoginRequest 로그인_요청) {
        final ExtractableResponse<Response> 로그인_응답 = 요청을_받는_로그인(로그인_요청);
        return 로그인_응답.body().asString();
    }

    public static ExtractableResponse<Response> 요청을_받는_로그인(final LoginRequest 로그인_요청) {
        return given().log().all()
                .contentType("application/json")
                .when()
                .body(로그인_요청)
                .post("/auth/login")
                .then()
                .log().all()
                .extract();
    }

    public static String 기본_로그인() {
        final LoginRequest 로그인_요청 = new LoginRequest("test", "password");
        return 로그인(로그인_요청);
    }
}
