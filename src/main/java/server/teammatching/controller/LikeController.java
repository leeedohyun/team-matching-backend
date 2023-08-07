package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.teammatching.dto.response.LikeResponseDto;
import server.teammatching.service.LikeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"관심 API"})
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    @ApiOperation(value = "관심 등록 API")
    @PostMapping("")
    public ResponseEntity<LikeResponseDto> generateLike(@RequestParam Long memberId, @RequestParam Long postId) {
        LikeResponseDto responseDto = likeService.generateLike(memberId, postId);
        return ResponseEntity.ok(responseDto);
    }

    @ApiOperation(value = "관심 목록 조회 API")
    @GetMapping("")
    public ResponseEntity<List<LikeResponseDto>> checkLikes(@RequestParam Long memberId) {
        List<LikeResponseDto> responseList = likeService.checkLikes(memberId);
        return ResponseEntity.ok(responseList);
    }
}
