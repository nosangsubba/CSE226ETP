package com.example.todolistapp;

import android.hardware.lights.LightState;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.Date;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private List<Task> taskList;
    private final String TAG = "recycler";
    private TaskClickListener taskClickListener;

    public TaskAdapter(List<Task> taskList, TaskClickListener taskClickListener) {
        this.taskList = taskList;
        this.taskClickListener = taskClickListener;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_row, parent, false);

        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task task = taskList.get(position);
        String dueDateFormatted = Utils.formatDate(task.getDueDate());

        holder.taskName.setText(task.getTaskName());
        holder.dueDate.setText(dueDateFormatted);
        holder.description.setText(task.getDescription());

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView taskName;
        private TextView dueDate;
        private AppCompatRadioButton radioButton;
        private TextView description;
        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.task);
            dueDate = itemView.findViewById(R.id.date_view);
            radioButton = itemView.findViewById(R.id.radio_button);
            description = itemView.findViewById(R.id.description);

            itemView.setOnClickListener(this);
            radioButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Task currTask = taskList.get(getAdapterPosition());
            int id = v.getId();
            if (id == R.id.task_row){
                taskClickListener.taskClick(currTask);
            }
            else if (id == R.id.radio_button){
                taskClickListener.radioButtonClick(currTask, radioButton);
            }
        }
    }
}
