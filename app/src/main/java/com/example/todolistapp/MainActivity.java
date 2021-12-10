package com.example.todolistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskClickListener{

    private TaskVewModel taskVewModel;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private FloatingActionButton addButton;
    BottomSheetFragment bottomSheetFragment;
    SharedViewModel sharedViewModel;
    AlertDialog.Builder builder;

    private final static String TAG = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.add_task);
        bottomSheetFragment = new BottomSheetFragment();

        recyclerView = findViewById(R.id.recycler_vew);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        taskVewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication())
                .create(TaskVewModel.class);

        sharedViewModel = new ViewModelProvider(this)
                .get(SharedViewModel.class);

        taskVewModel.getAllTasks().observe(MainActivity.this, tasks -> {
            taskAdapter = new TaskAdapter(tasks, this);
            recyclerView.setAdapter(taskAdapter);
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();
            }
        });
    }

    public void showBottomSheet(){
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clear_all){
            TaskVewModel.deleteAllTasks();
        }
        return true;
    }

    @Override
    public void taskClick(Task task) {
        Log.d(TAG, "taskClick");
        sharedViewModel.selectItem(task);
        sharedViewModel.setEdit(true);
        showBottomSheet();
    }

    @Override
    public void radioButtonClick(Task task, RadioButton radioButton) {
        Log.d(TAG, "radioButtonClick");
         builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete")
                .setCancelable(false)
                .setMessage("Do you want to delete " + task.getTaskName() + " ?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    TaskVewModel.deleteTask(task);
                    taskAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        radioButton.setChecked(false);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }
}