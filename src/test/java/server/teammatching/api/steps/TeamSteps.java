package server.teammatching.api.steps;

import static io.restassured.RestAssured.given;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;

public class TeamSteps {

    private static final String TEAM_BASE_URL = "/teams";
    private static final Long TEAM_BASE_ID = 2L;

    public static ExtractableResponse<Response> 요청을_받는_팀_생성(final TeamAndStudyCreateRequestDto 팀_생성_요청,
                                                              final String 세션) {
        return given().log().all()
                .contentType("application/json")
                .sessionId(세션)
                .body(팀_생성_요청)
                .when()
                .post(TEAM_BASE_URL + "/new")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 응답을_반환하는_팀_삭제(final String 세션) {
        return given().log().all()
                .sessionId(세션)
                .when()
                .delete(TEAM_BASE_URL + "/{id}/delete", TEAM_BASE_ID)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 팀_상세_조회_응답() {
        return given().log().all()
                .when()
                .get(TEAM_BASE_URL + "/check/{id}", TEAM_BASE_ID)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 응답을_반환하는_회원이_생성한_팀_조회(final String 세션, final String 회원_아이디) {
        return given().log().all()
                .sessionId(세션)
                .when()
                .get(TEAM_BASE_URL + "/{id}", 회원_아이디)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 응답을_반환하는_모든_팀_조회() {
        return given().log().all()
                .when()
                .get(TEAM_BASE_URL)
                .then()
                .log().all()
                .extract();
    }
}
