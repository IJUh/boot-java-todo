package com.app.todo.contorller;

import java.io.*;
import java.util.List;

import com.app.todo.dto.TodoDto;
import com.app.todo.service.BackUpService;
import com.app.todo.utils.FileUtils;
import com.app.todo.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/backup")
@Slf4j
public class BackUpController {

    private final BackUpService service;

    @GetMapping(value = "/download")
    public ResponseEntity<?> backUpFileDownLoad(HttpServletResponse response) throws Exception {
        List<TodoDto> dto = service.get();

        FileUtils.createTextFile(response, "myTodoList", JsonUtils.writeValueAsPrettyString(dto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value="/upload")
    public ResponseEntity<?> backUpFileUpLoad(@RequestParam MultipartFile multipartFile) throws Exception {
        File file = FileUtils.convertToFile(multipartFile);
        List<TodoDto> todoDtoList = JsonUtils.readValue(file, new TypeReference<List<TodoDto>>(){});

        service.backup(todoDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
