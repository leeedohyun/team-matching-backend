package server.teammatching.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.teammatching.entity.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberUpdateResponseDto {

    private Long memberId;
    private String updatedNickName;
    private String updatedEmail;
    private String updatedUniversity;
    private String message;

    public static MemberUpdateResponseDto from(Member updatedMember, String message) {
        return MemberUpdateResponseDto.builder()
                .memberId(updatedMember.getId())
                .updatedNickName(updatedMember.getNickName())
                .updatedEmail(updatedMember.getEmail())
                .updatedUniversity(updatedMember.getUniversity())
                .message(message)
                .build();
    }
}
