package com.lemzeeyyy.taskmanagerapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.lemzeeyyy.taskmanagerapp.model.Priority;
import com.lemzeeyyy.taskmanagerapp.model.SharedViewModel;
import com.lemzeeyyy.taskmanagerapp.model.Task;
import com.lemzeeyyy.taskmanagerapp.model.TaskViewModel;
import com.lemzeeyyy.taskmanagerapp.util.Utils;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private EditText enterTodo;
    private ImageButton calendarBtn;
    private ImageButton priorityBtn;
    private RadioGroup priorityRadioGroup;
    private RadioButton selectedRadioBtn;
    private int selectedBtnId;
    private ImageButton saveBtn;
    private CalendarView calendarView;
    private Group calendarGroup;
    private Date dueDate;
    Calendar calendar = Calendar.getInstance();
    private SharedViewModel sharedViewModel;
    private Boolean isEdit = false;
    private Priority priority;

    public BottomSheetFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        calendarGroup = view.findViewById(R.id.calendar_group);
        calendarView = view.findViewById(R.id.calendar_view);
        calendarBtn = view.findViewById(R.id.today_calendar_button);
        enterTodo = view.findViewById(R.id.enter_todo_et);
        saveBtn = view.findViewById(R.id.save_todo_button);
        priorityBtn = view.findViewById(R.id.priority_todo_button);
        priorityRadioGroup = view.findViewById(R.id.radioGroup_priority);
        Chip todayChip = view.findViewById(R.id.today_chip);
        todayChip.setOnClickListener(this);
        Chip tomorrowChip = view.findViewById(R.id.tomorrow_chip);
        tomorrowChip.setOnClickListener(this);
        Chip nextWeekChip = view.findViewById(R.id.next_week_chip);
        nextWeekChip.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(sharedViewModel.getSelectedItem().getValue() != null){
            isEdit = sharedViewModel.getEdit();
            Task task = sharedViewModel.getSelectedItem().getValue();
            enterTodo.setText(task.getTask());
            Log.d("TAGResume", "onResume: "+task.getTask());
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        calendarBtn.setOnClickListener(view1 -> {
            calendarGroup.setVisibility(
                    calendarGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            Utils.hideSoftKeyboard(view1);
        });
        priorityBtn.setOnClickListener(view13 -> {
            Utils.hideSoftKeyboard(view13);

            priorityRadioGroup.setVisibility(priorityRadioGroup
                    .getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });
        priorityRadioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
           if(priorityBtn.getVisibility() == View.VISIBLE){
               selectedBtnId = checkedId;
               selectedRadioBtn = view.findViewById(selectedBtnId);
               switch (selectedBtnId){
                   case R.id.radioButton_high:
                       priority = Priority.HIGH;
                       break;
                   case R.id.radioButton_low:
                       priority = Priority.LOW;
                       break;
                   case R.id.radioButton_med:
                       priority = Priority.MEDIUM;
                       break;
                   default:
                       priority = Priority.LOW;
               }
           }else {
               priority = Priority.LOW;
           }
        });


        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            calendar.clear();
            calendar.set(year,month,dayOfMonth);
            dueDate = calendar.getTime();

        });

        saveBtn.setOnClickListener(view12 -> {
            String task = enterTodo.getText().toString().trim();
            if(!TextUtils.isEmpty(task) && dueDate!=null && priority!=null){
                Task myTast = new Task(task, priority,dueDate,
                        Calendar.getInstance().getTime(), false);
                if(isEdit){
                    Task updateTask = sharedViewModel.getSelectedItem().getValue();
                    updateTask.setTask(task);
                    updateTask.setDateCreated(Calendar.getInstance().getTime());
                    updateTask.setPriority(Priority.LOW);
                    updateTask.setDueDate(dueDate);
                    updateTask.setDone(true);
                    TaskViewModel.update(updateTask);
                    sharedViewModel.setEdit(false);
                }else {
                    TaskViewModel.insert(myTast);
                }
                enterTodo.setText("");

                if(this.isVisible()){
                    this.dismiss();
                }
            }else{
                Snackbar.make(saveBtn,R.string.empty_field,Snackbar.LENGTH_LONG)
                        .show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.today_chip:
                calendar.add(Calendar.DAY_OF_YEAR,0);
                dueDate = calendar.getTime();

                break;
            case R.id.tomorrow_chip:
                calendar.add((Calendar.DAY_OF_YEAR),1);
                dueDate = calendar.getTime();

                break;
            case R.id.next_week_chip:
                calendar.add((Calendar.DAY_OF_YEAR),7);
                dueDate = calendar.getTime();

                break;

            default:
                break;
        }

    }
}