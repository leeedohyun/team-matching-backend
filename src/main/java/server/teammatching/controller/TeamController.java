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

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
@Api(tags = {"팀 API"})
public class TeamController {

    private final TeamService teamService;

    @ApiOperation(value = "팀 생성 API")
    @PostMapping("/new")
    public ResponseEntity create(@RequestBody TeamAndStudyCreateRequestDto form, @RequestParam Long memberId) {
        TeamAndStudyCreateResponseDto teamAndStudyCreateResponseDto = teamService.create(form, memberId);
        return ResponseEntity.created(URI.create(String.format("/new/%s", teamAndStudyCreateResponseDto.getPostId())))
                .body(teamAndStudyCreateResponseDto);
    }

    @ApiOperation(value = "팀 정보 수정 API")
    @PatchMapping("/{id}")
    public ResponseEntity update(@RequestBody TeamAndStudyCreateRequestDto requestDto,
                                 @PathVariable("id") Long postId) {
        TeamAndStudyCreateResponseDto responseDto = teamService.update(requestDto, postId);
        return ResponseEntity.ok(responseDto);
    }
}
