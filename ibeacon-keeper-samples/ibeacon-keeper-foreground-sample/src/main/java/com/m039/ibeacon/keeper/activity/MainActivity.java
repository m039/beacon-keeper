/** MainActivity.java ---
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 *
 */

package com.m039.ibeacon.keeper.activity;

import com.m039.ibeacon.keeper.foreground_sample.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.m039.ibeacon.keeper.receiver.IBeaconReceiver;
import com.m039.ibeacon.keeper.content.IBeaconEntity;

/**
 *
 *
 * Created:
 *
 * @author
 * @version
 * @since
 */
public class MainActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
    }

    private IBeaconReceiver mIBeaconReceiver = new IBeaconReceiver() {

            @Override
            protected void onFoundIBeacon(IBeaconEntity iBeaconEntity) {
                android.util.Log.d("MainActivity", "onFoundIBeacon | " + iBeaconEntity.getIBeacon());
            }

        };

    @Override
    protected void onStart() {
        super.onStart();

        mIBeaconReceiver.registerReceiver(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mIBeaconReceiver.unregisterReceiver(this);
    }
    
    
} // MainActivity
