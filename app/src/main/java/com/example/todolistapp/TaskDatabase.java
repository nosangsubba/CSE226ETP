package com.example.todolistapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;

@Database(entities = {Task.class}, version = 3)
@TypeConverters({Converter.class})
public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase INSTANCE;
    public abstract TaskDao taskDao();

    public static synchronized TaskDatabase getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, TaskDatabase.class, "TASK_DATABASE")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDataBase(INSTANCE).execute();
        }
    };

    private static class PopulateDataBase extends AsyncTask<Void, Void, Void>{
        TaskDao taskDao;

        public PopulateDataBase(TaskDatabase taskDatabase) {
            this.taskDao = taskDatabase.taskDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Task task = new Task("Task one", Calendar.getInstance().getTime(), 2 ,"Description",false);
            taskDao.insert(task);
            return null;
        }
    }

}
