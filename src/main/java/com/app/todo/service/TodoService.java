package com.app.todo.service;

import com.app.todo.dto.TodoDto;
import com.app.todo.dto.TodoListDto;
import com.app.todo.entity.PreTodo;
import com.app.todo.entity.Todo;
import com.app.todo.exception.InvalidDeleteException;
import com.app.todo.exception.InvalidPreTodoException;
import com.app.todo.exception.NoContentException;
import com.app.todo.exception.PreconditionFailedException;
import com.app.todo.repository.PreTodoRepository;
import com.app.todo.repository.TodoRepository;
import com.app.todo.utils.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoRepository repository;
    private final PreTodoRepository preRepository;

    @Transactional
    public TodoDto post(TodoDto.Request request) {
        Todo todo = new Todo(request);

        List<Long> preTodoIdList = request.getPreTodoIdList();
        if (!CommonUtil.isNull(preTodoIdList)) {
            List<PreTodo> preTodoList = checkAndSetPreTodo(preTodoIdList);
            todo.setPreTodoList(preTodoList);
        }

        repository.save(todo);
        return TodoDto.of(todo);
    }

    @Transactional
    public TodoDto put(Long id, TodoDto.Request request) {
        Todo todo = getTodoById(id);
        todo.update(request);

        if(!CommonUtil.isNull(todo.getPreTodoList())) {
            preRepository.deleteInBatch(todo.getPreTodoList());
        }

        List<Long> preTodoIdList = request.getPreTodoIdList();
        if (!CommonUtil.isNull(preTodoIdList)) {
            List<PreTodo> preTodoList = checkAndSetPreTodo(preTodoIdList, todo);
            todo.setPreTodoList(preTodoList);
        }

        repository.save(todo);
        return TodoDto.of(todo);
    }

    private Todo getTodoById(Long id) {
        return repository.findById(id).orElseThrow(NoContentException::new);
    }

    private Todo getTodoByPreTodoId(Long preTodoId) {
        return repository.findById(preTodoId).orElseThrow(InvalidPreTodoException::new);
    }

    private List<PreTodo> checkAndSetPreTodo(List<Long> preTodoIdList) {
        return checkAndSetPreTodo(preTodoIdList, null);
    }

    private List<PreTodo> checkAndSetPreTodo(List<Long> preTodoIdList, Todo todo) {
        List<PreTodo> preTodoList = new ArrayList<>();
        for (Long preTodoId : new HashSet<>(preTodoIdList)) {
            Todo preTodo = getTodoByPreTodoId(preTodoId);
            if (!CommonUtil.isNull(todo))
                preTodoIdCheck(preTodoId, todo);
            preTodoList.add(new PreTodo(preTodo));
        }
        return preTodoList;
    }

    private void preTodoIdCheck(Long preId, Todo todo) {
        if (preId.equals(todo.getId()))
            throw new InvalidPreTodoException();
        if (!CommonUtil.isNull(preRepository.findByPreTodoAndTodo(todo, preId)))
            throw new InvalidPreTodoException();
    }

    public TodoListDto get(TodoDto.Search searchDto) {
        Page<Todo> page = repository.findByTodoLike(searchDto);

        List<TodoDto> dtoList = new ArrayList<>();
        for (Todo todoEntity : page.getContent() ) {
            dtoList.add(TodoDto.of(todoEntity));
        }

        return TodoListDto.of(
                dtoList
                , searchDto.getPage()+1
                , page.getTotalPages()
        );
    }

    public TodoDto get(Long id) {
        return TodoDto.of(getTodoById(id));
    }

    @Transactional
    public TodoDto patch(Long id) {
        Todo todo = getTodoById(id);

        childTodoCheck(preRepository.findByPreTodo(todo));

        List<PreTodo> preTodoList = todo.getPreTodoList();
        if(!CommonUtil.isNull(preTodoList))
            parentsCheck(preTodoList);

        todo.done();
        repository.save(todo);
        return TodoDto.of(todo);
    }

    private void childTodoCheck(List<PreTodo> preTodoList) {
        for (PreTodo preTodo : preTodoList) {
            if(preTodo.getTodo().isDoneStat())
                throw new PreconditionFailedException();
        }
    }

    private void parentsCheck(List<PreTodo> preTodoList) {
        for (PreTodo preTodo : preTodoList) {
            if (!preTodo.getPreTodo().isDoneStat())
                throw new PreconditionFailedException();
        }
    }

    @Transactional
    public void delete(Long id) {
        Todo todo = getTodoById(id);
        if(!CommonUtil.isNull(preRepository.findByPreTodo(todo)))
            throw new InvalidDeleteException();

        repository.delete(todo);
    }

}
