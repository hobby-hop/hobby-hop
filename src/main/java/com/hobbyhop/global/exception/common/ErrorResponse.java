package com.hobbyhop.global.exception.common;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ErrorResponse {

  private int status;

  private List<String> errorMessages;

  public static ErrorResponse of(HttpStatus status, List<String> errorMessages) {
    return ErrorResponse.builder()
        .status(status.value())
        .errorMessages(errorMessages)
        .build();
  }

  public static ErrorResponse of(HttpStatus status, String errorMessage) {
    List<String> errorMessages = List.of(errorMessage);

    return ErrorResponse.builder()
        .status(status.value())
        .errorMessages(errorMessages)
        .build();
  }
}
