package com.tankraj.profiledemo.ui.addprofile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tankraj.profiledemo.db.AppDatabase;
import com.tankraj.profiledemo.entity.ProfileEntity;
import com.tankraj.profiledemo.repository.local.ProfileRepLocalository;

public class AddProfileViewModel extends ViewModel {

    private LiveData<ProfileEntity> profile;
    private final ProfileRepLocalository profileRepLocalository;

    public AddProfileViewModel(AppDatabase database, int profileId) {
        profileRepLocalository = new ProfileRepLocalository(database);
        profile = profileRepLocalository.loadProfileById(profileId);
    }

    public LiveData<ProfileEntity> getProfile() {
        return profile;
    }

    public void updateProfile(ProfileEntity profileEntity) {
        profileRepLocalository.updateProfileById(profileEntity);
    }
    public void insertProfile(ProfileEntity profileEntity) {
        profileRepLocalository.insertProfile(profileEntity);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }
}
