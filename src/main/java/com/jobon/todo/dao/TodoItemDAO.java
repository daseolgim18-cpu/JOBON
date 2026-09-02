package com.jobon.todo.dao;

/**
 * [추가] 할 일 CRUD DAO */
import java.util.List;
import java.time.LocalDate;
import org.apache.ibatis.annotations.*;
import com.jobon.todo.vo.TodoItemVO;

@Mapper
public interface TodoItemDAO {
    List<TodoItemVO> selectList(@Param("memberId") Long memberId, @Param("status") String status);

    TodoItemVO selectOne(@Param("memberId") Long memberId, @Param("todoId") Long todoId);

    int insert(TodoItemVO vo);

    int update(TodoItemVO vo);

    int delete(@Param("memberId") Long memberId, @Param("todoId") Long todoId);

    // [추가] 공고 마감일 변경 시 해당 공고에 연결된 미완료 TODO를 동기화합니다.
    int updateDueDateByJobId(@Param("memberId") Long memberId,
            @Param("jobId") Long jobId, @Param("dueDate") LocalDate dueDate);
}
