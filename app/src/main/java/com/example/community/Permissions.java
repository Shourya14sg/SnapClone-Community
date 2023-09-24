package com.example.community;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_MEDIA_IMAGES;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class Permissions {

    public static final int REQUEST_CODE = 100;
    private String[] neededPermissions;// = new String[]{CAMERA, READ_MEDIA_IMAGES};
    private Context context;
    private Activity activity;
    Permissions(String[] neededPermissions, Context context, Activity activity){
        this.neededPermissions=neededPermissions;
        this.context=context;
        this.activity=activity;
    }

    private void checkPermission() {
        int currentAPIversion = Build.VERSION.SDK_INT;
        //obviously app is for greater then android 10 so bellow condition is always true
        if (currentAPIversion >= Build.VERSION_CODES.M)
            ;//build.os.version_CODES.L lollipo/m/O oreo //M stands for Marshmallow not required but still
        {
            ArrayList<String> permissionsNotGranted = new ArrayList<>();
            for (String permission : neededPermissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)//package manager checks and manages packages,permission
                    permissionsNotGranted.add(permission);
            }

            if (permissionsNotGranted.size() > 0) {
                boolean shouldShowAlert = false;
                for (String permission : permissionsNotGranted) {
                    shouldShowAlert = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
                }

                if (shouldShowAlert) {
                    Toast.makeText(context, "Here", Toast.LENGTH_SHORT).show();
                    showPermissionAlert(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]));   //error
                } else {
                    requestPermissions(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]));
                    Toast.makeText(context, "Here", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showPermissionAlert(final String[] permissions) {
        Toast.makeText(context, "HEre", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle(R.string.permission_required);
        alertBuilder.setMessage(R.string.permission_message);
        alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(permissions);
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
    }

    //*****************IMPORTANT*******************/
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                for (int result : grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(context, R.string.permission_warning, Toast.LENGTH_LONG).show();
                        // Not all permissions granted. Show message to the user.
                        // setViewVisibility(R.id.showPermissionMsg, View.VISIBLE);
                        Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
                  //      key=false;
                        return;
                    }
                }
                // All permissions are granted. So, do the appropriate work now.
                Toast.makeText(context, "Permission Allowed", Toast.LENGTH_SHORT).show();
                //key=true;
                break;
        }
        activity.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}