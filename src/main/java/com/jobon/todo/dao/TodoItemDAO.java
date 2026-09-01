package com.jobon.todo.dao;

/**
 * [추가] 할 일 CRUD DAO */
import java.util.List;
import org.apache.ibatis.annotations.*;
import com.jobon.todo.vo.TodoItemVO;

@Mapper
public interface TodoItemDAO {
    List<TodoItemVO> selectList(@Param("memberId") Long memberId, @Param("status") String status);

    TodoItemVO selectOne(@Param("memberId") Long memberId, @Param("todoId") Long todoId);

    int insert(TodoItemVO vo);

    int update(TodoItemVO vo);

    int delete(@Param("memberId") Long memberId, @Param("todoId") Long todoId);
}
