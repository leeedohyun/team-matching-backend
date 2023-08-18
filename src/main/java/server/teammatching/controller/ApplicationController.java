package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import server.teammatching.auth.PrincipalDetails;
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
                                                            @AuthenticationPrincipal PrincipalDetails principal) {
        if (principal == null) {
            throw new RuntimeException("인증 정보가 없습니다");
        }
        ApplicationResponse applicationResponse = applicationService.applyProject(projectId, principal.getUsername());
        return ResponseEntity.ok(applicationResponse);
    }

    @ApiOperation(value = "스터디 지원 API")
    @PostMapping("/studies/{id}")
    public ResponseEntity<ApplicationResponse> applyStudy(@PathVariable("id") Long studyId,
                                                          @AuthenticationPrincipal PrincipalDetails principal) {
        if (principal == null) {
            throw new RuntimeException("인증 정보가 없습니다");
        }
        ApplicationResponse applicationResponse = applicationService.applyStudy(studyId, principal.getUsername());
        return ResponseEntity.ok(applicationResponse);
    }

    @ApiOperation(value = "팀 지원 API")
    @PostMapping("/teams/{id}")
    public ResponseEntity<ApplicationResponse> applyTeam(@PathVariable("id") Long studyId,
                                                         @AuthenticationPrincipal PrincipalDetails principal) {
        if (principal == null) {
            throw new RuntimeException("인증 정보가 없습니다");
        }
        ApplicationResponse applicationResponse = applicationService.applyTeam(studyId, principal.getUsername());
        return ResponseEntity.ok(applicationResponse);
    }

    @ApiOperation(value = "회원이 지원한 모든 지원 리스트 조회 API")
    @GetMapping("")
    public ResponseEntity<List<ApplicationResponse>> checkAllApplications(@AuthenticationPrincipal PrincipalDetails principal) {
        if (principal == null) {
            throw new RuntimeException("인증 정보가 없습니다");
        }
        List<ApplicationResponse> applicationResponses = applicationService.checkAllApplications(principal.getUsername());
        return ResponseEntity.ok(applicationResponses);
    }

    @ApiOperation(value = "지원 취소 API")
    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<String> deleteApplication(@PathVariable("id") Long applicationId,
                                                    @AuthenticationPrincipal PrincipalDetails principal) {
        if (principal == null) {
            throw new RuntimeException("인증 정보가 없습니다");
        }
        applicationService.deleteApplication(applicationId, principal.getUsername());
        return ResponseEntity.ok("삭제되었습니다.");
    }

    @ApiOperation(value = "지원 승인 API")
    @PostMapping("/approve")
    public ResponseEntity<ApplicationResponse> approveApplication(@RequestParam Long applicationId,
                                                                  @AuthenticationPrincipal PrincipalDetails principal) {
        if (principal == null) {
            throw new RuntimeException("인증 정보가 없습니다");
        }
        ApplicationResponse approvalResponse = applicationService.approveApplication(applicationId, principal.getUsername());
        return ResponseEntity.ok(approvalResponse);
    }

    @ApiOperation(value = "지원 거절 API")
    @PostMapping("/reject")
    public ResponseEntity<ApplicationResponse> rejectApplication(@RequestParam Long applicationId,
                                                                 @AuthenticationPrincipal PrincipalDetails principal) {
        if (principal == null) {
            throw new RuntimeException("인증 정보가 없습니다");
        }
        ApplicationResponse approvalResponse = applicationService.rejectApplication(applicationId, principal.getUsername());
        return ResponseEntity.ok(approvalResponse);
    }
}
