package com.app.todo.repository;

import com.app.todo.entity.PreTodo;
import com.app.todo.entity.Todo;

public interface PreTodoRepositoryCustom {
    PreTodo findByPreTodoAndTodo(Todo todo, Long preTodoId);
}
