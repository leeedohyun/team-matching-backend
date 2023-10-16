package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.teammatching.auth.AuthenticationUtils;
import server.teammatching.auth.PrincipalDetails;
import server.teammatching.dto.response.ResumeResponseDto;
import server.teammatching.service.ResumeService;

@RestController
@RequiredArgsConstructor
@Api(tags = {"지원서 조회 API"})
@RequestMapping("/resume")
public class ResumeController {

    private final ResumeService resumeService;

    @ApiOperation(value = "지원서 조회 API")
    @GetMapping("/{applicationId}")
    public ResponseEntity<ResumeResponseDto> getResume(@PathVariable("applicationId") Long applicationId,
                                                       @AuthenticationPrincipal PrincipalDetails principal) {
        AuthenticationUtils.validateAuthentication(principal);
        ResumeResponseDto resumeResponseDto = resumeService.findResume(applicationId);
        return ResponseEntity.ok(resumeResponseDto);
    }
}
