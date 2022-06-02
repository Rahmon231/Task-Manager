package com.lemzeeyyy.taskmanagerapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.lemzeeyyy.taskmanagerapp.adapter.OnTodoClickListener;
import com.lemzeeyyy.taskmanagerapp.adapter.RecyclerViewAdapter;
import com.lemzeeyyy.taskmanagerapp.model.Priority;
import com.lemzeeyyy.taskmanagerapp.model.SharedViewModel;
import com.lemzeeyyy.taskmanagerapp.model.Task;
import com.lemzeeyyy.taskmanagerapp.model.TaskViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
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

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTodoClickListener {
    private TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private int counter = 0;
    BottomSheetFragment bottomSheetFragment;
    private SharedViewModel sharedViewModel;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(this,R.raw.watch_me);
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
            adapter = new RecyclerViewAdapter(tasks,this,MainActivity.this);
            recyclerView.setAdapter(adapter);

            for(int i = 0 ; i < tasks.size() ; i++){
                if(tasks.get(i).getDueDate().equals(Calendar.getInstance().getTime())){
                    mediaPlayer.start();
                   }
            }

        });
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            showButtomSheetDialog();
        });
    }

    private void playMedia() {
        mediaPlayer.start();
    }

    private void showButtomSheetDialog() {
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
         SharedPreferences preferences = getSharedPreferences("shared_pref",MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        final boolean isLightMode = preferences.getBoolean("isLightMode",true);
        editor.apply();
        if(isLightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
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
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }
        else {
            SharedPreferences preferences = getSharedPreferences("shared_pref",MODE_PRIVATE);
            final SharedPreferences.Editor editor = preferences.edit();
            final boolean isLightMode = preferences.getBoolean("isLightMode",true);
            editor.apply();
            if(isLightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            if(isLightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("isLightMode",false);
                editor.apply();
                item.setTitle("dark mode");


            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("isLightMode",true);
                editor.apply();
                item.setTitle("dark mode");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTodoClick(int position, Task task) {
        Log.d("Clicked", "onTodoClick: "+Calendar.getInstance().getTime());
        sharedViewModel.setEdit(true);
        sharedViewModel.selectItem(task);
        showButtomSheetDialog();

    }

    @Override
    public void onTodoRadioBtnClick( Task task) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Do you want to delete this task?")
                .setPositiveButton("Delete", (dialogInterface, i) -> TaskViewModel.delete(task))
                .setNegativeButton("Cancel",null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getTask(int position, Task task) {
        Log.d("taggettasked", "getTask: "+task.getTask());
    }

}