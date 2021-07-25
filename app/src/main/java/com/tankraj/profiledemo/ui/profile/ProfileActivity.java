package com.tankraj.profiledemo.ui.profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tankraj.profiledemo.R;
import com.tankraj.profiledemo.databinding.ActivityProfileBinding;
import com.tankraj.profiledemo.databinding.DialogBottomSheetBinding;
import com.tankraj.profiledemo.db.AppDatabase;
import com.tankraj.profiledemo.db.AppExecutors;
import com.tankraj.profiledemo.entity.ProfileEntity;
import com.tankraj.profiledemo.ui.addprofile.AddProfileActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.tankraj.profiledemo.utils.Commons.LogDebugger;
import static com.tankraj.profiledemo.utils.Commons.checkEmpty;
import static com.tankraj.profiledemo.utils.Commons.showToast;


public class ProfileActivity extends AppCompatActivity implements ProfileAdapter.ItemClickListener {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private ProfileAdapter mAdapter;
    ProfileViewModel viewModel;
    ActivityProfileBinding binding;

    private List<ProfileEntity> filteredProfiles = new ArrayList<>();
    private List<ProfileEntity> allProfiles = new ArrayList<>();

    private boolean isFilter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        binding.rvProfile.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ProfileAdapter(this, this);
        setupViewModel();
        binding.rvProfile.setAdapter(mAdapter);

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
                        int position = viewHolder.getAbsoluteAdapterPosition();
                        List<ProfileEntity> profileEntityList = mAdapter.getProfiles();
                        viewModel.deleteProfiles(profileEntityList.get(position));
                    }
                });
            }
        }).attachToRecyclerView(binding.rvProfile);

        binding.fabAdd.setOnClickListener(view -> {
            Intent addProfileIntent = new Intent(ProfileActivity.this, AddProfileActivity.class);
            startActivity(addProfileIntent);
        });

        binding.fabFilter.setOnClickListener(v -> {
            showFilterBottomSheet();
        });
    }

    private void showFilterBottomSheet() {
        DialogBottomSheetBinding bindingDialog;
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        View view = getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        bindingDialog = DataBindingUtil.bind(view);
        dialog.setContentView(view);

        bindingDialog.tvFrom.setOnClickListener(v -> {

            final Calendar c = Calendar.getInstance();
            DatePickerDialog dpd = new DatePickerDialog(this,
                    (view1, year, monthOfYear, dayOfMonth) ->
                            bindingDialog.tvFrom.setText(dayOfMonth + "/"
                                    + (monthOfYear + 1) + "/" + year),
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
            dpd.show();
        });

        bindingDialog.tvTo.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            DatePickerDialog dpd = new DatePickerDialog(this,
                    (view1, year, monthOfYear, dayOfMonth) ->
                            bindingDialog.tvTo.setText(dayOfMonth + "/"
                                    + (monthOfYear + 1) + "/" + year),
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
            dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
            dpd.show();

        });

        bindingDialog.btnFilter.setOnClickListener(v -> {
            String fromDate = bindingDialog.tvFrom.getText().toString();
            String toDate = bindingDialog.tvTo.getText().toString();
            if (fromDate.equalsIgnoreCase(toDate)||(!fromDate.equalsIgnoreCase(pattern)
                    &&fromDate.equalsIgnoreCase(toDate))) {
                showToast(this, "Please select different dates!");
            } else if ((checkEmpty(fromDate)||checkEmpty(toDate))
                    ||fromDate.equalsIgnoreCase(pattern) ||toDate.equalsIgnoreCase(pattern)) {
                showToast(this, "Please select dates!");
            } else {
                try {
                    filterProfiles(dateFormat.parse(fromDate), dateFormat.parse(toDate));
                    dialog.dismiss();
                } catch (ParseException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        });

        bindingDialog.tvClear.setOnClickListener(v -> {
            setDefault();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        viewModel.getProfiles().observe(this, profileEntities -> {
            LogDebugger(TAG, "Updating list of profiles from LiveData in ViewModel");
            allProfiles = profileEntities;
            setDefault();
        });
    }

    private void setDefault() {
        mAdapter.setProfiles(allProfiles);
        mAdapter.notifyDataSetChanged();
        isFilter = false;
        toggleNoData();
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(ProfileActivity.this, AddProfileActivity.class);
        intent.putExtra(AddProfileActivity.EXTRA_PROF_ID, itemId);
        startActivity(intent);
    }

    public void filterProfiles(Date fromDate, Date toDate) {
        filteredProfiles.clear();
        for (ProfileEntity profileEntity : allProfiles) {
            if (profileEntity.getUpdatedAt().after(fromDate) && profileEntity.getUpdatedAt().before(toDate)) {
                filteredProfiles.add(profileEntity);
            }
        }

        mAdapter.setProfiles(filteredProfiles);
        mAdapter.notifyDataSetChanged();
        isFilter = true;
        toggleNoData();
    }

    private void toggleNoData() {
        if (isFilter) {
            if (filteredProfiles != null && filteredProfiles.size() > 0) {
                binding.tvNoData.setVisibility(View.GONE);
                binding.rvProfile.setVisibility(View.VISIBLE);
            } else {
                binding.tvNoData.setText(R.string.no_filtered_profiles);
                binding.tvNoData.setVisibility(View.VISIBLE);
                binding.rvProfile.setVisibility(View.GONE);
            }
        } else {
            if (allProfiles != null && allProfiles.size() > 0) {
                binding.tvNoData.setVisibility(View.GONE);
                binding.rvProfile.setVisibility(View.VISIBLE);
            } else {
                binding.tvNoData.setText(R.string.no_profiles);
                binding.tvNoData.setVisibility(View.VISIBLE);
                binding.rvProfile.setVisibility(View.GONE);
            }
        }
    }
}