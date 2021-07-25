package com.tankraj.profiledemo.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tankraj.profiledemo.entity.ProfileEntity;

import java.util.List;

@Dao
public interface ProfileDao {

    @Query("SELECT * FROM profiles ORDER BY updated_at")
    LiveData<List<ProfileEntity>> loadAllProfiles();

    @Query("SELECT * FROM profiles WHERE id=:id")
    LiveData<ProfileEntity> loadProfileById(int id);

    @Insert
    void insertProfile(ProfileEntity profileEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateProfile(ProfileEntity profileEntity);

    @Delete
    void deleteProfile(ProfileEntity profileEntity);


}
