package com.tankraj.profiledemo.ui.addprofile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import com.tankraj.profiledemo.entity.ProfileEntity;
import com.tankraj.profiledemo.utils.Commons;

import java.util.Calendar;
import java.util.Date;

import static android.text.TextUtils.isEmpty;
import static com.tankraj.profiledemo.utils.Commons.REQUEST_ID_MULTIPLE_PERMISSIONS;
import static com.tankraj.profiledemo.utils.Commons.checkAndRequestPermissions;
import static com.tankraj.profiledemo.utils.Commons.checkEmpty;
import static com.tankraj.profiledemo.utils.Commons.isValidEmail;
import static com.tankraj.profiledemo.utils.Commons.isValidMobile;
import static com.tankraj.profiledemo.utils.Commons.showToast;


public class AddProfileActivity extends AppCompatActivity {

    public static final String EXTRA_PROF_ID = "extraProfId";
    public static final String INSTANCE_PROF_ID = "instanceProfId";
    private static final int DEFAULT_PROF_ID = -1;
    private static final String TAG = AddProfileActivity.class.getSimpleName();

    private int mProfId = DEFAULT_PROF_ID;
    private String imageUri;
    private String gender;
    private String device;

    ActivityAddProfileBinding binding;
    private AppDatabase mDb;
    AddProfileViewModel viewModel;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_profile);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        handleImage(result.getData());
                    }
                });


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
                viewModel.getProfile().observe(this, new Observer<ProfileEntity>() {
                    @Override
                    public void onChanged(ProfileEntity profileEntity) {
                        viewModel.getProfile().removeObserver(this);
                        populateUI(profileEntity);
                    }
                });
            }
        }
    }

    private void handleImage(Intent data) {
        Uri selectedImageUri = data.getData();
        if (null != selectedImageUri) {
            binding.ivProfile.setImageURI(selectedImageUri);
            imageUri = selectedImageUri.toString();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_PROF_ID, mProfId);
        super.onSaveInstanceState(outState);
    }

    private void initViews() {
//        mButton = findViewById(R.id.saveButton);
        binding.ivProfile.setOnClickListener(v -> {
            if (checkAndRequestPermissions(this)) {
                chooseImage(this);
            }
        });
        binding.rbAndroid.setChecked(true);
        binding.rbMale.setChecked(true);
        binding.rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_male:
                    gender = getResources().getString(R.string.male);
                    break;
                case R.id.rb_female:
                    gender = getResources().getString(R.string.female);
                    break;
                case R.id.rb_others:
                    gender = getResources().getString(R.string.others);
            }

        });
        binding.rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_male:
                    gender = getResources().getString(R.string.male);
                    break;
                case R.id.rb_female:
                    gender = getResources().getString(R.string.female);
                    break;
                case R.id.rb_others:
                    gender = getResources().getString(R.string.others);
                    break;
            }

        });
        binding.rgDevice.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_android:
                    device = getResources().getString(R.string.android);
                    break;
                case R.id.rb_ios:
                    device = getResources().getString(R.string.ios);
                    break;
                case R.id.rb_other_device:
                    device = getResources().getString(R.string.others);
                    break;
            }

        });
        binding.btnSubmit.setOnClickListener(view -> onSaveButtonClicked());
    }

    private void populateUI(ProfileEntity profileEntity) {
        if (profileEntity == null) {
            return;
        }

        binding.etName.setText(profileEntity.getName());
        binding.etEmail.setText(profileEntity.getEmail());
        binding.etPhone.setText(profileEntity.getPhone());
        binding.etBio.setText(profileEntity.getBio());

        gender = profileEntity.getGender();
        device = profileEntity.getDeviceType();
        imageUri = profileEntity.getImageUri();

        if (profileEntity.getDeviceType().equalsIgnoreCase(getString(R.string.android))){
            binding.rbAndroid.setChecked(true);
        }else if (profileEntity.getDeviceType().equalsIgnoreCase(getString(R.string.ios))){
            binding.rbIos.setChecked(true);
        }else {
            binding.rbOtherDevice.setChecked(true);
        }
        if (profileEntity.getGender().equalsIgnoreCase(getString(R.string.male))){
            binding.rbMale.setChecked(true);
        }else if (profileEntity.getGender().equalsIgnoreCase(getString(R.string.female))){
            binding.rbFemale.setChecked(true);
        }else {
            binding.rbOthers.setChecked(true);
        }

        binding.ivProfile.setImageURI(Uri.parse(profileEntity.getImageUri()));
    }

    public void onSaveButtonClicked() {
        String name = binding.etName.getText().toString();
        String email = binding.etEmail.getText().toString();
        String phone = binding.etPhone.getText().toString();
        String bio = binding.etBio.getText().toString();
        Date date = new Date();

        if (checkEmpty(name)) {
            showToast(this, "Name cannot be empty!");
        } else if (checkEmpty(email)) {
            showToast(this, "Email cannot be empty!");
        } else if (checkEmpty(phone)) {
            showToast(this, "Email cannot be empty!");
        } else if (checkEmpty(bio)) {
            showToast(this, "Bio cannot be empty!");
        } else if (checkEmpty(imageUri)) {
            showToast(this, "Profile image not selected!");
        }else if (!isValidEmail(email)) {
            showToast(this, "Please enter valid email address!");
        } else if (!isValidMobile(phone)) {
            showToast(this, "Please enter valid phone number!");
        } else {
            if (checkEmpty(gender))
                gender = getResources().getString(R.string.male);
            if (checkEmpty(device))
                device = getResources().getString(R.string.android);
//        final ProfileEntity profileEntity = new ProfileEntity(name,  date);
            final ProfileEntity profileEntity = new ProfileEntity(name, email, phone, bio, device, imageUri, gender, date);
            AppExecutors.getInstance().diskIO().execute(() -> {
                if (mProfId == DEFAULT_PROF_ID) {
                    mDb.profileDao().insertProfile(profileEntity);
                } else {
                    profileEntity.setId(mProfId);
                    viewModel.updateProfile(profileEntity);
                }
                finish();
            });
        }
    }

    private void chooseImage(Context context) {
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(optionsMenu, (dialogInterface, i) -> {
            /*if (optionsMenu[i].equals("Take Photo")) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncher.launch(takePicture);
            } else*/
            if (optionsMenu[i].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(pickPhoto);
            } else if (optionsMenu[i].equals("Exit")) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        viewModel.onCleared();
    }

}