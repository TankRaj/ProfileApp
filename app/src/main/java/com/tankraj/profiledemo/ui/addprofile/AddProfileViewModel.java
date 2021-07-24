package com.tankraj.profiledemo.ui.addprofile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tankraj.profiledemo.db.AppDatabase;
import com.tankraj.profiledemo.model.Profile;
import com.tankraj.profiledemo.repository.local.ProfileRepository;

public class AddProfileViewModel extends ViewModel {

    private LiveData<Profile> task;
    private final ProfileRepository profileRepository;

    public AddProfileViewModel(AppDatabase database, int taskId) {
       // task = database.taskDao().loadTaskById(taskId);
        profileRepository = new ProfileRepository(database);
        task = profileRepository.getloadTaskById(taskId);
    }

    public LiveData<Profile> getTask() {
        return task;
    }

    public void updateTask(Profile task) {
        profileRepository.updateTaskById(task);
    }
}
