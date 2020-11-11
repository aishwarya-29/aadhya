package com.example.aadhya;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.androidhiddencamera.HiddenCameraFragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class HomeScreenFragment extends Fragment {
    Button help, stopRecording, help2;
    public static String pin = "3333";
    MediaRecorder mediaRecorder;
    File audioFile = null;
    AnimatorSet mAnimationSet;
    ObjectAnimator fadeOut, fadeIn;
    static boolean SOSMode = false;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserEmail;
    public  static String userID;
    SwitchCompat shakeswitch;
    HiddenCameraFragment mHiddenCameraFragment;
    Timer mTmr;
    TimerTask mTsk;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home_screen, container, false);
        currentUserEmail = currentUser.getEmail();
        Contacts.setContacts();
        getActivity().startService(new Intent(getActivity(),LocationMonitor.class));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("User").orderByChild("uemail").equalTo(currentUserEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    pin = ds.child("upin").getValue(String.class);
                    userID = ds.child("key").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        help = v.findViewById(R.id.help);
        help2 = v.findViewById(R.id.help2);
        stopRecording = v.findViewById(R.id.stop_recording);
        shakeswitch=v.findViewById(R.id.shakeSwitch);
        shakeswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Intent shake= new Intent(getContext(), ShakeService.class);
                if(b){
                    Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getView()), "Shake to send alerts is ENABLED", BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.setDuration(2000);
                    snackbar.show();
                    getActivity().startService(shake);
                }
                else{
                    Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getView()), "Shake to send alerts is DISABLED", BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.setDuration(2000);
                    snackbar.show();
                    getActivity().stopService(shake);
                }
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SOSMode) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
                    builder.setTitle("Are you sure?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            confirmAlert();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                }
            }
        });
        return v;
    }

    private void confirmAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
        dialog.setTitle("Sending for help...");
        dialog.setMessage("Enter your secret PIN to cancel alert");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dialog.setView(input);

        dialog.setPositiveButton("SOS", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendAlerts();
                dialogInterface.cancel();
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String s = input.getText().toString();
                if (s.equals(pin)) {
                    Toast.makeText(getContext(), "Cancelled alerts", Toast.LENGTH_SHORT).show();
                    dialogInterface.cancel();
                } else {
                    Toast.makeText(getContext(), "Invalid PIN", Toast.LENGTH_LONG).show();
                }
            }
        });

        final AlertDialog alert = dialog.create();
        alert.show();
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                if (alert.isShowing()) {
                    sendAlerts();
                    alert.dismiss();
                }
            }
        };

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 10000);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendAlerts() {
        SOSMode = true;
        Toast.makeText(getContext(), "Sending alerts to all your contacts. Contact 911 for immediate assistance.", Toast.LENGTH_LONG).show();
        sendSMS();
        takePicture();
        mTmr = new Timer();
        mTsk = new TimerTask() {
            @Override
            public void run() {
                takePicture();
            }
        };
        mTmr.schedule(mTsk, 600000);
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.RECORD_AUDIO}, 0);

        } else {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {
                startRecording();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendSMS() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 0);
        } else {
            ArrayList<String> contactno = Contacts.contactno;
            SmsManager sms = SmsManager.getDefault();
            String message = "SOS. I'm in trouble. Follow the link to view my location. http://www.aadhya.com/track/" + userID;
            for (String no : contactno) {
                sms.sendTextMessage(no, null, message, null, null);
            }
        }
    }
    public static void stop(){

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startRecording() {
        try {
            audioFile = File.createTempFile("sound", ".3gp", Objects.requireNonNull(getActivity()).getExternalFilesDir(null));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("media recording", "external storage access error");
            return;
        }

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
        startButtonAnimation();
        stopRecording.setVisibility(View.VISIBLE);
        stopRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAlerts();
            }
        });
    }

    private void startButtonAnimation() {
        help2.setVisibility(View.VISIBLE);
        fadeOut = ObjectAnimator.ofFloat(help2, "alpha", .5f, .1f);
        fadeOut.setDuration(300);
        fadeIn = ObjectAnimator.ofFloat(help2, "alpha", .1f, .5f);
        fadeIn.setDuration(300);
        mAnimationSet = new AnimatorSet();
        mAnimationSet = new AnimatorSet();
        mAnimationSet.play(fadeIn).after(fadeOut);
        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimationSet.start();
            }
        });
        mAnimationSet.start();
    }

    private void stopAlerts() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
        dialog.setTitle("False Alarm?");
        dialog.setMessage("Enter your secret PIN to cancel alert");
        final EditText input = new EditText(getContext());
        input.setWidth(50);

        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dialog.setView(input);

        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (input.getText().toString().equals(pin)) {
                    SOSMode = false;
                    mediaRecorder.stop();
                    stopRecording.setVisibility(View.INVISIBLE);
                    mediaRecorder.release();
                    mTmr.cancel();
                    Toast.makeText(getContext(), "stopped", Toast.LENGTH_SHORT).show();
                    help2.setVisibility(View.INVISIBLE);
                    mAnimationSet.end();
                    mAnimationSet.cancel();
                } else {
                    Toast.makeText(getContext(), "Invalid PIN", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final AlertDialog alert = dialog.create();
        alert.show();
    }
    public void takePicture()
    {
        if (mHiddenCameraFragment != null) {    //Remove fragment from container if present
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .remove(mHiddenCameraFragment)
                    .commit();
            mHiddenCameraFragment = null;
        }
        getActivity().startService(new Intent(getActivity(), VideoProcessingService.class));
    }
}
