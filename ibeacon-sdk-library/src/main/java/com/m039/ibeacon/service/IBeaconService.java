/** IBeaconService.java --- 
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 * 
 */

package com.m039.ibeacon.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.m039.ibeacon.content.IBeacon;
import com.m039.ibeacon.library.R;
import com.m039.ibeacon.util.SimpleLeScanner;

/**
 * 
 *
 * Created: 07/01/14
 *
 * @author Dmitry Mozgin
 * @version 
 * @since 
 */
public class IBeaconService extends Service {

    public static final String META_SCANNING_TIME_MS = "scanning_time_ms";
    public static final String TAG = "m039";

    public static void startService(Context ctx) {
        ctx.startService(new Intent(ctx, IBeaconService.class));
    }

    public static void startServiceByAlarmManager(Context ctx) {
        Intent i = new Intent(ctx, IBeaconService.class);
        PendingIntent pi = PendingIntent.getService(ctx, 0, i, 0);

        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, 
                        System.currentTimeMillis(), 
                        getRepeatTimeMs(ctx),
                        pi);
    }

    private static int getScanningTimeMs(Context ctx) {
        return ctx.getResources().getInteger(R.integer.beacon_service__scanning_time_ms);
    }

    private static int getIdleTimeMs(Context ctx) {
        return ctx.getResources().getInteger(R.integer.beacon_service__idle_time_ms);
    }

    private static int getRepeatTimeMs(Context ctx) {
        return getIdleTimeMs(ctx) + getScanningTimeMs(ctx);
    }

    private Handler mHandler = new Handler();

    private SimpleLeScanner mSimpleLeScanner = null;
    private SimpleLeScanner.LeScanCallback mLeScanCallback = new SimpleLeScanner.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, IBeacon ibeacon) {
                Log.d(TAG, "onLeScan, b : " + ibeacon);
            }
        };

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");

        mSimpleLeScanner = new SimpleLeScanner();
        mSimpleLeScanner.startScan(this, mLeScanCallback);

        mHandler.postDelayed(mOnStopScanRunnable, getScanningTimeMs(this));
    }

    private Runnable mOnStopScanRunnable = new Runnable() {
            @Override
            public void run() {
                if (mSimpleLeScanner != null) {
                    mSimpleLeScanner.stopScan(IBeaconService.this, mLeScanCallback);
                }
                stopSelf();
            }
        };

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Log.d(TAG, "Received start id " + startId + ": " + intent);        
        
        return START_STICKY;
    }    

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        mSimpleLeScanner = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

} // IBeaconService
