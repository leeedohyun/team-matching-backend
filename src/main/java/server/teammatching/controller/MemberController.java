package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import server.teammatching.Service.MemberService;
import server.teammatching.dto.request.MemberRequestDto;
import server.teammatching.dto.response.MemberResponseDto;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Api(tags = {"회원 API"})
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "회원 가입", notes = "회원 가입을 합니다.")
    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity create(@Valid @RequestBody MemberRequestDto request,
                                       BindingResult result) {

        MemberResponseDto responseDto = memberService.join(request);

        return ResponseEntity.created(URI.create(String.format("/new/%s", responseDto.getMemberId())))
                .body(responseDto);
    }
}
