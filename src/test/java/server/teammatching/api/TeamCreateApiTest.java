package server.teammatching.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static server.teammatching.api.steps.TeamSteps.요청을_받는_팀_생성;
import static server.teammatching.api.steps.TeamSteps.응답을_반환하는_팀_삭제;

import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import server.teammatching.api.helper.InitApiTest;
import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;

class TeamCreateApiTest extends InitApiTest {

    @Test
    void 팀_생성() {
        // given
        final TeamAndStudyCreateRequestDto 팀_생성_요청 = TeamAndStudyCreateRequestDto.builder()
                .title("팀 제목")
                .content("팀 내용")
                .recruitNumber(3)
                .build();

        // when
        final ExtractableResponse<Response> 팀_생성_응답 = 요청을_받는_팀_생성(팀_생성_요청, 기본_세션);

        // then
        assertThat(팀_생성_응답.statusCode()).isEqualTo(CREATED.value());
    }

    @Test
    void 로그인하지_않은_사용자가_팀_생성_시_실패() {
        // given
        final TeamAndStudyCreateRequestDto 팀_생성_요청 = TeamAndStudyCreateRequestDto.builder()
                .title("팀 제목")
                .content("팀 내용")
                .recruitNumber(3)
                .build();

        // when
        final ExtractableResponse<Response> 팀_생성_응답 = 요청을_받는_팀_생성(팀_생성_요청, 빈_세션);

        // then
        assertThat(팀_생성_응답.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    void 팀_삭제() {
        // given
        final TeamAndStudyCreateRequestDto 팀_생성_요청 = TeamAndStudyCreateRequestDto.builder()
                .title("팀 제목")
                .content("팀 내용")
                .recruitNumber(3)
                .build();
        요청을_받는_팀_생성(팀_생성_요청, 기본_세션);

        // when
        final ExtractableResponse<Response> 팀_삭제_응답 = 응답을_반환하는_팀_삭제(기본_세션);

        // then
        assertThat(팀_삭제_응답.statusCode()).isEqualTo(OK.value());
    }

    @Test
    void 로그인하지_않은_사용자가_팀_삭제_시_실패() {
        // given
        final TeamAndStudyCreateRequestDto 팀_생성_요청 = TeamAndStudyCreateRequestDto.builder()
                .title("팀 제목")
                .content("팀 내용")
                .recruitNumber(3)
                .build();
        요청을_받는_팀_생성(팀_생성_요청, 기본_세션);


        // when
        final ExtractableResponse<Response> 팀_삭제_응답 = 응답을_반환하는_팀_삭제(빈_세션);

        // then
        assertThat(팀_삭제_응답.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    void 삭제할_팀가_없으면_예외_발생() {
        // given
        // when
        final ExtractableResponse<Response> 팀_삭제_응답 = 응답을_반환하는_팀_삭제(기본_세션);

        // then
        assertThat(팀_삭제_응답.statusCode()).isEqualTo(NOT_FOUND.value());
    }
}
