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

    int countOwnedCompany(@Param("memberId") Long memberId, @Param("companyId") Long companyId);

    int countOwnedJob(@Param("memberId") Long memberId, @Param("jobId") Long jobId);

    TodoItemVO selectAutoApplicationTodo(@Param("memberId") Long memberId, @Param("marker") String marker);

    int deleteAutoApplicationTodo(@Param("memberId") Long memberId, @Param("marker") String marker);

    int update(TodoItemVO vo);

    // [추가] 다른 TODO 정보는 건드리지 않고 상태만 DONE으로 완료 처리합니다.
    int complete(@Param("memberId") Long memberId, @Param("todoId") Long todoId);

    int delete(@Param("memberId") Long memberId, @Param("todoId") Long todoId);

    // [추가] 공고 마감일 변경 시 해당 공고에 연결된 미완료 TODO를 동기화합니다.
    int updateDueDateByJobId(@Param("memberId") Long memberId,
            @Param("jobId") Long jobId, @Param("dueDate") LocalDate dueDate);
}
