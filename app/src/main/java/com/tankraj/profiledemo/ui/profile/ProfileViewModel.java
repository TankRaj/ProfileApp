package com.tankraj.profiledemo.ui.profile;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tankraj.profiledemo.db.AppDatabase;
import com.tankraj.profiledemo.entity.ProfileEntity;
import com.tankraj.profiledemo.repository.local.ProfileRepLocalository;

import java.util.List;

import static com.tankraj.profiledemo.utils.Commons.LogDebugger;

public class ProfileViewModel extends AndroidViewModel {

    private static final String TAG = ProfileViewModel.class.getSimpleName();

    private LiveData<List<ProfileEntity>> profiles;
    private final ProfileRepLocalository profileRepLocalository;
    public ProfileViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        LogDebugger(TAG, "Actively retrieving the profiles from the DataBase");
        profileRepLocalository = new ProfileRepLocalository(database);
        profiles = profileRepLocalository.loadAllProfiles();
    }

    public LiveData<List<ProfileEntity>> getProfiles() {
        return profiles;
    }

    public void deleteProfiles(ProfileEntity profileEntity) {
        profileRepLocalository.deleteProfiles(profileEntity);
    }
}