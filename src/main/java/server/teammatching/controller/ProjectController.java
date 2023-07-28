package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.teammatching.dto.request.ProjectRequestDto;
import server.teammatching.dto.response.ProjectResponseDto;
import server.teammatching.service.ProjectService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
@Api(tags = {"프로젝트 API"})
public class ProjectController {

    private final ProjectService projectService;

    @ApiOperation(value = "프로젝트 생성 API")
    @PostMapping("/new")
    public ResponseEntity create(@RequestBody ProjectRequestDto requestDto, Long memberId) {
        ProjectResponseDto responseDto = projectService.create(requestDto, memberId);
        return ResponseEntity.created(URI.create(String.format("/new/%s", responseDto.getPostId())))
                .body(responseDto);
    }

    @ApiOperation(value = "프로젝트 정보 수정 API")
    @PatchMapping("/{id}")
    public ResponseEntity update(@RequestBody ProjectRequestDto updateRequest, @PathVariable("id") Long projectId) {
        ProjectResponseDto updateResponse = projectService.update(updateRequest, projectId);
        return ResponseEntity.ok(updateResponse);
    }
}