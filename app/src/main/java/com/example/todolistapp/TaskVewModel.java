package com.example.todolistapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskVewModel extends AndroidViewModel {
    private static TaskRepository taskRepository;
    LiveData<List<Task>> allTasks;

    public TaskVewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        allTasks = taskRepository.getAllTasks();
    }

    public static void insertTask(Task task){
        taskRepository.insert(task);
    }
    public static void updateTask(Task task){
        taskRepository.update(task);
    }
    public static void deleteTask(Task task){
        taskRepository.delete(task);
    }
    public static void deleteAllTasks(){
        taskRepository.deleteAll();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }
}
