package com.app.todo.dto;

import com.app.todo.entity.PreTodo;
import com.app.todo.entity.Todo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler"})
public class TodoDto {
    private Long id;

    private String todo;

    private boolean doneStat;

    private List<Pre> preTodoList;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "YYYY-MM-dd")
    private LocalDateTime createdAt;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "YYYY-MM-dd")
    private LocalDateTime updatedAt;

    public static TodoDto of(Todo entity) {
        TodoDtoBuilder builder = builder();
        builder.id(entity.getId())
                .todo(entity.getTodo())
                .doneStat(entity.isDoneStat())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt());

        if(entity.getPreTodoList() != null) {
            List<Pre> preList = new ArrayList<>();
            for (PreTodo preEntity : entity.getPreTodoList()) {
                Todo preTodo = preEntity.getPreTodo();
                preList.add(Pre.builder().id(preTodo.getId())
                        .doneStat(preTodo.isDoneStat())
                        .todo(preTodo.getTodo())
                        .build());
            }
            if(preList.size() != 0) builder.preTodoList(preList);
        }

        return builder.build();
    }

    @Getter
    @AllArgsConstructor
    public static class Request {
        @NonNull
        private String todo;

        private List<Long> preTodoIdList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler"})
    public static class Pre {
        private Long id;

        private boolean doneStat;

        private String todo;
    }

    @Getter
    public static class Search {
        private Integer page;

        private Integer perPage;

        private String todo;

        private Boolean doneState;

        private String orderCondition;
    }


}
