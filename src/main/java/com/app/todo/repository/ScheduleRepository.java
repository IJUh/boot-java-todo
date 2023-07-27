package com.app.todo.repository;

import com.app.todo.dto.TodoDto;
import com.app.todo.entity.Schedule;
import com.app.todo.entity.Todo;
import com.app.todo.vo.ScheduleVO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, ScheduleRepositoryCustom {
}
