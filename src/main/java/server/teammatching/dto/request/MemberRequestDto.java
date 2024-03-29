package server.teammatching.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberRequestDto {

    @NotBlank(message = "아이디를 입력해야 합니다.")
    @ApiModelProperty(example = "아이디")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해야 합니다.")
    @ApiModelProperty(example = "비밀번호")
    private String password;

    @NotBlank(message = "이메일을 입력해야 합니다.")
    @ApiModelProperty(example = "이메일")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotBlank(message = "닉네임을 입력해야 합니다.")
    @ApiModelProperty(example = "닉네임")
    private String nickName;

    @NotBlank(message = "현재 재학중인 대학교를 입력해야 합니다.")
    @ApiModelProperty(example = "대학교")
    private String university;
}
