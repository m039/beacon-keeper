/** IBeaconService.java ---
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 *
 */

package com.m039.ibeacon.keeper.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.m039.ibeacon.keeper.content.IBeacon;
import com.m039.ibeacon.keeper.library.R;
import com.m039.ibeacon.keeper.U;
import com.m039.ibeacon.keeper.util.SimpleLeScanner;

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

    public static final String TAG = "m039";

    public static final String PACKAGE = "com.m039.ibeacon.keeper.service.";
    
    public static final String ACTION_FOUND_IBEACON = PACKAGE + "action.FOUND_IBEACON";
    public static final String ACTION_BLE_DISABLED = PACKAGE + "action.BLE_DISABLED";
    public static final String ACTION_BLE_ENABLED = PACKAGE + "action.BLE_ENABLED";

    public static final String EXTRA_BEACON = PACKAGE + "extra.beacon";

    public static final String META_SCANNING_TIME_MS = "scanning_time_ms";


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
                sendFoundBeaconBroadcast(ibeacon);
            }
        };

    @Override
    public void onCreate() {
        super.onCreate();

        if (U.BLE.isEnabled(this)) {
            sendBleEnabledBroadcast();
        } else {
            sendBleDisabledBroadcast();
        }

        mSimpleLeScanner = new SimpleLeScanner();
        if (mSimpleLeScanner.startScan(this, mLeScanCallback)) {
            mHandler.postDelayed(mOnStopScanRunnable, getScanningTimeMs(this));
        } else {
            mOnStopScanRunnable.run();
        }
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
    public void onDestroy() {
        super.onDestroy();
        mSimpleLeScanner = null;
    }

    private void sendFoundBeaconBroadcast(IBeacon ibeacon) {
        Intent intent = new Intent();
        intent.setAction(ACTION_FOUND_IBEACON);
        intent.setPackage(getPackageName());
        intent.putExtra(EXTRA_BEACON, ibeacon);
        sendBroadcast(intent); 
    }

    private void sendBleEnabledBroadcast() {
        Intent intent = new Intent();
        intent.setAction(ACTION_BLE_ENABLED);
        intent.setPackage(getPackageName());
        sendBroadcast(intent); 
    }
    
    private void sendBleDisabledBroadcast() {
        Intent intent = new Intent();
        intent.setAction(ACTION_BLE_DISABLED);
        intent.setPackage(getPackageName());
        sendBroadcast(intent); 
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

} // IBeaconService
