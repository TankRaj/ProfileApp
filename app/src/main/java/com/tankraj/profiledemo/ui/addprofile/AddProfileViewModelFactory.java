package com.tankraj.profiledemo.ui.addprofile;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.tankraj.profiledemo.db.AppDatabase;

public class AddProfileViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final int mProfId;

    public AddProfileViewModelFactory(AppDatabase database, int profId) {
        mDb = database;
        mProfId = profId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new AddProfileViewModel(mDb, mProfId);
    }
}
