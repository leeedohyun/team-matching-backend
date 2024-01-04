package server.teammatching.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import server.teammatching.auth.AuthenticationUtils;
import server.teammatching.auth.PrincipalDetails;
import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;
import server.teammatching.dto.response.TeamAndStudyCreateResponseDto;
import server.teammatching.service.TeamService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
@Api(tags = {"팀 API"})
public class TeamController {

    private final TeamService teamService;

    @ApiOperation(value = "팀 생성 API")
    @PostMapping("/new")
    public ResponseEntity<TeamAndStudyCreateResponseDto> create(@RequestBody final TeamAndStudyCreateRequestDto form,
                                                                @AuthenticationPrincipal final PrincipalDetails principal) {
        AuthenticationUtils.validateAuthentication(principal);
        final TeamAndStudyCreateResponseDto teamAndStudyCreateResponseDto = teamService.create(form, principal.getUsername());
        return ResponseEntity.created(URI.create(String.format("/new/%s", teamAndStudyCreateResponseDto.getPostId())))
                .body(teamAndStudyCreateResponseDto);
    }

    @ApiOperation(value = "팀 정보 수정 API")
    @PatchMapping("/{id}")
    public ResponseEntity<TeamAndStudyCreateResponseDto> update(@PathVariable("id") final Long postId,
                                                                @AuthenticationPrincipal final PrincipalDetails principal,
                                                                @RequestBody final TeamAndStudyCreateRequestDto requestDto) {
        AuthenticationUtils.validateAuthentication(principal);
        final TeamAndStudyCreateResponseDto responseDto = teamService.update(postId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @ApiOperation(value = "모든 팀 조회 API")
    @GetMapping
    public ResponseEntity<List<TeamAndStudyCreateResponseDto>> checkAllTeams() {
        final List<TeamAndStudyCreateResponseDto> allTeamsResponse = teamService.checkAllTeams();
        return ResponseEntity.ok(allTeamsResponse);
    }

    @ApiOperation(value = "팀 상세 조회")
    @GetMapping("/check/{teamId}")
    public ResponseEntity<TeamAndStudyCreateResponseDto> checkOne(@PathVariable final Long teamId) {
        final TeamAndStudyCreateResponseDto responseDto = teamService.findOne(teamId);
        return ResponseEntity.ok(responseDto);
    }

    @ApiOperation(value = "회원이 생성한 팀 조회 API")
    @GetMapping("/{id}")
    public ResponseEntity<List<TeamAndStudyCreateResponseDto>> checkMemberTeams(@PathVariable("id") final String loginId,
                                                                                @AuthenticationPrincipal final PrincipalDetails principal) {
        AuthenticationUtils.validateAuthentication(principal);
        final List<TeamAndStudyCreateResponseDto> allMemberTeamsResponse = teamService.checkMemberTeams(loginId);
        return ResponseEntity.ok(allMemberTeamsResponse);
    }

    @ApiOperation(value = "팀 삭제 API")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> delete(@PathVariable("id") final Long teamId,
                                         @AuthenticationPrincipal final PrincipalDetails principal) {
        AuthenticationUtils.validateAuthentication(principal);
        teamService.delete(teamId);
        return ResponseEntity.ok("정상적으로 삭제되었습니다.");
    }
}
