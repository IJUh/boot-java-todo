package com.app.todo.vo;

import com.app.todo.entity.Todo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ScheduleVO {
    private List<Todo> todoList;

    public static ScheduleVO of(Todo todo) {
        return new ScheduleVO(todo);
    }
}
