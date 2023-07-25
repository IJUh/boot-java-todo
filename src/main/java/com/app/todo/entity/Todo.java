package com.app.todo.entity;

import com.app.todo.dto.TodoDto;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(columnDefinition = "int comment 'id'")
    private Long id;

    @Column(columnDefinition = "varchar comment '생성일시'")
    private String todo;

    @Column(columnDefinition = "boolean default false comment '생성일시'")
    private boolean doneStat = false;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PreTodo> preTodoList;

    @Column(nullable = false, columnDefinition = "timestamp not null default current_timestamp comment '생성일시'")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "timestamp null on update current_timestamp comment '수정일시'")
    private LocalDateTime updatedAt;
    @Column(columnDefinition = "timestamp not null default current_timestamp comment '시작일'")
    private LocalDateTime startDate;
    @Column(columnDefinition = "timestamp null on update current_timestamp comment '종료일'")
    private LocalDateTime endDate;

    public Todo(TodoDto.Request request) {
        this.todo = request.getTodo();
        this.createdAt = LocalDateTime.now();
    }

    public Todo(TodoDto todo) {
        this.todo = todo.getTodo();
        this.doneStat = todo.isDoneStat();
        this.createdAt = todo.getCreatedAt();
        this.updatedAt = todo.getUpdatedAt();
    }

    public void update(TodoDto.Request request) {
        this.todo = request.getTodo();
        this.preTodoList = null;
        this.updatedAt = LocalDateTime.now();
    }

    public void done() {
        this.doneStat = !this.doneStat;
        this.updatedAt = LocalDateTime.now();
    }

    public void setPreTodoList(List<PreTodo> preTodoList) {
        for (PreTodo preTodo : preTodoList) {
            preTodo.setTodo(this);
        }
        this.preTodoList = preTodoList;
    }
}
