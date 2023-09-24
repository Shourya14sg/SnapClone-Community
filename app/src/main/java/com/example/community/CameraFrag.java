package com.example.community;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_MEDIA_IMAGES;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFrag extends Fragment implements SurfaceHolder.Callback, Camera.PictureCallback,Camera.PreviewCallback{
    public static final int REQUEST_CODE = 100;
    private String[] neededPermissions = new String[]{CAMERA, READ_MEDIA_IMAGES};
    boolean key=false;
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder holder;
    Camera.PictureCallback callback;
    public static CameraFrag newInstance(String param1, String param2) {
        CameraFrag fragment = new CameraFrag();
        return fragment;
    }

    public CameraFrag() {
        // Required empty public constructor
    }
    private void checkPermission() {
        int currentAPIversion= Build.VERSION.SDK_INT;
        //obviously app is for greater then android 10 so bellow condition is always true
        if(currentAPIversion >= Build.VERSION_CODES.M);//build.os.version_CODES.L lollipo/m/O oreo //M stands for Marshmallow not required but still
        {
            ArrayList<String> permissionsNotGranted=new ArrayList<>();
            for(String permission :neededPermissions) {
                if(ContextCompat.checkSelfPermission(getContext(),permission)!= PackageManager.PERMISSION_GRANTED)//package manager checks and manages packages,permission
                    permissionsNotGranted.add(permission);
            }

            if (permissionsNotGranted.size() > 0) {
                boolean shouldShowAlert = false;
                for (String permission : permissionsNotGranted) {
                    shouldShowAlert = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission);
                }

                if (shouldShowAlert) {
                    Toast.makeText(getContext(), "Here", Toast.LENGTH_SHORT).show();
                    showPermissionAlert(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]));   //error
                }
                else {
                    requestPermissions(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]));
                    Toast.makeText(getContext(), "Here", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showPermissionAlert(final String[] permissions) {
        Toast.makeText(getContext(), "HEre", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
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
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_CODE);
    }
//*****************IMPORTANT*******************/
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                for (int result : grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(getContext(), R.string.permission_warning, Toast.LENGTH_LONG).show();
                        // Not all permissions granted. Show message to the user.
                       // setViewVisibility(R.id.showPermissionMsg, View.VISIBLE);
                        Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                        key=false;
                        return;
                    }
                }
                // All permissions are granted. So, do the appropriate work now.
                Toast.makeText(getContext(), "Permission Allowed", Toast.LENGTH_SHORT).show();
                key=true;
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!key){
            Toast.makeText(getContext(), "Error : permission denied"+key, Toast.LENGTH_SHORT).show();
            //   return null;
        }
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_camera, container, false);
        surfaceView=view.findViewById(R.id.surfaceView);
        setupSurfaceHolder();

        Button mLogOut=view.findViewById(R.id.log_out);
        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(),SplashScreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                //FirebaseAuth mAuth=
                //FirebaseUser user=mAuth.getCurrentUser();
            }
        });

        Button captureImage=view.findViewById(R.id.capturePic);
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //camera.addCallbackBuffer(this)
                captureImage();
                /*if(camera!=null)
                    camera.takePicture(null,null,(Camera.PictureCallback) getContext());
            */
            }

        });

        return view;
    }
    public void captureImage() {
        if (camera != null) {
            camera.takePicture(null, null, this);
        }
    }
    private void setupSurfaceHolder() {
        holder = surfaceView.getHolder();

        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        //IMPORTANT
        //startCamera
        camera = android.hardware.Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        camera.setDisplayOrientation(90);// as our screen corners are 90'
        parameters.setPreviewFrameRate(30); //almost all device supports it
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        camera.setParameters(parameters);
        try {
            //it is like video
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
@Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            resetCamera();
}

    private void resetCamera() {
        if (holder.getSurface() == null) {
            // Return if preview surface does not exist
            return;
        }
        if (camera != null) {
            // Stop if preview surface is already running.
            camera.stopPreview();
            try {
                // Set preview display
                camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Start the camera preview...
            camera.startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
//    releaseCamera();
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(ContextCompat.checkSelfPermission(getContext(),CAMERA)!=PackageManager.PERMISSION_GRANTED)
            checkPermission();
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        saveImage(bytes);
        //resetCamera();
    }
    private void saveImage(byte[] bytes) {
        FileOutputStream outStream;
        try {
            String fileName = "ClonePhotos" + System.currentTimeMillis() + ".jpeg";
            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            outStream = new FileOutputStream(file);
            outStream.write(bytes);
            outStream.close();
            Toast.makeText(getContext(), "Picture Saved: " + fileName, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {

    }

    public void onflipCamera(View view) {
        if(camera!=null)
        {
           // camera= Camera.
        }
    }

}
