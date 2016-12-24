package com.example.stevennl.tastysnake.controller.game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;

import com.example.stevennl.tastysnake.base.SingleFragmentActivity;
import com.example.stevennl.tastysnake.util.network.UploadService;

/**
 * Activity controlling the game.
 */
public class GameActivity extends SingleFragmentActivity {
    private static final String TAG = "GameActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!UploadService.isAlarmOn(this)) {
            UploadService.setAlarm(this, true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (UploadService.isAlarmOn(this)) {
            UploadService.setAlarm(this, false);
        }
    }

    @Override
    protected Fragment createFragment() {
        return new HomeFragment();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(getFrameLayoutId());
                String className = fragment.getClass().getName();
                if (className.equals(HomeFragment.class.getName())) {
                    ((HomeFragment)fragment).onBackPressed();
                } else if (className.equals(ConnectFragment.class.getName())) {
                    ((ConnectFragment)fragment).onBackPressed();
                } else if (className.equals(BattleFragment.class.getName())) {
                    ((BattleFragment)fragment).onBackPressed();
                } else {
                    replaceFragment(new HomeFragment(), true);
                }
                break;
            default:
                break;
        }
        return false;
    }
}
