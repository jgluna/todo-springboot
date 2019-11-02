package com.github.jgluna.todo.domain;

import org.springframework.http.HttpStatus;

public class TaskApplicationException extends Throwable {
  private final HttpStatus status;
  private final String message;

  TaskApplicationException(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }

  public HttpStatus getStatus() {
    return status;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
