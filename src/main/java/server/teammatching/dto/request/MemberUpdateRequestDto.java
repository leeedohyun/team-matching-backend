package server.teammatching.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberUpdateRequestDto {

    private String updatedNickName;
    private String updatedEmail;
    private String updatedUniversity;
}
