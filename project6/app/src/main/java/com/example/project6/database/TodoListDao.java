package com.example.project6.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
@Dao
public interface TodoListDao {
    @Query("SELECT * FROM todo")
    List<TodoListEntity> loadAll();

    @Insert
    long addTodo(TodoListEntity entity);

    @Query("DELETE FROM todo")
    void deleteAll();

    @Delete
    void deleteRecord(TodoListEntity... onetodo);

    @Query("SELECT * FROM todo WHERE content=:content")
    TodoListEntity getEntity(String content);

    @Update
    void updateEntity(TodoListEntity... todos);

    @Query("SELECT * FROM todo WHERE isFinished=:isFinished")
    List<TodoListEntity> loadAll(boolean isFinished);
}
