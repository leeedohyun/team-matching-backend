package server.teammatching.dto.response;

import lombok.*;
import server.teammatching.entity.ApplicationStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AlarmResponse {

    private Long alarmId;
    private ApplicationStatus applicationStatus;
}
