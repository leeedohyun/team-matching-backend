package server.teammatching.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TeamAndStudyCreateRequestDto {

    private String title;
    private int recruitNumber;
    private String field;
    private String content;
}
