package com.app.todo.repository;

import com.app.todo.dto.TodoDto;
import com.app.todo.entity.Todo;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import org.springframework.data.domain.Pageable;
import java.util.List;

import static com.app.todo.entity.QTodo.todo1;

public class TodoRepositoryImpl extends QuerydslRepositorySupport implements TodoRepositoryCustom {
    public TodoRepositoryImpl() {
        super(Todo.class);
    }

    @Override
    public Page<Todo> findByTodoLike(TodoDto.Search searchDto) {
        Pageable page = PageRequest.of(searchDto.getPage(), searchDto.getPerPage());
        JPQLQuery<Todo> baseQuery = from(todo1);

        if(searchDto.getTodo() != null) {
            baseQuery.where(todo1.todo.contains(searchDto.getTodo()));
        }
        if(searchDto.getDoneState() != null) {
            baseQuery.where(todo1.doneStat.eq(searchDto.getDoneState()));
        }
        if(searchDto.getOrderCondition() != null) {
            switch (searchDto.getOrderCondition()) {
                case "idAsc":
                    baseQuery.orderBy(todo1.id.asc());
                    break;
                case "idDesc":
                    baseQuery.orderBy(todo1.id.desc());
                    break;
                case "todoAsc":
                    baseQuery.orderBy(todo1.todo.asc());
                    break;
                case "todoDesc":
                    baseQuery.orderBy(todo1.todo.desc());
                    break;
                case "createAtAsc":
                    baseQuery.orderBy(todo1.createdAt.asc());
                    break;
                case "createAtDesc":
                    baseQuery.orderBy(todo1.createdAt.desc());
                    break;
                case "updateAtAsc":
                    baseQuery.orderBy(todo1.updatedAt.asc());
                    break;
                case "updateAtDesc":
                    baseQuery.orderBy(todo1.updatedAt.desc());
                    break;
            }
        }

        List<Todo> todoList = getQuerydsl().applyPagination(page, baseQuery).fetch();
        return new PageImpl<>(todoList, page, baseQuery.fetchCount());
    }
}
