package server.teammatching.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import server.teammatching.dto.response.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFoundException(MemberNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.CONFLICT)
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePostNotFoundException(PostNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(RecruitNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRecruitNotFoundException(RecruitNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(LikeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLikeNotFoundException(LikeNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(ApplicationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleApplicationNotFoundException(ApplicationNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.UNAUTHORIZED)
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(RecruitmentCompletedException.class)
    public ResponseEntity<ErrorResponse> handleRecruitmentCompletedException(RecruitmentCompletedException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.CONFLICT)
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(AlreadyApplicationException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyApplicationException(AlreadyApplicationException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.CONFLICT)
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(InsufficientMembersException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientMembersException(InsufficientMembersException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest()
                .body(ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.FORBIDDEN)
                        .message(e.getMessage())
                        .build());
    }
}
