package com.tankraj.profiledemo.repository.local;

import androidx.lifecycle.LiveData;

import com.tankraj.profiledemo.db.AppDatabase;
import com.tankraj.profiledemo.dao.ProfileDao;
import com.tankraj.profiledemo.model.Profile;

import java.util.List;

public class ProfileRepository {
    private static final String LOG_TAG = ProfileRepository.class.getSimpleName();
    private LiveData<List<Profile>> tasks;
    private ProfileDao profileDao;
    AppDatabase database;
    public ProfileRepository(AppDatabase database) {
        this.database = database;
    }


    public LiveData<List<Profile>> getloadAllTasks() {
        tasks = database.taskDao().loadAllTasks();
        return tasks;
    }


    public LiveData<Profile> getloadTaskById(int taskId) {
        return database.taskDao().loadTaskById(taskId);
    }

    public void deleteTasks(Profile profile) {
        database.taskDao().deleteTask(profile);
    }

    public void updateTaskById(Profile task) {
        database.taskDao().updateTask(task);
    }
}
