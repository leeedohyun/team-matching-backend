package server.teammatching.api.steps;

import static io.restassured.RestAssured.given;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import server.teammatching.api.helper.InitApiTest;
import server.teammatching.dto.request.ProjectRequestDto;

public class ProjectSteps extends InitApiTest {

    private static final String PROJECT_BASE_URL = "/projects";
    private static final String CONTENT_TYPE = "application/json";

    public static ExtractableResponse<Response> 요청을_받는_포르젝트_생성(final String 세션, final ProjectRequestDto 프로젝트_생성_요청) {
        return given().log().all()
                .contentType(CONTENT_TYPE)
                .sessionId(세션)
                .when()
                .body(프로젝트_생성_요청)
                .post(PROJECT_BASE_URL + "/new")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 회원이_생성한_모든_프로젝트_조회(final String 세션) {
        return given().log().all()
                .contentType(CONTENT_TYPE)
                .sessionId(세션)
                .when()
                .get(PROJECT_BASE_URL + "/{id}", 기본_회원_아이디)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 프로젝트_상세_조회(final String 세션, final Long 프로젝트_아이디) {
        return given().log().all()
                .contentType(CONTENT_TYPE)
                .sessionId(세션)
                .when()
                .get(PROJECT_BASE_URL + "/check/{id}", 프로젝트_아이디)
                .then()
                .log().all()
                .extract();
    }

    public static Long 기본_프로젝트() {
        final ProjectRequestDto 프로젝트_생성_요청 = ProjectRequestDto.builder()
                .title("프로젝트 제목")
                .content("프로젝트 내용")
                .field("프로젝트 분야")
                .recruitNumber(3)
                .techStack("프로젝트 기술 스택")
                .frontendNumber(1)
                .designerNumber(1)
                .backendNumber(1)
                .build();
        final ExtractableResponse<Response> 요청을_받는_포르젝트_생성 = 요청을_받는_포르젝트_생성(기본_세션, 프로젝트_생성_요청);
        final String postId = String.valueOf((Object) 요청을_받는_포르젝트_생성.jsonPath().get("postId"));
        return Long.parseLong(postId);
    }

    public static ExtractableResponse<Response> 프로젝트_삭제_요청(final String 세션) {
        return given().log().all()
                .sessionId(세션)
                .when()
                .delete(PROJECT_BASE_URL + "/{id}/delete", 프로젝트_아이디)
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 응답을_반환하는_모든_프로젝트_조회(final String 세션) {
        return given().log().all()
                .sessionId(세션)
                .when()
                .get(PROJECT_BASE_URL)
                .then()
                .log().all()
                .extract();
    }
}
