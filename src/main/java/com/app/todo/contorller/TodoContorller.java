package com.app.todo.contorller;

import com.app.todo.dto.TodoDto;
import com.app.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/todo")
public class TodoContorller {

    private final TodoService service;

    @PostMapping
    public ResponseEntity<?> post(@RequestBody @Valid TodoDto.Request request) {
        return new ResponseEntity<>(service.post(request), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return new ResponseEntity<>(service.get(id), HttpStatus.OK);
    }

    @PostMapping(value = "/list")
    public ResponseEntity<?> get(@RequestBody TodoDto.Search searchDto) {
        return new ResponseEntity<>(service.get(searchDto), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> put(@PathVariable Long id, @RequestBody @Valid TodoDto.Request request) {
        return new ResponseEntity<>(service.put(id, request), HttpStatus.OK);
    }

    @PutMapping(value = "/done/{id}")
    public ResponseEntity<?> patch(@PathVariable Long id) {
        service.patch(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
