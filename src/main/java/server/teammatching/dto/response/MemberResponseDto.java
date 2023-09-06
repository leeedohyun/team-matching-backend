package server.teammatching.dto.response;

import lombok.*;
import server.teammatching.entity.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberResponseDto {

    private Long memberId;
    private String loginId;
    private String email;
    private String university;
    private String nickName;

    public static MemberResponseDto from(Member member) {
        return MemberResponseDto.builder()
                .email(member.getEmail())
                .loginId(member.getLoginId())
                .memberId(member.getId())
                .university(member.getUniversity())
                .nickName(member.getNickName())
                .build();
    }
}
