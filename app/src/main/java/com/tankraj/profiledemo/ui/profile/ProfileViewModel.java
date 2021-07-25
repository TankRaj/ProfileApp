package com.tankraj.profiledemo.ui.profile;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tankraj.profiledemo.db.AppDatabase;
import com.tankraj.profiledemo.entity.ProfileEntity;
import com.tankraj.profiledemo.repository.local.LocalRepository;

import java.util.List;

import static com.tankraj.profiledemo.utils.Commons.LogDebugger;

public class ProfileViewModel extends AndroidViewModel {

    private static final String TAG = ProfileViewModel.class.getSimpleName();

    private LiveData<List<ProfileEntity>> profiles;
    private final LocalRepository localRepository;
    public ProfileViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        LogDebugger(TAG, "Actively retrieving the profiles from the DataBase");
        localRepository = new LocalRepository(database);
        profiles = localRepository.loadAllProfiles();
    }

    public LiveData<List<ProfileEntity>> getProfiles() {
        return profiles;
    }

    public void deleteProfiles(ProfileEntity profileEntity) {
        localRepository.deleteProfiles(profileEntity);
    }
}