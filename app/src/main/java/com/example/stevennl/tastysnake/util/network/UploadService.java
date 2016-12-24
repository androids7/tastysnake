package com.example.stevennl.tastysnake.util.network;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.model.AnalysisData;

/**
 * Service to upload data to remote server.
 */
public class UploadService extends IntentService {
    private static final String TAG = "UploadService";

    public UploadService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart() called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int val = super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand() called");
        return val;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent() called");
        if (!NetworkUtil.isNetworkAvailable(this)) {
            Log.d(TAG, "Network unavailable");
        } else {
            Log.d(TAG, "Network available");
            AnalysisData data = AnalysisData.create(this);
            if (data == null || data.N < Config.UPLOAD_THRESHOLD) {
                return;
            }
            Log.d(TAG, "Insert W: " + data.W);
            NetworkUtil.getInstance(this).insertW(data.W, new NetworkUtil.ResultListener<String>() {
                @Override
                public void onGotResult(String result) {
                    Log.d(TAG, "Response: " + result);
                }

                @Override
                public void onError(VolleyError err) {
                    Log.e(TAG, err.toString());
                }
            });
        }
    }

    /**
     * Set the service alarm to start the service repeatedly.
     *
     * @param context The context
     * @param on True to start the alarm, false to stop it.
     */
    public static void setAlarm(Context context, boolean on) {
        Intent i = new Intent(context, UploadService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if (on) {
            am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), Config.FREQUENCY_UPLOAD, pi);
        } else {
            am.cancel(pi);
            pi.cancel();
        }
    }

    /**
     * Return true if the service alarm is on.
     */
    public static boolean isAlarmOn(Context context) {
        Intent i = new Intent(context, UploadService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }
}
