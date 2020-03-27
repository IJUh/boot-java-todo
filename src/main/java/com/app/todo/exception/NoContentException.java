package com.app.todo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoContentException extends RuntimeException {
}
