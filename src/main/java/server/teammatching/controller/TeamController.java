package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.teammatching.dto.request.TeamForm;
import server.teammatching.dto.response.TeamResponse;
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
    public ResponseEntity create(@RequestBody TeamForm form, @RequestParam Long memberId) {
        TeamResponse teamResponse = teamService.create(form, memberId);
        return ResponseEntity.created(URI.create(String.format("/new/%s", teamResponse.getPostId())))
                .body(teamResponse);
    }
}
