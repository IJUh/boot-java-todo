package com.app.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String scheduleId;

    // 각 스케쥴 테이블은 여러개의 todo를 가지고 있어
    @Getter
    @OneToMany
    @Column(columnDefinition = "todo리스트")
    private List<Todo> todoList;

    public Schedule() {

    }
}
