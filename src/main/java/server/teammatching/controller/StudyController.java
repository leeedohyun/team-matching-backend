package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
                                                                @RequestParam Long memberId) {
        TeamAndStudyCreateResponseDto responseDto = studyService.create(requestDto, memberId);
        return ResponseEntity.created(URI.create(String.format("/new/%s", responseDto.getPostId())))
                .body(responseDto);
    }

    @ApiOperation(value = "스터디 정보 수정 API")
    @PatchMapping("/{id}")
    public ResponseEntity<TeamAndStudyCreateResponseDto> update(@RequestBody TeamAndStudyCreateRequestDto updateRequest,
                                                                @PathVariable("id") Long studyId) {
        TeamAndStudyCreateResponseDto updateResponse = studyService.update(updateRequest, studyId);
        return ResponseEntity.ok(updateResponse);
    }

    @ApiOperation(value = "모든 스터디 조회 API")
    @GetMapping("")
    public ResponseEntity<List<TeamAndStudyCreateResponseDto>> checkAllStudies() {
        List<TeamAndStudyCreateResponseDto> allStudiesResponse = studyService.checkAllStudies();
        return ResponseEntity.ok(allStudiesResponse);
    }

    @ApiOperation(value = "회원이 생성한 스터디 조회 API")
    @GetMapping("{id}")
    public ResponseEntity<List<TeamAndStudyCreateResponseDto>> checkMemberStudies(@PathVariable("id") Long memberId) {
        List<TeamAndStudyCreateResponseDto> allMemberStudiesResponse = studyService.checkMemberStudies(memberId);
        return ResponseEntity.ok(allMemberStudiesResponse);
    }

    @ApiOperation(value = "스터디 삭제 API")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> delete(@PathVariable("id") Long studyId) {
        studyService.delete(studyId);
        return ResponseEntity.ok("정상적으로 삭제되었습니다.");
    }
}
