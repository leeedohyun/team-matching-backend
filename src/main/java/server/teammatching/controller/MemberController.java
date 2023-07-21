package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import server.teammatching.Service.MemberService;
import server.teammatching.dto.MemberRequest;
import server.teammatching.entity.Member;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Api(tags = {"회원 API"})
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "회원 생성", notes = "회원 가입을 합니다.")
    @PostMapping("/new")
    public ResponseEntity<Member> create(@Valid @RequestBody MemberRequest request,
                                         BindingResult result) {

        Member createdMember = memberService.join(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdMember.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
