package com.example.stevennl.tastysnake.util.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.stevennl.tastysnake.TastySnakeApp;

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
            // TODO UploadService: Send latest W value to remote server
        }
    }

    /**
     * Set the service alarm to start the service repeatedly.
     *
     * @param context The context
     * @param on True to start the alarm, false to stop it.
     */
    public static void setAlarm(Context context, boolean on) {
        // TODO UploadService: setAlarm()
    }

    /**
     * Return true if the service alarm is on.
     */
    public static boolean isAlarmOn() {
        // TODO UploadService: isAlarmOn()
        return false;
    }
}
