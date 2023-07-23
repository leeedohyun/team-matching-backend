package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import server.teammatching.entity.Member;
import server.teammatching.service.MemberService;
import server.teammatching.dto.request.MemberRequestDto;
import server.teammatching.dto.response.MemberResponseDto;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Api(tags = {"회원 API"})
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "회원 가입", notes = "회원 가입을 합니다.")
    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<MemberResponseDto> create(@Valid @RequestBody MemberRequestDto request,
                                       BindingResult result) {

        MemberResponseDto responseDto = memberService.join(request);

        return ResponseEntity.created(URI.create(String.format("/new/%s", responseDto.getMemberId())))
                .body(responseDto);
    }

    @ApiOperation(value = "모든 회원 조회")
    @GetMapping("")
    public ResponseEntity<List<Member>> list() {
        List<Member> memberList = memberService.findAll();
        return new ResponseEntity<>(memberList, HttpStatus.OK);
    }
}
