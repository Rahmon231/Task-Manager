package com.lemzeeyyy.taskmanagerapp.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.lemzeeyyy.taskmanagerapp.model.Task;
import com.lemzeeyyy.taskmanagerapp.util.TaskRoomDatabase;

import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskRepository(Application application) {
        TaskRoomDatabase roomDatabase = TaskRoomDatabase.getDatabase(application);
        taskDao = roomDatabase.taskDao();
        allTasks = taskDao.getAllTask();
    }
    public LiveData<List<Task>> getAllData(){
        return allTasks;
    }
    public void insertTask(Task task){
        TaskRoomDatabase.databaseWriteExecutor.execute(()->{
            taskDao.insertTask(task);
        });
    }
    public void deleteAll(){
        TaskRoomDatabase.databaseWriteExecutor.execute(()->{
            taskDao.deleteAllTask();
        });

    }
    public void deleteTask(Task task){
        TaskRoomDatabase.databaseWriteExecutor.execute(()->{
            taskDao.deleteTask(task);
        });
    }
    public LiveData<Task> getTask(long id){
        return taskDao.getTask(id);
    }
    public void updateTask(Task task){
        TaskRoomDatabase.databaseWriteExecutor.execute(()->{
            taskDao.updateTask(task);
        });
    }
}
