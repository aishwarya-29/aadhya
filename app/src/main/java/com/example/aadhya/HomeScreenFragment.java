package com.example.aadhya;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class HomeScreenFragment extends Fragment {
    Button help, stopRecording, help2;
    final String pin = "1234";
    MediaRecorder mediaRecorder;
    File audioFile = null;
    AnimatorSet mAnimationSet;
    ObjectAnimator fadeOut, fadeIn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_home_screen, container, false);
        help = v.findViewById(R.id.help);
        help2 = v.findViewById(R.id.help2);
        stopRecording = v.findViewById(R.id.stop_recording);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                if(s.equals(pin)) {
                    Toast.makeText(getContext(),"Cancelled alerts", Toast.LENGTH_SHORT).show();
                    dialogInterface.cancel();
                } else {
                    Toast.makeText(getContext(),"Invalid PIN", Toast.LENGTH_LONG).show();
                }
            }
        });

        final AlertDialog alert = dialog.create();
        alert.show();
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
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

    private void sendAlerts() {
        Toast.makeText(getContext(),"Sending alerts to all your contacts. Contact 911 for immediate assistance.", Toast.LENGTH_LONG).show();
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.RECORD_AUDIO}, 0);

        } else {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {
                startRecording();
            }
        }
    }

    private void startRecording() {
        try {
            audioFile = File.createTempFile("sound",".3gp", getActivity().getExternalFilesDir(null));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("media recording","external storage access error");
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

    private void startButtonAnimation(){
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
        final AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(getContext(),R.style.AlertDialogCustom));
        dialog.setTitle("False Alarm?");
        dialog.setMessage("Enter your secret PIN to cancel alert");

        final EditText input = new EditText(getContext());
        input.setWidth(50);

        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        dialog.setView(input);

        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(input.getText().toString().equals(pin)) {
                    mediaRecorder.stop();
                    stopRecording.setVisibility(View.INVISIBLE);
                    mediaRecorder.release();
                    Toast.makeText(getContext(),"stopped", Toast.LENGTH_SHORT).show();
                    help2.setVisibility(View.INVISIBLE);
                    mAnimationSet.end();
                    mAnimationSet.cancel();
                } else {
                    Toast.makeText(getContext(),"Invalid PIN", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final AlertDialog alert = dialog.create();
        alert.show();

    }


}


