package com.hobbyhop.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;


@Getter
public class ApiResponse<T> {

    private boolean success = false;
    private HttpStatus httpStatus;
    private Results result;

    public ApiResponse(HttpStatus httpStatus, Results result) {
        this.httpStatus = httpStatus;
        if (httpStatus.is2xxSuccessful()) {
            this.success = true;
        }
        this.result = result;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<T>(
                HttpStatus.OK,
                new Results(data, null)
        );
    }

    public static <T> ApiResponse<T> fail(ErrorResponse errorResponse) {
        return new ApiResponse<T>(
                HttpStatus.valueOf(errorResponse.getStatusCode().value()),
                new Results(null, errorResponse)
        );
    }
}
