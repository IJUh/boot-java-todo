package com.app.todo.repository;

import com.app.todo.entity.PreTodo;
import com.app.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreTodoRepository extends JpaRepository<PreTodo, Long> , PreTodoRepositoryCustom{
    List<PreTodo> findByPreTodo(Todo preTodo);
}
