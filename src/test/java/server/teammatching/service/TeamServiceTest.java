package server.teammatching.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;
import server.teammatching.dto.response.TeamAndStudyCreateResponseDto;
import server.teammatching.entity.Post;
import server.teammatching.exception.MemberNotFoundException;
import server.teammatching.exception.PostNotFoundException;

class TeamServiceTest extends PostServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Test
    void 팀_생성() {
        // given
        given(memberRepository.findByLoginId(any()))
                .willReturn(Optional.of(leader));

        final Post team = Post.createTeam("제목", "내용", 1, leader);

        given(postRepository.save(any()))
                .willReturn(team);

        // when
        final TeamAndStudyCreateRequestDto requestDto = TeamAndStudyCreateRequestDto.builder()
                .title("제목")
                .content("내용")
                .recruitNumber(1)
                .build();

        final TeamAndStudyCreateResponseDto responseDto = teamService.create(requestDto, "id");

        // then
        assertThat(responseDto).usingRecursiveComparison()
                .isEqualTo(TeamAndStudyCreateResponseDto.from(team));
    }

    @Test
    void 팀_생성_시_리더가_존재하지_않으면_예외_발생() {
        // given
        given(memberRepository.findByLoginId(any()))
                .willThrow(MemberNotFoundException.class);

        // when
        // then
        final TeamAndStudyCreateRequestDto requestDto = TeamAndStudyCreateRequestDto.builder()
                .title("제목")
                .content("내용")
                .recruitNumber(1)
                .build();

        assertThatThrownBy(() -> teamService.create(requestDto, "id"))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 팀_상세_조회() {
        // given
        final Post team = Post.builder()
                .id(1L)
                .title("제목")
                .content("내용")
                .recruitNumber(1)
                .leader(leader)
                .build();

        given(postRepository.findById(any()))
                .willReturn(Optional.of(team));

        // when
        final TeamAndStudyCreateResponseDto response = teamService.findOne(1L);

        // then
        assertThat(response).usingRecursiveComparison()
                .isEqualTo(TeamAndStudyCreateResponseDto.from(team));
    }

    @Test
    void 팀_상세_조회_시_팀이_존재하지_않으면_예외_발생() {
        // given
        given(postRepository.findById(any()))
                .willThrow(PostNotFoundException.class);

        // when
        // then
        assertThatThrownBy(() -> teamService.findOne(1L))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void 모든_팀_조회() {
        // given
        final Post team1 = Post.createTeam("제목1", "내용1", 1, leader);
        final Post team2 = Post.createTeam("제목2", "내용2", 2, leader);

        given(postRepository.findByType(any()))
                .willReturn(List.of(team1, team2));

        // when
        final List<TeamAndStudyCreateResponseDto> responses = teamService.checkAllTeams();

        // then
        assertAll(
                () -> assertThat(responses.get(0)).usingRecursiveComparison()
                        .isEqualTo(TeamAndStudyCreateResponseDto.from(team1)),
                () -> assertThat(responses.get(1)).usingRecursiveComparison()
                        .isEqualTo(TeamAndStudyCreateResponseDto.from(team2))
        );
    }

    @Test
    void 회원이_생성한_팀_조회() {
        // given
        given(memberRepository.findByLoginId(any()))
                .willReturn(Optional.of(leader));

        final Post team1 = Post.createTeam("제목1", "내용1", 1, leader);
        final Post team2 = Post.createTeam("제목2", "내용2", 2, leader);
        given(postRepository.findByLeaderAndType(any(), any()))
                .willReturn(List.of(team1, team2));

        // when
        final List<TeamAndStudyCreateResponseDto> responses = teamService.checkMemberTeams("id1");

        // then
        assertAll(
                () -> assertThat(responses.get(0)).usingRecursiveComparison()
                        .isEqualTo(TeamAndStudyCreateResponseDto.from(team1)),
                () -> assertThat(responses.get(1)).usingRecursiveComparison()
                        .isEqualTo(TeamAndStudyCreateResponseDto.from(team2))
        );
    }

    @Test
    void 회원이_생성한_팀_조회_시_존재하지_않는_회원이면_예외_발생() {
        // given
        given(memberRepository.findByLoginId(any()))
                .willThrow(MemberNotFoundException.class);

        // when
        // then
        assertThatThrownBy(() -> teamService.checkMemberTeams("id1"))
            .isInstanceOf(MemberNotFoundException.class);
    }
}
