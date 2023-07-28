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

@RestController
@RequiredArgsConstructor
@RequestMapping("/studies")
@Api(tags = {"스터디 API"})
public class StudyController {

    private final StudyService studyService;

    @ApiOperation(value = "스터디 생성 API")
    @PostMapping("/new")
    public ResponseEntity create(@RequestBody TeamAndStudyCreateRequestDto requestDto, @RequestParam Long memberId) {
        TeamAndStudyCreateResponseDto responseDto = studyService.create(requestDto, memberId);
        return ResponseEntity.created(URI.create(String.format("/new/%s", responseDto.getPostId())))
                .body(requestDto);
    }

    @ApiOperation(value = "스터디 정보 수정 API")
    @PatchMapping("/{id}")
    public ResponseEntity update(@RequestBody TeamAndStudyCreateRequestDto updateRequest, @PathVariable("id") Long studyId) {
        TeamAndStudyCreateResponseDto updateResponse = studyService.update(updateRequest, studyId);
        return ResponseEntity.ok(updateResponse);
    }
}
