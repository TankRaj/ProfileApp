package com.tankraj.profiledemo.ui.addprofile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.tankraj.profiledemo.R;
import com.tankraj.profiledemo.databinding.ActivityAddProfileBinding;
import com.tankraj.profiledemo.db.AppDatabase;
import com.tankraj.profiledemo.db.AppExecutors;
import com.tankraj.profiledemo.model.Profile;
import com.tankraj.profiledemo.utils.Commons;

import java.util.Date;

import static com.tankraj.profiledemo.utils.Commons.REQUEST_ID_MULTIPLE_PERMISSIONS;
import static com.tankraj.profiledemo.utils.Commons.checkAndRequestPermissions;


public class AddProfileActivity extends AppCompatActivity {

    public static final String EXTRA_PROF_ID = "extraProfId";
    public static final String INSTANCE_PROF_ID = "instanceProfId";
    private static final int DEFAULT_PROF_ID = -1;
    private static final String TAG = AddProfileActivity.class.getSimpleName();

    private int mProfId = DEFAULT_PROF_ID;

    ActivityAddProfileBinding binding;
    private AppDatabase mDb;
    AddProfileViewModel viewModel;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Uri selectedImageUri = data.getData();
                        if (null != selectedImageUri) {
                            binding.ivProfile.setImageURI(selectedImageUri);
                        }
                    }
                });

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_profile);

        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_PROF_ID)) {
            mProfId = savedInstanceState.getInt(INSTANCE_PROF_ID, DEFAULT_PROF_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_PROF_ID)) {
            binding.btnSubmit.setText(R.string.update_button);
            if (mProfId == DEFAULT_PROF_ID) {
                mProfId = intent.getIntExtra(EXTRA_PROF_ID, DEFAULT_PROF_ID);
                AddProfileViewModelFactory factory = new AddProfileViewModelFactory(mDb, mProfId);
                viewModel = ViewModelProviders.of(this, factory).get(AddProfileViewModel.class);
                viewModel.getTask().observe(this, new Observer<Profile>() {
                    @Override
                    public void onChanged(Profile profile) {
                        viewModel.getTask().removeObserver(this);
                        populateUI(profile);
                    }
                });
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_PROF_ID, mProfId);
        super.onSaveInstanceState(outState);
    }

    private void initViews() {
//        mEditText = findViewById(R.id.editTextTaskDescription);
//        mRadioGroup = findViewById(R.id.radioGroup);
//
//        mButton = findViewById(R.id.saveButton);
        binding.ivProfile.setOnClickListener(v -> {
            if(checkAndRequestPermissions(this)){
                chooseImage(this);
            }
        });
        binding.btnSubmit.setOnClickListener(view -> onSaveButtonClicked());
    }

    private void populateUI(Profile profile) {
        if (profile == null) {
            return;
        }

        binding.etName.setText(profile.getDescription());
//        setPriorityInViews(task.getPriority());
    }

    public void onSaveButtonClicked() {
        String description = binding.etName.getText().toString();
//        int priority = getPriorityFromViews();
        Date date = new Date();

        final Profile task = new Profile(description, 2, date);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mProfId == DEFAULT_PROF_ID) {
                    mDb.taskDao().insertTask(task);
                } else {
                    task.setId(mProfId);
                    viewModel.updateTask(task);
                }
                finish();
            }
        });
    }

    private void chooseImage(Context context){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    activityResultLauncher.launch(takePicture);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(pickPhoto);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(AddProfileActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Commons.showToast(this, "App Requires Access to Camara.");

                } else if (ContextCompat.checkSelfPermission(AddProfileActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Commons.showToast(this, "App Requires Access to Your Storage.");
                } else {
                    chooseImage(AddProfileActivity.this);
                }
                break;

        }
    }

}