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
public class MemberUpdateRequestDto {

    private String updatedNickName;
    private String updatedEmail;
    private String updatedUniversity;
}
