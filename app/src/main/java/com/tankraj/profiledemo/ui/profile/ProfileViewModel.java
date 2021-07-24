package com.tankraj.profiledemo.ui.profile;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tankraj.profiledemo.db.AppDatabase;
import com.tankraj.profiledemo.model.Profile;
import com.tankraj.profiledemo.repository.local.ProfileRepository;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {

    private static final String TAG = ProfileViewModel.class.getSimpleName();

    private LiveData<List<Profile>> tasks;
    private final ProfileRepository profileRepository;
    public ProfileViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        //tasks = database.taskDao().loadAllTasks();
        profileRepository = new ProfileRepository(database);
        tasks = profileRepository.getloadAllTasks();
    }

    public LiveData<List<Profile>> getTasks() {
        return tasks;
    }

    public void deleteTask(Profile profile) {
        profileRepository.deleteTasks(profile);
    }
}