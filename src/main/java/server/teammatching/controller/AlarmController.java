package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<List<ApplicantAlarmResponse>> checkApplicantAlarms(@RequestParam Long memberId) {
        List<ApplicantAlarmResponse> response = alarmService.checkApplicantAlarms(memberId);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "리더 알림 조회 API")
    @GetMapping("/leader")
    public ResponseEntity<List<LeaderAlarmResponse>> checkLeaderAlarms(@RequestParam Long memberId) {
        List<LeaderAlarmResponse> response = alarmService.checkLeaderAlarms(memberId);
        return ResponseEntity.ok(response);
    }
}
