package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.teammatching.dto.response.AlarmResponse;
import server.teammatching.service.AlarmService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"알림 API"})
@RequestMapping("/alarms")
public class AlarmController {

    private final AlarmService alarmService;

    @ApiOperation(value = "알림 조회 API")
    @GetMapping("")
    public ResponseEntity<List<AlarmResponse>> checkAlarms(@RequestParam Long memberId) {
        List<AlarmResponse> response = alarmService.checkAlarms(memberId);
        return ResponseEntity.ok(response);
    }
}
