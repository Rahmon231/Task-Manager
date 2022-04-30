package com.lemzeeyyy.taskmanagerapp.adapter;

import com.lemzeeyyy.taskmanagerapp.model.Task;

public interface OnTodoClickListener {
    void onTodoClick(int position, Task task);
    void onTodoRadioBtnClick(Task task);
    void getTask(int position,Task task);
}
