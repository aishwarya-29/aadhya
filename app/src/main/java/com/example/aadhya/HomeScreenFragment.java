package com.example.aadhya;

import android.Manifest;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class HomeScreenFragment extends Fragment {
    Button help, stopRecording;
    final String pin = "1234";
    MediaRecorder mediaRecorder;
    File audioFile = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_home_screen, container, false);
        help = v.findViewById(R.id.help);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                        dialog.setTitle("Sending for help...");
                        dialog.setMessage("Enter your secret PIN to cancel alert");

                        final EditText input = new EditText(getContext());
                        input.setWidth(50);

                        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        dialog.setView(input);

                        dialog.setPositiveButton("SOS", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
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
        final ProgressDialog mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setTitle("Recording");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setButton("Stop recording", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mProgressDialog.dismiss();
                mediaRecorder.stop();
                mediaRecorder.release();
                Toast.makeText(getContext(),"stopped", Toast.LENGTH_SHORT).show();
            }
        });

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
            public void onCancel(DialogInterface p1) {
                mediaRecorder.stop();
                mediaRecorder.release();
            }
        });
        mediaRecorder.start();
        mProgressDialog.show();
        Toast.makeText(getContext(),"Started", Toast.LENGTH_SHORT).show();
    }

//    public void stopRecording(View view) {
//        mediaRecorder.stop();
//        mediaRecorder.release();
//        addRecordingToMediaLibrary();
//    }
//
//    protected void addRecordingToMediaLibrary() {
//        ContentValues values = new ContentValues(4);
//        long current = System.currentTimeMillis();
//        values.put(MediaStore.Audio.Media.TITLE, "audio" + audioFile.getName());
//        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
//        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
//        values.put(MediaStore.Audio.Media.DATA, audioFile.getAbsolutePath());
//
//        ContentResolver contentResolver = getContext().getContentResolver();
//        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Uri newUri = contentResolver.insert(base, values);
//
//        //sending broadcast message to scan the media file so that it can be available
//        getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
//        Toast.makeText(getContext(), "Added File " + newUri, Toast.LENGTH_LONG).show();
//    }
}