package com.lemzeeyyy.taskmanagerapp.model;

import android.app.Application;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lemzeeyyy.taskmanagerapp.R;
import com.lemzeeyyy.taskmanagerapp.data.TaskRepository;

import java.util.Calendar;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    public static TaskRepository repository;
    public final LiveData<List<Task>> allTasks;
    private MediaPlayer mediaPlayer;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllData();
        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(application, R.raw.watch_me);

    }

    public LiveData<List<Task>> getAllTasks() {
        Log.d("TagGetDueDate", "getAllTasks: "+allTasks.getValue());
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
