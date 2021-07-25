package com.tankraj.profiledemo.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.tankraj.profiledemo.dao.ProfileDao;
import com.tankraj.profiledemo.entity.ProfileEntity;
import com.tankraj.profiledemo.utils.DateConverter;

import static com.tankraj.profiledemo.utils.Commons.LogDebugger;


@Database(entities = {ProfileEntity.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "db_profiles";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                LogDebugger(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        //.allowMainThreadQueries()
                        .build();
            }
        }
        LogDebugger(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract ProfileDao profileDao();

}
