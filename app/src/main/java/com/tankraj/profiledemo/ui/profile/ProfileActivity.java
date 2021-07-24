package com.tankraj.profiledemo.ui.profile;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tankraj.profiledemo.R;
import com.tankraj.profiledemo.databinding.ActivityAddProfileBinding;
import com.tankraj.profiledemo.databinding.ActivityProfileBinding;
import com.tankraj.profiledemo.db.AppDatabase;
import com.tankraj.profiledemo.db.AppExecutors;
import com.tankraj.profiledemo.model.Profile;
import com.tankraj.profiledemo.ui.addprofile.AddProfileActivity;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ProfileActivity extends AppCompatActivity implements ProfileAdapter.ItemClickListener {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private ProfileAdapter mAdapter;
    private AppDatabase mDb;
    ProfileViewModel viewModel;
    ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         binding =  DataBindingUtil.setContentView(this, R.layout.activity_profile);

        binding.rvProfile.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ProfileAdapter(this, this);
        setupViewModel();
        binding.rvProfile.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        binding.rvProfile.addItemDecoration(decoration);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Profile> tasks = mAdapter.getTasks();
                        viewModel.deleteTask(tasks.get(position));
                    }
                });
            }
        }).attachToRecyclerView(binding.rvProfile);

        binding.fabAdd.setOnClickListener(view -> {
            Intent addProfileIntent = new Intent(ProfileActivity.this, AddProfileActivity.class);
            startActivity(addProfileIntent);
        });
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        viewModel.getTasks().observe(this, new Observer<List<Profile>>() {
            @Override
            public void onChanged(@Nullable List<Profile> taskEntries) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                mAdapter.setTasks(taskEntries);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(ProfileActivity.this, AddProfileActivity.class);
        intent.putExtra(AddProfileActivity.EXTRA_PROF_ID, itemId);
        startActivity(intent);
    }
}