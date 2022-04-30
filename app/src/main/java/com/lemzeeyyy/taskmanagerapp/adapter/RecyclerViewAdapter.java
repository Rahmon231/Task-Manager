package com.lemzeeyyy.taskmanagerapp.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.lemzeeyyy.taskmanagerapp.R;
import com.lemzeeyyy.taskmanagerapp.model.Task;
import com.lemzeeyyy.taskmanagerapp.util.Utils;

import java.util.Calendar;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final List<Task> taskList;
    private final OnTodoClickListener todoClickListener;
    private MediaPlayer mediaPlayer;


    public RecyclerViewAdapter(List<Task> taskList, OnTodoClickListener todoClickListener, Context context) {
        this.taskList = taskList;
        this.todoClickListener = todoClickListener;
        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(context,R.raw.watch_me);


    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.todo_task.setText(task.getTask());
        String formatted = Utils.formatDate(task.getDueDate());
        ColorStateList colorStateList = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.enabled},
                new int[]{android.R.attr.enabled}
        },
                new int[]{
                        Utils.priorityColor(task),
                        Color.LTGRAY

        });
        holder.todayChip.setText(formatted);
        holder.todayChip.setTextColor(Utils.priorityColor(task));
        holder.todayChip.setChipIconTint(colorStateList);
        holder.radioButton.setButtonTintList(colorStateList);

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public AppCompatRadioButton radioButton;
        public AppCompatTextView todo_task;
        public Chip todayChip;
        OnTodoClickListener onTodoClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.todo_radio_button);
            todo_task =itemView.findViewById(R.id.todo_row_todo);
            todayChip = itemView.findViewById(R.id.todo_row_chip);
            this.onTodoClickListener = todoClickListener;
            itemView.setOnClickListener(this);
            radioButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Task currTask = taskList.get(getAdapterPosition());
            int id = view.getId();
        if(id == R.id.todo_row_layout){
            onTodoClickListener.onTodoClick(getAdapterPosition(),currTask);
        } else if (id == R.id.todo_radio_button){

            onTodoClickListener.onTodoRadioBtnClick(currTask);
            }
        }
    }
}
