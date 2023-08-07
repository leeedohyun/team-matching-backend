package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.teammatching.dto.response.LikeResponseDto;
import server.teammatching.service.LikeService;

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
}
