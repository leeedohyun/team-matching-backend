package server.teammatching.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static server.teammatching.api.steps.StudySteps.스터디_상세_조회_응답;
import static server.teammatching.api.steps.StudySteps.요청을_받는_스터디_생성;
import static server.teammatching.api.steps.StudySteps.응답을_반환하는_모든_스터디_조회;
import static server.teammatching.api.steps.StudySteps.응답을_반환하는_회원이_생성한_스터디_조회;

import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import server.teammatching.api.helper.InitApiTest;
import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;

public class StudyReadApiTest extends InitApiTest {

    @Test
    void 스터디_상세_조회() {
        // given
        final TeamAndStudyCreateRequestDto 스터디_생성_요청 = TeamAndStudyCreateRequestDto.builder()
                .title("스터디 제목")
                .content("스터디 내용")
                .recruitNumber(3)
                .build();
        요청을_받는_스터디_생성(스터디_생성_요청, 기본_세션);

        // when
        final ExtractableResponse<Response> 스터디_상세_조회_응답 = 스터디_상세_조회_응답();

        // then
        assertThat(스터디_상세_조회_응답.statusCode()).isEqualTo(OK.value());
    }

    @Test
    void 회원이_생성한_스터디_조회() {
        // given
        final TeamAndStudyCreateRequestDto 스터디_생성_요청 = TeamAndStudyCreateRequestDto.builder()
                .title("스터디 제목")
                .content("스터디 내용")
                .recruitNumber(3)
                .build();
        요청을_받는_스터디_생성(스터디_생성_요청, 기본_세션);

        // when
        final ExtractableResponse<Response> 회원이_생성한_스터디_상세_조회_응답 = 응답을_반환하는_회원이_생성한_스터디_조회(기본_세션, 기본_회원_아이디);

        // then
        assertThat(회원이_생성한_스터디_상세_조회_응답.statusCode()).isEqualTo(OK.value());
    }

    @Test
    void 로그인하지_않은_사용자가_스터디_조회_시_실패() {
        // given
        final TeamAndStudyCreateRequestDto 스터디_생성_요청 = TeamAndStudyCreateRequestDto.builder()
                .title("스터디 제목")
                .content("스터디 내용")
                .recruitNumber(3)
                .build();
        요청을_받는_스터디_생성(스터디_생성_요청, 기본_세션);

        // when
        final ExtractableResponse<Response> 회원이_생성한_스터디_상세_조회_응답 = 응답을_반환하는_회원이_생성한_스터디_조회(빈_세션, 기본_회원_아이디);

        // then
        assertThat(회원이_생성한_스터디_상세_조회_응답.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    void 모든_스터디_조회() {
        // given
        final TeamAndStudyCreateRequestDto 스터디_생성_요청1 = TeamAndStudyCreateRequestDto.builder()
                .title("스터디 제목")
                .content("스터디 내용")
                .recruitNumber(3)
                .build();
        요청을_받는_스터디_생성(스터디_생성_요청1, 기본_세션);

        final TeamAndStudyCreateRequestDto 스터디_생성_요청2 = TeamAndStudyCreateRequestDto.builder()
                .title("스터디 제목")
                .content("스터디 내용")
                .recruitNumber(3)
                .build();
        요청을_받는_스터디_생성(스터디_생성_요청2, 기본_세션);

        // when
        final ExtractableResponse<Response> 모든_스터디_조회_응답 = 응답을_반환하는_모든_스터디_조회();

        // then
        assertThat(모든_스터디_조회_응답.statusCode()).isEqualTo(OK.value());
    }
}
