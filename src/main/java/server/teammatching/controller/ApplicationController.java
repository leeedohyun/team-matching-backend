package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.teammatching.dto.response.ApplicationResponse;
import server.teammatching.service.ApplicationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"지원 API"})
@RequestMapping("/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    @ApiOperation(value = "프로젝트 지원 API")
    @PostMapping("/projects/{id}")
    public ResponseEntity<ApplicationResponse> applyProject(@PathVariable("id") Long projectId,
                                                            @RequestParam Long memberId) {
        ApplicationResponse applicationResponse = applicationService.applyProject(projectId, memberId);
        return ResponseEntity.ok(applicationResponse);
    }

    @ApiOperation(value = "스터디 지원 API")
    @PostMapping("/studies/{id}")
    public ResponseEntity<ApplicationResponse> applyStudy(@PathVariable("id") Long studyId,
                                                          @RequestParam Long memberId) {
        ApplicationResponse applicationResponse = applicationService.applyStudy(studyId, memberId);
        return ResponseEntity.ok(applicationResponse);
    }

    @ApiOperation(value = "팀 지원 API")
    @PostMapping("/teams/{id}")
    public ResponseEntity<ApplicationResponse> applyTeam(@PathVariable("id") Long studyId,
                                                          @RequestParam Long memberId) {
        ApplicationResponse applicationResponse = applicationService.applyTeam(studyId, memberId);
        return ResponseEntity.ok(applicationResponse);
    }

    @ApiOperation(value = "회원이 지원한 모든 지원 리스트 조회 API")
    @GetMapping("")
    public ResponseEntity<List<ApplicationResponse>> checkAllApplications(@RequestParam Long memberId) {
        List<ApplicationResponse> applicationResponses = applicationService.checkAllApplications(memberId);
        return ResponseEntity.ok(applicationResponses);
    }
}
