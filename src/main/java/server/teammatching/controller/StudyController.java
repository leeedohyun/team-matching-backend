package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
import server.teammatching.auth.AuthenticationUtils;
import server.teammatching.auth.PrincipalDetails;
import server.teammatching.dto.request.TeamAndStudyCreateRequestDto;
import server.teammatching.dto.response.TeamAndStudyCreateResponseDto;
import server.teammatching.service.StudyService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/studies")
@Api(tags = {"스터디 API"})
public class StudyController {

    private final StudyService studyService;

    @ApiOperation(value = "스터디 생성 API")
    @PostMapping("/new")
    public ResponseEntity<TeamAndStudyCreateResponseDto> create(@RequestBody TeamAndStudyCreateRequestDto requestDto,
                                                                @AuthenticationPrincipal PrincipalDetails principal) {
        AuthenticationUtils.validateAuthentication(principal);
        TeamAndStudyCreateResponseDto responseDto = studyService.create(requestDto, principal.getUsername());
        return ResponseEntity.created(URI.create(String.format("/new/%s", responseDto.getPostId())))
                .body(responseDto);
    }

    @ApiOperation(value = "스터디 정보 수정 API")
    @PatchMapping("/{id}")
    public ResponseEntity<TeamAndStudyCreateResponseDto> update(@PathVariable("id") Long studyId,
                                                                @AuthenticationPrincipal PrincipalDetails principal,
                                                                @RequestBody TeamAndStudyCreateRequestDto updateRequest) {
        AuthenticationUtils.validateAuthentication(principal);
        TeamAndStudyCreateResponseDto updateResponse = studyService.update(studyId, principal.getUsername(), updateRequest);
        return ResponseEntity.ok(updateResponse);
    }

    @ApiOperation(value = "모든 스터디 조회 API")
    @GetMapping("")
    public ResponseEntity<List<TeamAndStudyCreateResponseDto>> checkAllStudies() {
        List<TeamAndStudyCreateResponseDto> allStudiesResponse = studyService.checkAllStudies();
        return ResponseEntity.ok(allStudiesResponse);
    }

    @ApiOperation(value = "스터디 상세 조회")
    @GetMapping("/check/{id}")
    public ResponseEntity<TeamAndStudyCreateResponseDto> checkOne(@PathVariable("id") Long studyId) {
        TeamAndStudyCreateResponseDto responseDto = studyService.findOne(studyId);
        return ResponseEntity.ok(responseDto);
    }

    @ApiOperation(value = "회원이 생성한 스터디 조회 API")
    @GetMapping("/{id}")
    public ResponseEntity<List<TeamAndStudyCreateResponseDto>> checkMemberStudies(@PathVariable("id") String memberId,
                                                                                  @AuthenticationPrincipal PrincipalDetails principal) {
        AuthenticationUtils.validateAuthentication(principal);
        List<TeamAndStudyCreateResponseDto> allMemberStudiesResponse = studyService.checkMemberStudies(memberId, principal.getUsername());
        return ResponseEntity.ok(allMemberStudiesResponse);
    }

    @ApiOperation(value = "스터디 삭제 API")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> delete(@PathVariable("id") Long studyId,
                                         @AuthenticationPrincipal PrincipalDetails principal) {
        AuthenticationUtils.validateAuthentication(principal);
        studyService.delete(studyId, principal.getUsername());
        return ResponseEntity.ok("정상적으로 삭제되었습니다.");
    }
}
