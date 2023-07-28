package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;
import server.teammatching.dto.response.TeamAndStudyCreateResponseDto;
import server.teammatching.service.TeamService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
@Api(tags = {"팀 API"})
public class TeamController {

    private final TeamService teamService;

    @ApiOperation(value = "팀 생성 API")
    @PostMapping("/new")
    public ResponseEntity<TeamAndStudyCreateResponseDto> create(@RequestBody TeamAndStudyCreateRequestDto form,
                                                                @RequestParam Long memberId) {
        TeamAndStudyCreateResponseDto teamAndStudyCreateResponseDto = teamService.create(form, memberId);
        return ResponseEntity.created(URI.create(String.format("/new/%s", teamAndStudyCreateResponseDto.getPostId())))
                .body(teamAndStudyCreateResponseDto);
    }

    @ApiOperation(value = "팀 정보 수정 API")
    @PatchMapping("/{id}")
    public ResponseEntity<TeamAndStudyCreateResponseDto> update(@RequestBody TeamAndStudyCreateRequestDto requestDto,
                                                                @PathVariable("id") Long postId) {
        TeamAndStudyCreateResponseDto responseDto = teamService.update(requestDto, postId);
        return ResponseEntity.ok(responseDto);
    }

    @ApiOperation(value = "모든 팀 조회 API")
    @GetMapping("")
    public ResponseEntity<List<TeamAndStudyCreateResponseDto>> checkAllTeams() {
        List<TeamAndStudyCreateResponseDto> allTeamsResponse = teamService.checkAllTeams();
        return ResponseEntity.ok(allTeamsResponse);
    }

    @ApiOperation(value = "회원이 생성한 팀 조회 API")
    @GetMapping("/{id}")
    public ResponseEntity<List<TeamAndStudyCreateResponseDto>> checkMemberTeams(@PathVariable("id") Long memberId) {
        List<TeamAndStudyCreateResponseDto> allMemberTeamsResponse = teamService.checkMemberTeams(memberId);
        return ResponseEntity.ok(allMemberTeamsResponse);
    }

    @ApiOperation(value = "팀 삭제 API")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> delete(@PathVariable("id") Long teamId) {
        teamService.delete(teamId);
        return ResponseEntity.ok("정상적으로 삭제되었습니다.");
    }
}
