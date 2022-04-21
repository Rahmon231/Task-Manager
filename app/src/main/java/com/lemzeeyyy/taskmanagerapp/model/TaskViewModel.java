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
}
