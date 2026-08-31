package com.jobon.todo.service;
/** [추가] TodoItem CRUD 비즈니스 로직 */
import java.util.List; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import com.jobon.todo.dao.TodoItemDAO; import com.jobon.todo.vo.TodoItemVO;
@Service public class TodoItemServiceImpl implements TodoItemService {
 private final TodoItemDAO dao; public TodoItemServiceImpl(TodoItemDAO dao){this.dao=dao;}
 public List<TodoItemVO> list(Long memberId,String status){return dao.selectList(memberId, status);}
 public TodoItemVO get(Long memberId,Long todoId){ TodoItemVO v=dao.selectOne(memberId,todoId); if(v==null) throw new IllegalArgumentException("데이터를 찾을 수 없습니다."); return v;}
 @Transactional public void create(TodoItemVO vo){validate(vo); if(dao.insert(vo)!=1) throw new IllegalStateException("등록에 실패했습니다.");}
 @Transactional public void update(TodoItemVO vo){validate(vo); if(dao.update(vo)!=1) throw new IllegalStateException("수정에 실패했습니다.");}
 @Transactional public void delete(Long memberId,Long todoId){if(dao.delete(memberId,todoId)!=1) throw new IllegalArgumentException("삭제할 데이터를 찾을 수 없습니다.");}
 private void validate(TodoItemVO vo){ if(vo==null || vo.getMemberId()==null) throw new IllegalArgumentException("로그인 정보가 없습니다."); if(vo.getTitle()==null||vo.getTitle().isBlank()) throw new IllegalArgumentException("할 일을 입력해주세요."); if(vo.getPriority()==null||vo.getPriority().isBlank()) vo.setPriority("MEDIUM"); if(vo.getStatus()==null||vo.getStatus().isBlank()) vo.setStatus("TODO"); }
}
