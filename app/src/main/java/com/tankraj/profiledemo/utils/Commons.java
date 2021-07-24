package com.tankraj.profiledemo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Commons {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 222;

    public static Date getCurrentDateTime(){
        Date currentDate =  Calendar.getInstance().getTime();
        return currentDate;
    }

    public static String getFormattedDateString(Date date) {

        try {

            SimpleDateFormat spf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            String dateString = spf.format(date);

            Date newDate = spf.parse(dateString);
            spf= new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            return spf.format(newDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void openKeyboard(final Context context) {
        new Handler().postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }, 500);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}
