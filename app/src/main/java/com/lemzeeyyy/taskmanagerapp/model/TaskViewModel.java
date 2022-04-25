package com.lemzeeyyy.taskmanagerapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lemzeeyyy.taskmanagerapp.data.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    public static TaskRepository repository;
    public final LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllData();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }
    public LiveData<Task> getTask(long id){
        return repository.getTask(id);
    }
    public static void update(Task task){
        repository.updateTask(task);
    }
    public static void delete(Task task){
        repository.deleteTask(task);
    }
    public static void deleteAll(){
        repository.deleteAll();
    }
    public static void insert(Task task){
        repository.insertTask(task);
    }
}
