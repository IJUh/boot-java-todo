package com.app.todo.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreTodo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(columnDefinition = "int comment 'id'")
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", referencedColumnName = "id", columnDefinition = "int comment 'todo 고유아이디'")
    private Todo todo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pre_todo_id", referencedColumnName = "id", columnDefinition = "int comment 'todo 고유아이디'")
    private Todo preTodo;

    @Column(nullable = false, columnDefinition = "timestamp not null default current_timestamp comment '생성일시'")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "timestamp null on update current_timestamp comment '수정일시'")
    private LocalDateTime updatedAt;

    public PreTodo(Todo todo, Todo preTodo) {
        this.todo = todo;
        this.preTodo = preTodo;
        this.createdAt = LocalDateTime.now();
    }

    public PreTodo(Todo preTodo) {
        this.preTodo = preTodo;
        this.createdAt = LocalDateTime.now();
    }
}
