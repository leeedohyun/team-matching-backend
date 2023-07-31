package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @GetMapping("")
    public ResponseEntity<List<RecruitmentResponse>> checkApplications(@RequestParam Long postId) {
        List<RecruitmentResponse> response = recruitmentService.checkApplications(postId);
        return ResponseEntity.ok(response);
    }
}
