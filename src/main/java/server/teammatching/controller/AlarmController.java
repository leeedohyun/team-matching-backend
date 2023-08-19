package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.teammatching.auth.AuthenticationUtils;
import server.teammatching.auth.PrincipalDetails;
import server.teammatching.dto.response.ApplicantAlarmResponse;
import server.teammatching.dto.response.LeaderAlarmResponse;
import server.teammatching.service.AlarmService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"알림 API"})
@RequestMapping("/alarms")
public class AlarmController {

    private final AlarmService alarmService;

    @ApiOperation(value = "지원자 알림 조회 API")
    @GetMapping("/applicant")
    public ResponseEntity<List<ApplicantAlarmResponse>> checkApplicantAlarms(@AuthenticationPrincipal PrincipalDetails principal) {
        AuthenticationUtils.validateAuthentication(principal);
        List<ApplicantAlarmResponse> response = alarmService.checkApplicantAlarms(principal.getUsername());
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "리더 알림 조회 API")
    @GetMapping("/leader")
    public ResponseEntity<List<LeaderAlarmResponse>> checkLeaderAlarms(@AuthenticationPrincipal PrincipalDetails principal) {
        AuthenticationUtils.validateAuthentication(principal);
        List<LeaderAlarmResponse> response = alarmService.checkLeaderAlarms(principal.getUsername());
        return ResponseEntity.ok(response);
    }
}
