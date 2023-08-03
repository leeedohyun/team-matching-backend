package server.teammatching.dto.response;

import lombok.*;
import server.teammatching.entity.ApplicationStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ApplicantAlarmResponse {

    private Long alarmId;
    private String title;
    private ApplicationStatus applicationStatus;
}
