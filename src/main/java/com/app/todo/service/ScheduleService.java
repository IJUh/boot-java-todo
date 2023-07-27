package com.app.todo.service;

import com.app.todo.dto.TodoDto;
import com.app.todo.entity.Schedule;
import com.app.todo.entity.Todo;
import com.app.todo.repository.ScheduleRepository;
import com.app.todo.vo.ScheduleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Service
public class ScheduleService {

    private final ScheduleRepository repository;

/*    @Transactional
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
    }*/

    public List<Schedule> get() {
        List<Schedule> voList = new ArrayList<>();
        for (Schedule schedule:repository.findAll()) {
            voList.add(schedule);
        }
        return voList;
    }

    public void addOneTodo(TodoDto todoDto) {
        repository.save(todoDto);
    }
}
