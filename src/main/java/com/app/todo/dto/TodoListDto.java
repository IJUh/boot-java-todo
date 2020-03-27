package com.app.todo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler"})
public class TodoListDto {
    private List<TodoDto> todoList;

    private Integer currentPage;

    private Integer totalPage;

    public static TodoListDto of(List<TodoDto> todoList, Integer currentPage, Integer totalPage) {
        return TodoListDto.builder()
                .todoList(todoList)
                .currentPage(currentPage)
                .totalPage(totalPage)
                .build();
    }
}
