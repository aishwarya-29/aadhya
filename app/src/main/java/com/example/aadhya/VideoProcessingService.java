package com.example.aadhya;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.CameraError;
import com.androidhiddencamera.HiddenCameraService;
import com.androidhiddencamera.HiddenCameraUtils;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraFocus;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Keval on 11-Nov-16.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

public class VideoProcessingService extends HiddenCameraService {
    // class variable
    final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

    final java.util.Random rand = new java.util.Random();

    // consider using a Map<String,Boolean> to say whether the identifier is being used or not
    final Set<String> identifiers = new HashSet<String>();

    public String randomIdentifier() {
        StringBuilder builder = new StringBuilder();
        while (builder.toString().length() == 0) {
            int length = rand.nextInt(5) + 5;
            for (int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if (identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            if (HiddenCameraUtils.canOverDrawOtherApps(this)) {
                CameraConfig cameraConfig = new CameraConfig()
                        .getBuilder(this)
                        .setCameraFacing(CameraFacing.REAR_FACING_CAMERA)
                        .setCameraResolution(CameraResolution.MEDIUM_RESOLUTION)
                        .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                        .setCameraFocus(CameraFocus.AUTO)
                        .build();

                startCamera(cameraConfig);

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(VideoProcessingService.this,
                                "Capturing image.", Toast.LENGTH_SHORT).show();

                        takePicture();
                    }
                }, 2000L);
            } else {

                //Open settings to grant permission for "Draw other apps".
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
            }
        } else {

            //TODO Ask your parent activity for providing runtime permission
            Toast.makeText(this, "Camera permission not available", Toast.LENGTH_SHORT).show();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onImageCapture(final File imageFile) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference picharRef = storageRef.child(imageFile.getName());
        StorageReference picharImagesRef = storageRef.child(imageFile.getAbsolutePath());
        picharRef.getName().equals(picharImagesRef.getName());
        picharRef.getPath().equals(picharImagesRef.getPath());
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String currentUserEmail = currentUser.getEmail();
        final String[] userID = new String[1];
        reference.child("User").orderByChild("uemail").equalTo(currentUserEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    userID[0] = ds.child("key").getValue(String.class);
                    storePicture(userID[0], imageFile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void storePicture(String userID, File imageFile) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        Log.d(String.valueOf(0), userID + "/");
        StorageReference pichar1Ref = storageRef.child("data/" + userID + "/" + "images/" + randomIdentifier());
        Uri file = Uri.fromFile(new File(imageFile.getAbsolutePath()));
        UploadTask uploadTask = pichar1Ref.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getApplication(), "OopsieDoodle", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });
        stopSelf();
    }

    @Override
    public void onCameraError(@CameraError.CameraErrorCodes int errorCode) {
        switch (errorCode) {
            case CameraError.ERROR_CAMERA_OPEN_FAILED:
                //Camera open failed. Probably because another application
                //is using the camera
                Toast.makeText(this, "R.string.error_cannot_open", Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_IMAGE_WRITE_FAILED:
                //Image write failed. Please check if you have provided WRITE_EXTERNAL_STORAGE permission
                Toast.makeText(this, "R.string.error_cannot_write", Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE:
                //camera permission is not available
                //Ask for the camera permission before initializing it.
                Toast.makeText(this, "R.string.error_cannot_get_permission", Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION:
                //Display information dialog to the user with steps to grant "Draw over other app"
                //permission for the app.
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA:
                Toast.makeText(this, "R.string.error_not_having_camera", Toast.LENGTH_LONG).show();
                break;
        }

        stopSelf();
    }
}