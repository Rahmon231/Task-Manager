package com.lemzeeyyy.taskmanagerapp.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.lemzeeyyy.taskmanagerapp.data.TaskDao;
import com.lemzeeyyy.taskmanagerapp.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class TaskRoomDatabase extends RoomDatabase {
    public static final int NUMBER_OF_THREADS = 4;
    public static final String TASK_DATABASE = "task_database";
    public static volatile TaskRoomDatabase INSTANCE;

    public static final ExecutorService databaseWriteExecutor =
             Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static final RoomDatabase.Callback sRoomDatabase =
            new RoomDatabase.Callback(){
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    databaseWriteExecutor.execute(()->{
                        //invoke dao
                        TaskDao taskDao = INSTANCE.taskDao();
                        taskDao.deleteAllTask();
                    });
                }
            };

    public static TaskRoomDatabase getDatabase(Context context){
        if(INSTANCE == null){
            synchronized (TaskRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskRoomDatabase.class, TASK_DATABASE)
                            .addCallback(sRoomDatabase)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    public abstract TaskDao taskDao();
}
