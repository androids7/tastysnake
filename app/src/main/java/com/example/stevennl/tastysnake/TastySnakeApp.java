package com.example.stevennl.tastysnake;

import android.app.Application;
import android.util.Log;

import com.example.stevennl.tastysnake.util.CommonUtil;
import com.example.stevennl.tastysnake.util.SharedPrefUtil;

import java.util.UUID;

public class TastySnakeApp extends Application {
    private static final String TAG = "TastySnakeApp";

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefUtil.loadThemeType(this);
        initDeviceId();
    }

    private void initDeviceId() {
        SharedPrefUtil.loadDeviceId(this);
        if (Config.DEVICE_ID == null) {
            Config.DEVICE_ID = UUID.randomUUID().toString();
            SharedPrefUtil.saveDeviceId(this);
        }
        Log.d(TAG, "Device Id: " + Config.DEVICE_ID);
    }
}
