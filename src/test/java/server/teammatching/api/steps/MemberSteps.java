package server.teammatching.api.steps;

import static io.restassured.RestAssured.given;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import server.teammatching.dto.request.MemberRequestDto;

public class MemberSteps {

    public static ExtractableResponse<Response> 요청을_받는_회원_가입(final MemberRequestDto 회원_가입_요청) {
        return given().log().all()
                .contentType("application/json")
                .when()
                .body(회원_가입_요청)
                .post("/members/new")
                .then()
                .log().all()
                .extract();
    }

    public static String 기본_회원_가입() {
        final MemberRequestDto 회원_가입_요청 = MemberRequestDto.builder()
                .loginId("test")
                .email("myEmail@email.com")
                .nickName("nickName")
                .password("password")
                .university("홍익대학교")
                .build();
        return 요청을_받는_회원_가입(회원_가입_요청).jsonPath().get("loginId");
    }
}
