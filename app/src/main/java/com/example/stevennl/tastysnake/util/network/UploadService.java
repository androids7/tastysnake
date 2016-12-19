package com.example.stevennl.tastysnake.util.network;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.TastySnakeApp;
import com.example.stevennl.tastysnake.model.AnalysisData;

/**
 * Service to upload data to remote server. Started in {@link TastySnakeApp#initService()}.
 * Author: QX
 */
public class UploadService extends IntentService {
    private static final String TAG = "UploadService";

    /**
     * Initialize.
     */
    public UploadService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent() called");
        if (!NetworkUtil.isNetworkAvailable(this)) {
            Log.d(TAG, "Network unavailable");
        } else {
            Log.d(TAG, "Network available");
            AnalysisData data = AnalysisData.create(this);
            if (data == null)
                return;
            NetworkUtil networkUtil = NetworkUtil.getInstance(this);
            networkUtil.insertW(data.W, null);
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
