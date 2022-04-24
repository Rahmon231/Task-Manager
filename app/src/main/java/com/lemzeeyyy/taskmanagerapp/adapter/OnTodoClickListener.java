package com.lemzeeyyy.taskmanagerapp.adapter;

import com.lemzeeyyy.taskmanagerapp.model.Task;

public interface OnTodoClickListener {
    void onTodoClick(int position, Task task);
    void onTodoRadioBtn(Task task);
}
