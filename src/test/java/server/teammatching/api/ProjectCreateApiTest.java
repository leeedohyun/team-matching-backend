package server.teammatching.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static server.teammatching.api.steps.ProjectSteps.요청을_받는_포르젝트_생성;
import static server.teammatching.api.steps.ProjectSteps.프로젝트_삭제_요청;

import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import server.teammatching.api.helper.InitApiTest;
import server.teammatching.api.steps.ProjectSteps;
import server.teammatching.dto.request.ProjectRequestDto;

public class ProjectCreateApiTest extends InitApiTest {

    @Test
    void 프로젝트_생성() {
        // given
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

        // when
        final ExtractableResponse<Response> 프로젝트_생성_응답 = 요청을_받는_포르젝트_생성(기본_세션, 프로젝트_생성_요청);

        // then
        assertThat(프로젝트_생성_응답.statusCode()).isEqualTo(CREATED.value());
    }

    @Test
    void 로그인이_되어_있지_않은_사용자가_프로젝트_생성_시_실패() {
        // given
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

        // when
        final ExtractableResponse<Response> 프로젝트_생성_응답 = 요청을_받는_포르젝트_생성(빈_세션, 프로젝트_생성_요청);

        // then
        assertThat(프로젝트_생성_응답.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    void 모집_인원과_각_분야별_모집_인원의_합이_맞지_않으면_생성_실패() {
        // given
        final ProjectRequestDto 프로젝트_생성_요청 = ProjectRequestDto.builder()
                .title("프로젝트 제목")
                .content("프로젝트 내용")
                .field("프로젝트 분야")
                .recruitNumber(2)
                .techStack("프로젝트 기술 스택")
                .frontendNumber(1)
                .designerNumber(1)
                .backendNumber(1)
                .build();

        // when
        final ExtractableResponse<Response> 프로젝트_생성_응답 = 요청을_받는_포르젝트_생성(기본_세션, 프로젝트_생성_요청);

        // then
        assertThat(프로젝트_생성_응답.statusCode()).isEqualTo(BAD_REQUEST.value());
    }

    @Test
    void 프로젝트_삭제() {
        // given
        // when
        final ExtractableResponse<Response> 프로젝트_삭제_응답 = 프로젝트_삭제_요청(기본_세션);

        // then
        assertThat(프로젝트_삭제_응답.statusCode()).isEqualTo(OK.value());
    }

    @Test
    void 로그인하지_않은_사용자가_프로젝트_삭제_시_실패() {
        // given
        // when
        final ExtractableResponse<Response> 프로젝트_삭제_응답 = ProjectSteps.프로젝트_삭제_요청(빈_세션);

        // then
        assertThat(프로젝트_삭제_응답.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }
}
