package server.teammatching.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import server.teammatching.auth.AuthenticationUtils;
import server.teammatching.auth.PrincipalDetails;
import server.teammatching.dto.request.MemberRequestDto;
import server.teammatching.dto.request.MemberUpdateRequestDto;
import server.teammatching.dto.response.MemberResponseDto;
import server.teammatching.dto.response.MemberUpdateResponseDto;
import server.teammatching.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Api(tags = {"회원 API"})
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "회원 가입 API", notes = "회원 가입을 합니다.")
    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<MemberResponseDto> create(@RequestBody @Valid MemberRequestDto request) {
        MemberResponseDto responseDto = memberService.join(request);

        return ResponseEntity.created(URI.create(String.format("/new/%s", responseDto.getMemberId())))
                .body(responseDto);
    }

    @ApiOperation(value = "마이 페이지 조회 API")
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMemberInfo(@PathVariable("id") String loginId,
                                                           @AuthenticationPrincipal PrincipalDetails principal) {
        AuthenticationUtils.validateAuthentication(principal);
        MemberResponseDto memberResponse = memberService.findOne(loginId, principal.getUsername());
        return ResponseEntity.ok(memberResponse);
    }

    @ApiOperation(value = "모든 회원 조회 API")
    @GetMapping("")
    public ResponseEntity<List<MemberResponseDto>> list() {
        List<MemberResponseDto> memberListResponse = memberService.findAll();
        return ResponseEntity.ok(memberListResponse);
    }

    @ApiOperation(value = "회원 정보 수정 API")
    @PatchMapping("/{id}/edit")
    public ResponseEntity<MemberUpdateResponseDto> update(@PathVariable("id") String loginId,
                                                          @RequestBody MemberUpdateRequestDto updateRequest,
                                                          @AuthenticationPrincipal PrincipalDetails principal) {
        AuthenticationUtils.validateAuthentication(principal);
        MemberUpdateResponseDto updateResponse = memberService.update(loginId, updateRequest, principal.getUsername());
        return ResponseEntity.ok(updateResponse);
    }

    @ApiOperation(value = "회원 탈퇴 API")
    @DeleteMapping("{id}/withdrawal")
    public ResponseEntity<String> delete(@PathVariable("id") String loginId,
                                         @AuthenticationPrincipal PrincipalDetails principal) {
        AuthenticationUtils.validateAuthentication(principal);
        memberService.delete(loginId);
        return ResponseEntity.ok("탈퇴가 되었습니다.");
    }
}
