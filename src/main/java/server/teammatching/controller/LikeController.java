package server.teammatching.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.teammatching.auth.AuthenticationUtils;
import server.teammatching.auth.PrincipalDetails;
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
    public ResponseEntity<LikeResponseDto> generateLike(@AuthenticationPrincipal PrincipalDetails principal,
                                                        @RequestParam Long postId) {
        AuthenticationUtils.validateAuthentication(principal);
        LikeResponseDto responseDto = likeService.generateLike(principal.getUsername(), postId);
        return ResponseEntity.ok(responseDto);
    }

    @ApiOperation(value = "관심 목록 조회 API")
    @GetMapping("")
    public ResponseEntity<List<LikeResponseDto>> checkLikes(@AuthenticationPrincipal PrincipalDetails principal) {
        AuthenticationUtils.validateAuthentication(principal);
        List<LikeResponseDto> responseList = likeService.checkLikes(principal.getUsername());
        return ResponseEntity.ok(responseList);
    }

    @ApiOperation(value = "관심 취소 API")
    @DeleteMapping("")
    public ResponseEntity<LikeResponseDto> cancelLike(@AuthenticationPrincipal PrincipalDetails principal,
                                                      @RequestParam Long postId) {
        AuthenticationUtils.validateAuthentication(principal);
        LikeResponseDto deleteResponse = likeService.cancelLike(principal.getUsername(), postId);
        return ResponseEntity.ok(deleteResponse);
    }
}
