package com.example.aadhya;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

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

public class ShakeService extends Service {
    private SensorManager mSensorManager;
    private float acceleration;
    private float accelerationCurrent;
    private float accelerationLast;
    Boolean sent=false;
    private int mInterval = 3600000;
    private Handler mHandler;
    public ShakeService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        mHandler = new Handler();
        startRepeatingTask();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        acceleration = 10f;
        accelerationCurrent = SensorManager.GRAVITY_EARTH;
        accelerationLast = SensorManager.GRAVITY_EARTH;
        return START_STICKY;
    }

    private void sendAlerts(){

        ArrayList<String> contactno = Contacts.contactno;
        SmsManager sms = SmsManager.getDefault();
        String message = "SOS. I'm in trouble. Follow the link to view my location. http://www.aadhya.com/track/" + HomeScreenFragment.userID;
        for (String no : contactno) {
            sms.sendTextMessage(no, null, message, null, null);
        }
        sent=true;
    }
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
//            Log.i("hey", "Listening...");
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            accelerationLast = accelerationCurrent;
            accelerationCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = accelerationCurrent - accelerationLast;
            acceleration = acceleration * 0.9f + delta;
//            Log.i("hey", acceleration+" "+sent);
            if (acceleration > 12 && !sent) {
               sendAlerts();
               Toast.makeText(getApplicationContext(), "Sending alerts... ", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(mSensorListener);
        stopRepeatingTask();

    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i("hey","removed");
        sent=false;
        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
                sent=false;
                mHandler.postDelayed(mStatusChecker, mInterval);
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
    
}
