package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.teammatching.auth.AuthenticationUtils;
import server.teammatching.auth.PrincipalDetails;
import server.teammatching.dto.request.ProjectRequestDto;
import server.teammatching.dto.response.ProjectResponseDto;
import server.teammatching.service.ProjectService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
@Api(tags = {"프로젝트 API"})
public class ProjectController {

    private final ProjectService projectService;

    @ApiOperation(value = "프로젝트 생성 API")
    @PostMapping("/new")
    public ResponseEntity<ProjectResponseDto> create(@AuthenticationPrincipal PrincipalDetails principal,
                                                     @RequestBody ProjectRequestDto requestDto) {
        AuthenticationUtils.validateAuthentication(principal);
        ProjectResponseDto responseDto = projectService.create(principal.getUsername(), requestDto);
        return ResponseEntity.created(URI.create(String.format("/new/%s", responseDto.getPostId())))
                .body(responseDto);
    }

    @ApiOperation(value = "프로젝트 정보 수정 API")
    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> update(@PathVariable("id") Long projectId,
                                                     @AuthenticationPrincipal PrincipalDetails principal,
                                                     @RequestBody ProjectRequestDto updateRequest) {
        AuthenticationUtils.validateAuthentication(principal);
        ProjectResponseDto updateResponse = projectService.update(projectId, principal.getUsername(), updateRequest);
        return ResponseEntity.ok(updateResponse);
    }

    @ApiOperation(value = "모든 프로젝트 조회 API")
    @GetMapping("")
    public ResponseEntity<List<ProjectResponseDto>> checkAllProject() {
        List<ProjectResponseDto> allProjectsResponse = projectService.checkAllProjects();
        return ResponseEntity.ok(allProjectsResponse);
    }

    @ApiOperation(value = "회원이 생성한 프로젝트 조회 API")
    @GetMapping("/{id}")
    public ResponseEntity<List<ProjectResponseDto>> checkMemberProject(@PathVariable("id") String loginId,
                                                                       @AuthenticationPrincipal PrincipalDetails principal) {
        AuthenticationUtils.validateAuthentication(principal);
        List<ProjectResponseDto> allMemberProjectsResponse = projectService.checkMemberProjects(loginId, principal.getUsername());
        return ResponseEntity.ok(allMemberProjectsResponse);
    }

    @ApiOperation(value = "프로젝트 삭제 API")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> delete(@PathVariable("id") Long postId,
                                         @AuthenticationPrincipal PrincipalDetails principal) {
        AuthenticationUtils.validateAuthentication(principal);
        projectService.delete(postId, principal.getUsername());
        return ResponseEntity.ok("정상적으로 삭제되었습니다.");
    }
}
