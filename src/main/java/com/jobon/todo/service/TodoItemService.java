package com.jobon.todo.service;
/** [추가] TodoItem 서비스 인터페이스 */
import java.util.List; import com.jobon.todo.vo.TodoItemVO;
public interface TodoItemService { List<TodoItemVO> list(Long memberId,String status); TodoItemVO get(Long memberId,Long todoId); void create(TodoItemVO vo); void update(TodoItemVO vo); void delete(Long memberId,Long todoId); }
