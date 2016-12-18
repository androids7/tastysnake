package com.example.stevennl.tastysnake.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.stevennl.tastysnake.Config;

/**
 * Manage operations for {@link SharedPreferences}.
 * Author: LCY
 */
public class SharedPrefUtil {
    private static final String TAG = "SharedPrefUtil";
    private static final String PREF_APP = "pref_conf";
    private static final String PREF_KEY_THEME_TYPE = "theme_type";
    private static final String PREF_KEY_DEVICE_ID = "device_id";

    /**
     * Save current theme type.
     */
    public static void saveThemeType(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE);
        sp.edit().putInt(PREF_KEY_THEME_TYPE, Config.theme.ordinal()).apply();
    }

    /**
     * Load current theme type.
     */
    public static void loadThemeType(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE);
        int type = sp.getInt(PREF_KEY_THEME_TYPE, Config.ThemeType.LIGHT.ordinal());
        Config.theme = Config.ThemeType.values()[type];
    }

    /**
     * Save device id.
     */
    public static void saveDeviceId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE);
        sp.edit().putString(PREF_KEY_DEVICE_ID, Config.DEVICE_ID).apply();
    }

    /**
     * Load device id.
     */
    public static void loadDeviceId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE);
        Config.DEVICE_ID = sp.getString(PREF_KEY_DEVICE_ID, null);
    }
}
