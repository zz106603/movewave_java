package com.movewave.common.exception.handler;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_OCCURRED = "Error occurred: {}";

    /**
     * 잘못된 인자가 전달된 경우의 예외를 처리합니다.
     *
     * @param ex IllegalArgumentException 예외 객체
     * @return 400 Bad Request 응답
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error(ERROR_OCCURRED, ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 접근 권한이 없는 경우의 예외를 처리합니다.
     *
     * @param ex AccessDeniedException 예외 객체
     * @return 403 Forbidden 응답
     */
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
//    }

    /**
     * 요청한 리소스를 찾을 수 없는 경우의 예외를 처리합니다.
     *
     * @param ex NoSuchElementException 또는 EntityNotFoundException 예외 객체
     * @return 404 Not Found 응답
     */
    @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
    public ResponseEntity<String> handleNotFoundExceptions(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * 데이터 무결성 위반 예외를 처리합니다.
     * (예: 중복된 키 값 입력, 참조 무결성 위반 등)
     *
     * @param ex DataIntegrityViolationException 예외 객체
     * @return 409 Conflict 응답
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error(ERROR_OCCURRED, ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * 지원하지 않는 미디어 타입이 요청된 경우의 예외를 처리합니다.
     *
     * @param ex UnsupportedMediaTypeStatusException 예외 객체
     * @return 415 Unsupported Media Type 응답
     */
    @ExceptionHandler(UnsupportedMediaTypeStatusException.class)
    public ResponseEntity<String> handleUnsupportedMediaTypeStatusException(UnsupportedMediaTypeStatusException ex) {
        log.error(ERROR_OCCURRED, ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * 애플리케이션의 상태가 부적절한 경우의 예외를 처리합니다.
     * - 파일 관련 오류: 422 Unprocessable Entity
     * - 리소스 충돌: 409 Conflict
     * - 기타: 500 Internal Server Error
     *
     * @param ex IllegalStateException 예외 객체
     * @return 상황에 따른 적절한 HTTP 상태 코드와 응답
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        log.error(ERROR_OCCURRED, ex.getMessage());

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex.getMessage().contains("File")) {
            status = HttpStatus.UNPROCESSABLE_ENTITY;
        } else if (ex.getMessage().contains("Conflict")) {
            status = HttpStatus.CONFLICT;
        }
        return new ResponseEntity<>(ex.getMessage(), status);
    }

    /**
     * 처리되지 않은 모든 RuntimeException을 처리합니다.
     *
     * @param ex RuntimeException 예외 객체
     * @return 500 Internal Server Error 응답
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        log.error(ERROR_OCCURRED, ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
