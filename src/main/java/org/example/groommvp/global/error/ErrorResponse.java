package org.example.groommvp.global.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;

/**
 * 공통 응답 포맷({@link org.example.groommvp.global.common.ApiResponse}) 의 error 필드에 들어가는 객체.
 *
 * <p>에러 코드, 메시지, HTTP 상태값을 담으며, {@code @Valid} 검증 실패처럼
 * 필드 단위 오류가 있는 경우 {@code fieldErrors} 에 상세 내역을 담는다.
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    private final String code;
    private final String message;
    private final int status;
    private final List<FieldError> fieldErrors;

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .build();
    }

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(message)
                .status(errorCode.getStatus().value())
                .build();
    }

    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .fieldErrors(FieldError.from(bindingResult))
                .build();
    }

    /** {@code @Valid} 검증 실패 시 어떤 필드가 왜 틀렸는지 담는다. */
    @Getter
    @Builder
    public static class FieldError {
        private final String field;
        private final String value;
        private final String reason;

        private static List<FieldError> from(BindingResult bindingResult) {
            return bindingResult.getFieldErrors().stream()
                    .map(error -> FieldError.builder()
                            .field(error.getField())
                            .value(error.getRejectedValue() == null ? "" : error.getRejectedValue().toString())
                            .reason(error.getDefaultMessage())
                            .build())
                    .toList();
        }
    }
}
