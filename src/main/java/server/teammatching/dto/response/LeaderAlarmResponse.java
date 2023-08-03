package server.teammatching.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LeaderAlarmResponse {

    private Long alarmId;
    private String title;
}
