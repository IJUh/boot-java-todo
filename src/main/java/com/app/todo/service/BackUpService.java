package com.app.todo.service;

import com.app.todo.dto.TodoDto;
import com.app.todo.entity.PreTodo;
import com.app.todo.entity.Todo;
import com.app.todo.repository.PreTodoRepository;
import com.app.todo.repository.TodoRepository;
import com.app.todo.utils.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BackUpService {

    private final TodoRepository repository;
    private final PreTodoRepository preRepository;

    @Transactional
    public void backup(List<TodoDto> todoDtoList) {
        Map<Long,Todo> todoMap = todoDtoList.stream().collect(Collectors.toMap(TodoDto::getId, Todo::new));
        repository.saveAll(todoMap.values());

        for (TodoDto dto : todoDtoList) {
            if(!CommonUtil.isNull(dto.getPreTodoList())) {
                for (TodoDto.Pre pre : dto.getPreTodoList()) {
                    preRepository.save(new PreTodo(todoMap.get(dto.getId()), todoMap.get(pre.getId())));
                }
            }
        }
    }

    public List<TodoDto> get() {
        List<TodoDto> dtoList = new ArrayList<>();
        for (Todo todoEntity:repository.findAll()) {
            dtoList.add(TodoDto.of(todoEntity));
        }
        return dtoList;
    }
}
