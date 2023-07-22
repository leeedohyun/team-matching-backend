package server.teammatching.dto.response;

import lombok.*;
import server.teammatching.entity.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString
public class MemberResponseDto {

    private Long memberId;
    private String loginId;
    private String email;
    private String university;
    private String nickName;
    private String message;

    public static MemberResponseDto from(Member member, String message) {
        return MemberResponseDto.builder()
                .email(member.getEmail())
                .loginId(member.getLoginId())
                .memberId(member.getId())
                .university(member.getUniversity())
                .nickName(member.getNickName())
                .message(message)
                .build();
    }
}
