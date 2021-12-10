package com.example.todolistapp;

import android.widget.RadioButton;

public interface TaskClickListener {
    void taskClick(Task task);
    void radioButtonClick(Task task, RadioButton radioButton);
}
