package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.teammatching.dto.response.ApplicationResponse;
import server.teammatching.service.ApplicationService;

@RestController
@RequiredArgsConstructor
@Api(tags = {"지원 API"})
public class ApplicationController {

    private final ApplicationService applicationService;

    @ApiOperation(value = "프로젝트 지원 API")
    @PostMapping("/{projectId}/")
    public ResponseEntity<ApplicationResponse> applyProject(@PathVariable("projectId") Long projectId, @RequestParam Long memberId) {
        ApplicationResponse applicationResponse = applicationService.applyProject(projectId, memberId);
        return ResponseEntity.ok(applicationResponse);
    }
}
