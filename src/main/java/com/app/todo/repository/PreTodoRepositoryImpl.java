package com.app.todo.repository;

import com.app.todo.entity.PreTodo;
import com.app.todo.entity.Todo;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import static com.app.todo.entity.QPreTodo.preTodo1;

public class PreTodoRepositoryImpl extends QuerydslRepositorySupport implements PreTodoRepositoryCustom {
    public PreTodoRepositoryImpl() {
        super(Todo.class);
    }

    @Override
    public PreTodo findByPreTodoAndTodo(Todo todo, Long preTodoId) {
        return from(preTodo1)
                .where(
                        preTodo1.preTodo.id.eq(todo.getId())
                        .and(preTodo1.todo.id.eq(preTodoId))
                )
                .fetchFirst();
    }
}
