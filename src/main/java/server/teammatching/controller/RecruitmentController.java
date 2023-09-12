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
import server.teammatching.dto.response.RecruitmentResponse;
import server.teammatching.service.RecruitmentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"모집 API"})
@RequestMapping("/recruitments")
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @ApiOperation(value = "지원 리스트 조회 API")
    @GetMapping("/{id}")
    public ResponseEntity<List<RecruitmentResponse>> checkApplications(@PathVariable("id") Long postId,
                                                                       @AuthenticationPrincipal PrincipalDetails principal) {
        AuthenticationUtils.validateAuthentication(principal);
        List<RecruitmentResponse> response = recruitmentService.checkApplications(postId, principal.getUsername());
        return ResponseEntity.ok(response);
    }
}
