package com.lemzeeyyy.taskmanagerapp;

import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.lemzeeyyy.taskmanagerapp.adapter.OnTodoClickListener;
import com.lemzeeyyy.taskmanagerapp.adapter.RecyclerViewAdapter;
import com.lemzeeyyy.taskmanagerapp.model.Priority;
import com.lemzeeyyy.taskmanagerapp.model.Task;
import com.lemzeeyyy.taskmanagerapp.model.TaskViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTodoClickListener {
    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private int counter = 0;
    BottomSheetFragment bottomSheetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bottomSheetFragment = new BottomSheetFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.bottomSheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);
        taskViewModel = new ViewModelProvider
                .AndroidViewModelFactory(MainActivity.this.getApplication())
                .create(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, tasks -> {

            adapter = new RecyclerViewAdapter(tasks,this);
            recyclerView.setAdapter(adapter);
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//            Task task = new Task("Task "+counter++, Priority.HIGH, Calendar.getInstance().getTime(),
//                    Calendar.getInstance().getTime(),false);
//           TaskViewModel.insert(task);

            showButtomSheetDialog();
        });
    }

    private void showButtomSheetDialog() {
        bottomSheetFragment.show(getSupportFragmentManager(),bottomSheetFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTodoClick(int position, Task task) {

    }

    @Override
    public void onTodoRadioBtn(Task task) {
        TaskViewModel.delete(task);
    }
}