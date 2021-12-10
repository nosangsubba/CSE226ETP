package com.example.todolistapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Database;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private EditText taskNameEt;
    private CalendarView calendarView;
    private Button saveButton;
    Calendar calendar = Calendar.getInstance();
    private Date dueDate;
    private boolean isEdit;
    private SharedViewModel sharedViewModel;
    private NumberPicker numberPicker;
    Integer priority;
    private EditText description;

    public BottomSheetFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.bottom_sheet,container, false);

        taskNameEt = view.findViewById(R.id.add_task);
        calendarView = view.findViewById(R.id.calendar_view);
        saveButton = view.findViewById(R.id.save_button);
        numberPicker = view.findViewById(R.id.priority_picker);
        description = view.findViewById(R.id.descriptionEt);

        taskNameEt.setText("");

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(10);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedViewModel.getSelectedItem().getValue() != null){
            isEdit = sharedViewModel.isEdit();
            Task task = sharedViewModel.getSelectedItem().getValue();
            taskNameEt.setText(task.getTaskName());
            numberPicker.setValue(task.getPriority());
            description.setText(task.getDescription());
            calendarView.setDate(Converter.dateToTimeStamp(task.getDueDate()));
        }
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            calendar.clear();
            calendar.set(year, month, dayOfMonth);
            dueDate = calendar.getTime();
        });
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                priority = picker.getValue();
            }
        });

        saveButton.setOnClickListener(v -> {
            String taskString = taskNameEt.getText().toString();
            String descriptionString = description.getText().toString();
            if (!TextUtils.isEmpty(taskString) && dueDate != null){
                Task myTask = new Task(taskString, dueDate,priority,descriptionString, false);
                Log.d("check", "onClick:  saved");
                if (isEdit){
                    Task updateTask = sharedViewModel.getSelectedItem().getValue();
                    updateTask.setTaskName(taskString);
                    updateTask.setDueDate(dueDate);
                    updateTask.setPriority(priority);
                    updateTask.setDescription(descriptionString);
                    updateTask.setCompleted(false);
                    sharedViewModel.setEdit(false);
                    TaskVewModel.updateTask(updateTask);
                }
                else {
                    TaskVewModel.insertTask(myTask);
                }
                taskNameEt.setText("");
                if (this.isVisible()){
                    this.dismiss();
                }
            }

        });
    }
}
