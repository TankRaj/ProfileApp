package com.tankraj.profiledemo.repository.local;

import androidx.lifecycle.LiveData;

import com.tankraj.profiledemo.db.AppDatabase;
import com.tankraj.profiledemo.dao.ProfileDao;
import com.tankraj.profiledemo.entity.ProfileEntity;

import java.util.List;

public class LocalRepository {
    private static final String LOG_TAG = LocalRepository.class.getSimpleName();
    private LiveData<List<ProfileEntity>> profiles;
    private ProfileDao profileDao;
    AppDatabase database;
    public LocalRepository(AppDatabase database) {
        this.database = database;
    }


    public LiveData<List<ProfileEntity>> loadAllProfiles() {
        profiles = database.profileDao().loadAllProfiles();
        return profiles;
    }


    public LiveData<ProfileEntity> loadProfileById(int profileId) {
        return database.profileDao().loadProfileById(profileId);
    }

    public void deleteProfiles(ProfileEntity profileEntity) {
        database.profileDao().deleteProfile(profileEntity);
    }

    public void updateProfileById(ProfileEntity profileEntity) {
        database.profileDao().updateProfile(profileEntity);
    }
    public void insertProfile(ProfileEntity profileEntity) {
        database.profileDao().insertProfile(profileEntity);
    }
}
