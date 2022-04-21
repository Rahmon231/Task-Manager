package com.lemzeeyyy.taskmanagerapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.lemzeeyyy.taskmanagerapp.model.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insertTask(Task task);

    @Query("DELETE FROM task_table")
    void deleteAllTask();

    @Query("SELECT * FROM task_table")
    LiveData<List<Task>> getAllTask();

    @Query("SELECT * FROM task_table WHERE task_table.task_id== :id")
    LiveData<Task> getTask(long id);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);
}
