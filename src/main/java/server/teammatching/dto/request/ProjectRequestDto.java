package server.teammatching.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProjectRequestDto {

    private String title;
    private int recruitNumber;
    private String field;
    private String content;
    private int designerNumber;
    private int frontendNumber;
    private int backendNumber;
}
