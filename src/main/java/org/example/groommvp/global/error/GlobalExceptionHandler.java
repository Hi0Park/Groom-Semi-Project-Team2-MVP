package org.example.groommvp.global.error;

import lombok.extern.slf4j.Slf4j;
import org.example.groommvp.global.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 처리기.
 *
 * <p>컨트롤러에서 발생한 예외를 한곳에서 잡아 공통 응답 포맷({@link ApiResponse}) 으로 변환한다.
 * 각 도메인 개발자는 예외 처리를 직접 신경 쓰지 않고 {@link BusinessException} 만 던지면 된다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 비즈니스 로직에서 의도적으로 던진 예외 처리. */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("[BusinessException] {} - {}", errorCode.name(), e.getMessage());
        ErrorResponse error = ErrorResponse.of(errorCode, e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(ApiResponse.error(error));
    }

    /** {@code @Valid} 바디 검증 실패 처리. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("[ValidationException] {}", e.getMessage());
        ErrorResponse error = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return ResponseEntity.status(ErrorCode.INVALID_INPUT_VALUE.getStatus()).body(ApiResponse.error(error));
    }

    /** 그 외 예상하지 못한 모든 예외 처리. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("[UnhandledException]", e);
        ErrorResponse error = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus()).body(ApiResponse.error(error));
    }
}
