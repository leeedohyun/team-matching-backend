package server.teammatching.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static server.teammatching.api.steps.StudySteps.요청을_받는_스터디_생성;
import static server.teammatching.api.steps.StudySteps.응답을_반환하는_스터디_삭제;

import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import server.teammatching.api.helper.InitApiTest;
import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;

class StudyCreateApiTest extends InitApiTest {

    @Test
    void 스터디_생성() {
        // given
        final TeamAndStudyCreateRequestDto 스터디_생성_요청 = TeamAndStudyCreateRequestDto.builder()
                .title("스터디 제목")
                .content("스터디 내용")
                .recruitNumber(3)
                .build();

        // when
        final ExtractableResponse<Response> 스터디_생성_응답 = 요청을_받는_스터디_생성(스터디_생성_요청, 기본_세션);

        // then
        assertThat(스터디_생성_응답.statusCode()).isEqualTo(CREATED.value());
    }

    @Test
    void 로그인하지_않은_사용자가_스터디_생성_시_실패() {
        // given
        final TeamAndStudyCreateRequestDto 스터디_생성_요청 = TeamAndStudyCreateRequestDto.builder()
                .title("스터디 제목")
                .content("스터디 내용")
                .recruitNumber(3)
                .build();

        // when
        final ExtractableResponse<Response> 스터디_생성_응답 = 요청을_받는_스터디_생성(스터디_생성_요청, 빈_세션);

        // then
        assertThat(스터디_생성_응답.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    void 스터디_삭제() {
        // given
        final TeamAndStudyCreateRequestDto 스터디_생성_요청 = TeamAndStudyCreateRequestDto.builder()
                .title("스터디 제목")
                .content("스터디 내용")
                .recruitNumber(3)
                .build();
        요청을_받는_스터디_생성(스터디_생성_요청, 기본_세션);

        // when
        final ExtractableResponse<Response> 스터디_삭제_응답 = 응답을_반환하는_스터디_삭제(기본_세션);

        // then
        assertThat(스터디_삭제_응답.statusCode()).isEqualTo(OK.value());
    }

    @Test
    void 로그인하지_않은_사용자가_스터디_삭제_시_실패() {
        // given
        final TeamAndStudyCreateRequestDto 스터디_생성_요청 = TeamAndStudyCreateRequestDto.builder()
                .title("스터디 제목")
                .content("스터디 내용")
                .recruitNumber(3)
                .build();
        요청을_받는_스터디_생성(스터디_생성_요청, 기본_세션);


        // when
        final ExtractableResponse<Response> 스터디_삭제_응답 = 응답을_반환하는_스터디_삭제(빈_세션);

        // then
        assertThat(스터디_삭제_응답.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    void 삭제할_스터디가_없으면_예외_발생() {
        // given
        // when
        final ExtractableResponse<Response> 스터디_삭제_응답 = 응답을_반환하는_스터디_삭제(기본_세션);

        // then
        assertThat(스터디_삭제_응답.statusCode()).isEqualTo(NOT_FOUND.value());
    }
}
