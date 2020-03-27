package com.app.todo.service;

import com.app.todo.dto.TodoDto;
import com.app.todo.entity.Todo;
import com.app.todo.exception.InvalidDeleteException;
import com.app.todo.exception.InvalidPreTodoException;
import com.app.todo.exception.NoContentException;
import com.app.todo.exception.PreconditionFailedException;
import com.app.todo.repository.TodoRepository;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TodoServiceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private TodoService service;

    @Autowired
    private TodoRepository todoRepository;

    @Test
    @Transactional
    //정상 등록 케이스
    public void createTodo_1() {
        TodoDto.Request request = new TodoDto.Request("create 테스트1", null);

        TodoDto response = service.post(request);

        assertThat(response.getTodo(), is(request.getTodo()));
        assertThat(response.isDoneStat(), is(false));
        assertNull(response.getPreTodoList());
    }

    @Test
    @Transactional
    //정상 등록 케이스 - 선행 작업 존재하는경우
    public void createTodo_2() {
        Todo todo = new Todo(null, "done 테스트", false, null, LocalDateTime.now(), LocalDateTime.now());
        todoRepository.save(todo);

        TodoDto.Request request = new TodoDto.Request("create 테스트2", Arrays.asList(todo.getId()));

        TodoDto response = service.post(request);

        assertThat(response.getTodo(), is(request.getTodo()));
        assertThat(response.isDoneStat(), is(false));
        assertThat(response.getPreTodoList().get(0).getId(), is(todo.getId()));
        assertThat(response.getPreTodoList().get(0).getTodo(), is(todo.getTodo()));
    }

    @Test(expected = InvalidPreTodoException.class)
    @Transactional
    //에러 케이스 - 존재하지 않는 id로 선행 작업 등록
    public void createTodo_3() {
        TodoDto.Request request = new TodoDto.Request("create 테스트2", Arrays.asList(99999L));
        service.post(request);
    }

    @Test
    @Transactional
    //정상 등록 케이스 - 같은 id로 여러개의 선행 작업 등록
    public void createTodo_4() {
        Todo todo = new Todo(null, "done 테스트", false, null, LocalDateTime.now(), LocalDateTime.now());
        todoRepository.save(todo);

        TodoDto.Request request = new TodoDto.Request("create 테스트2", Arrays.asList(todo.getId(), todo.getId(), todo.getId(), todo.getId()));

        TodoDto response = service.post(request);

        assertThat(response.getTodo(), is(request.getTodo()));
        assertThat(response.isDoneStat(), is(false));
        assertThat(response.getPreTodoList().size(),is(1));
    }

    @Test
    @Transactional
    //정상 수정 케이스 - todo 항목록 수정 / 선행작업 등록
    public void updateTodo_1() {
        TodoDto.Request preRequest = new TodoDto.Request("update 테스트 pre todo", null);

        TodoDto preResponse = service.post(preRequest);

        TodoDto.Request request = new TodoDto.Request("update 테스트", null);
        TodoDto response = service.post(request);

        TodoDto.Request updateRequest = new TodoDto.Request("update 테스트 수정", Arrays.asList(preResponse.getId()));
        TodoDto updateResponse = service.put(response.getId(), updateRequest);

        assertThat(updateResponse.getTodo(), is(updateRequest.getTodo()));
        assertThat(updateResponse.getPreTodoList().get(0).getId(), is(preResponse.getId()));
        assertThat(updateResponse.getPreTodoList().get(0).getTodo(), is(preResponse.getTodo()));
    }

    @Test
    @Transactional
    //정상 수정 케이스 - todo 항목록 수정 / 선행작업 삭제
    public void updateTodo_2() {
        TodoDto.Request preRequest = new TodoDto.Request("update 테스트 pre todo", null);

        TodoDto preTodoResponse = service.post(preRequest);

        TodoDto.Request request = new TodoDto.Request("update 테스트", Arrays.asList(preTodoResponse.getId()));
        TodoDto response = service.post(request);

        TodoDto.Request updateRequest = new TodoDto.Request("update 테스트 수정", null);
        TodoDto updateResponse = service.put(response.getId(), updateRequest);

        assertThat(updateResponse.getTodo(), is(updateRequest.getTodo()));
        assertNull(updateResponse.getPreTodoList());
    }

    @Test(expected = InvalidPreTodoException.class)
    @Transactional
    //에러 케이스 - 자기자신을 선행작업으로 등록한 항목을 선행작업으로 등록
    public void updateTodo_3() {
        Todo preTodo = new Todo(null, "update 테스트 pre Todo", false, null, LocalDateTime.now(), LocalDateTime.now());
        todoRepository.save(preTodo);

        TodoDto.Request request = new TodoDto.Request("create 테스트2", Arrays.asList(preTodo.getId()));
        TodoDto response = service.post(request);

        TodoDto.Request updateRequest = new TodoDto.Request("update 테스트 pre Todo", Arrays.asList(response.getId()));
        service.put(preTodo.getId(), updateRequest);
    }

    @Test(expected = InvalidPreTodoException.class)
    @Transactional
    //에러 케이스 - 자기자신을 선행작업으로 등록
    public void updateTodo_4() {
        Todo preTodo = new Todo(null, "update 테스트 pre Todo", false, null, LocalDateTime.now(), LocalDateTime.now());
        todoRepository.save(preTodo);

        TodoDto.Request updateRequest = new TodoDto.Request("update 테스트 pre Todo", Arrays.asList(preTodo.getId()));
        service.put(preTodo.getId(), updateRequest);
    }

    @Test(expected = NoContentException.class)
    @Transactional
    //에러 케이스 - 존재하지 않는 항목에 대한 수정
    public void updateTodo_5() {
        TodoDto.Request updateRequest = new TodoDto.Request("update 테스트 pre Todo", null);
        service.put(9999L, updateRequest);
    }

    @Test(expected = InvalidPreTodoException.class)
    @Transactional
    //에러 케이스 - 존재하지 않는 항목을 선행작업으로 등록
    public void updateTodo_6() {
        Todo todo = new Todo(null, "update 테스트", false, null, LocalDateTime.now(), LocalDateTime.now());
        todoRepository.save(todo);

        TodoDto.Request request = new TodoDto.Request("update 테스트", Arrays.asList(99999L));
        service.put(todo.getId(), request);
    }

    @Test
    @Transactional
    //정상 완료상태 변경 케이스 - 미완료 -> 완료
    public void doneTodo_1() {
        TodoDto.Request request = new TodoDto.Request("update 테스트", null);
        TodoDto postResponse = service.post(request);

        TodoDto doneResponse = service.patch(postResponse.getId());

        assertTrue(doneResponse.isDoneStat());
    }

    @Test
    @Transactional
    //정상 완료상태 변경 케이스 - 완료 -> 미완료
    public void doneTodo_2() {
        Todo todo = new Todo(null, "done 테스트", true, null, LocalDateTime.now(), LocalDateTime.now());
        todoRepository.save(todo);

        TodoDto doneResponse = service.patch(todo.getId());

        assertFalse(doneResponse.isDoneStat());
    }

    @Test(expected=PreconditionFailedException.class)
    @Transactional
    //에러케이스 - 선행작업이 미완료인 항목에 대해 완료처리
    public void doneTodo_3() {
        Todo todo = new Todo(null, "done 테스트", false, null, LocalDateTime.now(), LocalDateTime.now());
        todoRepository.save(todo);

        TodoDto.Request request = new TodoDto.Request("create 테스트2", Arrays.asList(todo.getId()));
        TodoDto response = service.post(request);

        service.patch(response.getId());
    }

    @Test
    @Transactional
    //정상 삭제 케이스
    public void deleteTodo_1() {
        Todo todo = new Todo(null, "delete 테스트", false, null, LocalDateTime.now(), LocalDateTime.now());
        todoRepository.save(todo);

        service.delete(todo.getId());
        assertFalse(todoRepository.findById(todo.getId()).isPresent());
    }

    @Test(expected = NoContentException.class)
    @Transactional
    //에러 케이스 - 이미 삭제된 항목에 대한 삭제 처리
    public void deleteTodo_2() {
        Todo todo = new Todo(null, "delete 테스트", false, null, LocalDateTime.now(), LocalDateTime.now());
        todoRepository.save(todo);
        todoRepository.delete(todo);

        service.delete(todo.getId());
    }

    @Test(expected = InvalidDeleteException.class)
    @Transactional
    //에러 케이스 - 자기자신을 선행학목으로 지정한 항목이 존재 하는 경우의 삭제 처리
    public void deleteTodo_3() {
        Todo preTodo = new Todo(null, "delete 테스트", false, null, LocalDateTime.now(), LocalDateTime.now());
        todoRepository.save(preTodo);

        TodoDto.Request request = new TodoDto.Request("create 테스트2", Arrays.asList(preTodo.getId()));
        service.post(request);

        service.delete(preTodo.getId());
    }

}
