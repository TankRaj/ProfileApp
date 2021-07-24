package com.tankraj.profiledemo.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tankraj.profiledemo.model.Profile;

import java.util.List;

@Dao
public interface ProfileDao {

    @Query("SELECT * FROM Profile ORDER BY priority")
    LiveData<List<Profile>> loadAllTasks();

    @Query("SELECT * FROM Profile WHERE id=:id")
    LiveData<Profile> loadTaskById(int id);

    @Insert
    void insertTask(Profile profile);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Profile profile);

    @Delete
    void deleteTask(Profile profile);


}
