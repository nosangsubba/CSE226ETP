package com.example.todolistapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;


    public TaskRepository(Application application) {
        TaskDatabase database = TaskDatabase.getInstance(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getAllTasks();
    }
    public void insert(Task task){
        new InsertAsyncTask(taskDao).execute(task);
    }
    public void update(Task task){
        new UpdateAsyncTask(taskDao).execute(task);
    }

    public void delete(Task task){
        new DeleteAsyncTask(taskDao).execute(task);

    }
    public void deleteAll(){
        new DeleteAllAsyncTask(taskDao).execute();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    private class InsertAsyncTask extends AsyncTask<Task, Void, Void>{
        private TaskDao taskDao;

        public InsertAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }

    private class UpdateAsyncTask extends AsyncTask<Task, Void, Void>{
        private TaskDao taskDao;

        public UpdateAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends AsyncTask<Task, Void, Void>{
        private TaskDao taskDao;

        public DeleteAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }
    private class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void>{
        private TaskDao taskDao;

        public DeleteAllAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.deleteAll();
            return null;
        }
    }

}
