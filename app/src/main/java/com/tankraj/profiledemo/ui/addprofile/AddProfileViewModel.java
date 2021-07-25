package com.tankraj.profiledemo.ui.addprofile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tankraj.profiledemo.db.AppDatabase;
import com.tankraj.profiledemo.entity.ProfileEntity;
import com.tankraj.profiledemo.repository.local.LocalRepository;

public class AddProfileViewModel extends ViewModel {

    private LiveData<ProfileEntity> profile;
    private final LocalRepository localRepository;

    public AddProfileViewModel(AppDatabase database, int profileId) {
        localRepository = new LocalRepository(database);
        profile = localRepository.loadProfileById(profileId);
    }

    public LiveData<ProfileEntity> getProfile() {
        return profile;
    }

    public void updateProfile(ProfileEntity profileEntity) {
        localRepository.updateProfileById(profileEntity);
    }
    public void insertProfile(ProfileEntity profileEntity) {
        localRepository.insertProfile(profileEntity);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }
}
