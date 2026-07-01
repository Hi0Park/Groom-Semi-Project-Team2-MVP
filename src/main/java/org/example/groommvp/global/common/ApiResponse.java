package org.example.groommvp.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.example.groommvp.global.error.ErrorResponse;

/**
 * 프로젝트 전역에서 사용하는 공통 응답 포맷.
 *
 * <p>모든 API 는 이 포맷으로 감싸서 응답한다. 성공/실패 여부를 {@code success} 로 구분하고,
 * 성공 시에는 {@code data} 에 실제 결과를, 실패 시에는 {@code error} 에 에러 정보를 담는다.
 *
 * <pre>
 * // 성공 예시
 * { "success": true, "data": { ... } }
 *
 * // 실패 예시 (GlobalExceptionHandler 가 생성)
 * { "success": false, "error": { "code": "PRODUCT_NOT_FOUND", "message": "...", "status": 404 } }
 * </pre>
 *
 * @param <T> 응답 데이터 타입
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final ErrorResponse error;

    private ApiResponse(boolean success, T data, ErrorResponse error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    /** 데이터를 담아 성공 응답을 생성한다. */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    /** 반환할 데이터가 없는 성공 응답을 생성한다. (예: 삭제, 단순 처리 완료) */
    public static ApiResponse<Void> success() {
        return new ApiResponse<>(true, null, null);
    }

    /** 에러 정보를 담아 실패 응답을 생성한다. (주로 GlobalExceptionHandler 에서 사용) */
    public static ApiResponse<Void> error(ErrorResponse error) {
        return new ApiResponse<>(false, null, error);
    }
}
