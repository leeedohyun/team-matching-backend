package server.teammatching.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static server.teammatching.api.steps.AuthSteps.로그인;
import static server.teammatching.api.steps.MemberSteps.요청을_받는_회원_가입;
import static server.teammatching.api.steps.ProjectSteps.요청을_받는_포르젝트_생성;
import static server.teammatching.api.steps.ProjectSteps.응답을_반환하는_모든_프로젝트_조회;
import static server.teammatching.api.steps.ProjectSteps.프로젝트_상세_조회;
import static server.teammatching.api.steps.ProjectSteps.회원이_생성한_모든_프로젝트_조회;

import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import server.teammatching.api.helper.InitApiTest;
import server.teammatching.dto.request.LoginRequest;
import server.teammatching.dto.request.MemberRequestDto;
import server.teammatching.dto.request.ProjectRequestDto;

public class ProjectReadApiTest extends InitApiTest {

    @Test
    void 로그인_한_사용자의_프로젝트_상세_조회() {
        // given
        // when
        final ExtractableResponse<Response> 프로젝트_상세_조회_응답 = 프로젝트_상세_조회(기본_세션, 프로젝트_아이디);

        // then
        assertThat(프로젝트_상세_조회_응답.statusCode()).isEqualTo(OK.value());
    }

    @Test
    void 로그인_하지_않은_사용자의_프로젝트_상세_조회() {
        // given
        // when
        final ExtractableResponse<Response> 프로젝트_상세_조회_응답 = 프로젝트_상세_조회(빈_세션, 프로젝트_아이디);

        // then
        assertThat(프로젝트_상세_조회_응답.statusCode()).isEqualTo(OK.value());
    }

    @Test
    void 회원이_생성한_프로젝트_조회() {
        // given
        // when
        final ExtractableResponse<Response> 프로젝트_조회_응답 = 회원이_생성한_모든_프로젝트_조회(기본_세션);

        // then
        assertThat(프로젝트_조회_응답.statusCode()).isEqualTo(OK.value());
    }

    @Test
    void 로그인하지_않은_사용자가_회원이_생성한_프로젝트_조회_시_실패() {
        // given
        // when
        final ExtractableResponse<Response> 프로젝트_조회_응답 = 회원이_생성한_모든_프로젝트_조회(빈_세션);

        // then
        assertThat(프로젝트_조회_응답.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    void 모든_프로젝트_조회() {
        // given
        final MemberRequestDto 회원_가입_요청 = MemberRequestDto.builder()
                .loginId("hell0")
                .nickName("two")
                .email("enail@naber.com")
                .password("1234")
                .university("홍익대학교")
                .build();
        요청을_받는_회원_가입(회원_가입_요청);

        final String 세션 = 로그인(new LoginRequest("hell0", "1234"));

        final ProjectRequestDto 프로젝트_생성_요청 = ProjectRequestDto.builder()
                .title("프로젝트 제목11")
                .content("프로젝트 내용22")
                .field("프로젝트 분야33")
                .recruitNumber(5)
                .techStack("프로젝트 기술 스택111")
                .frontendNumber(2)
                .designerNumber(2)
                .backendNumber(1)
                .build();
        요청을_받는_포르젝트_생성(세션, 프로젝트_생성_요청);

        // when
        final ExtractableResponse<Response> 프로젝트_조회_응답 = 응답을_반환하는_모든_프로젝트_조회(세션);

        // then
        assertThat(프로젝트_조회_응답.statusCode()).isEqualTo(OK.value());
    }

    @Test
    void 모든_프로젝트_조회_시_로그인이_되어있지_않아도_성공() {
        // given
        final MemberRequestDto 회원_가입_요청 = MemberRequestDto.builder()
                .loginId("hell0")
                .nickName("two")
                .email("enail@naber.com")
                .password("1234")
                .university("홍익대학교")
                .build();
        요청을_받는_회원_가입(회원_가입_요청);

        final String 세션 = 로그인(new LoginRequest("hell0", "1234"));

        final ProjectRequestDto 프로젝트_생성_요청 = ProjectRequestDto.builder()
                .title("프로젝트 제목11")
                .content("프로젝트 내용22")
                .field("프로젝트 분야33")
                .recruitNumber(5)
                .techStack("프로젝트 기술 스택111")
                .frontendNumber(2)
                .designerNumber(2)
                .backendNumber(1)
                .build();
        요청을_받는_포르젝트_생성(세션, 프로젝트_생성_요청);

        // when
        final ExtractableResponse<Response> 프로젝트_조회_응답 = 응답을_반환하는_모든_프로젝트_조회(빈_세션);

        // then
        assertThat(프로젝트_조회_응답.statusCode()).isEqualTo(OK.value());
    }
}
