package com.hobbyhop.global.response;

import lombok.Getter;
import org.springframework.web.ErrorResponse;

@Getter
public class Results< T > {
    private final T data;
    private final ErrorResponse errorResponse;

    public Results( T data, ErrorResponse errorResponse ) {
        this.data = data;
        this.errorResponse = errorResponse;
    }
}
