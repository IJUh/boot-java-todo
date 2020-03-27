package com.app.todo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class PreconditionFailedException extends RuntimeException {
}
