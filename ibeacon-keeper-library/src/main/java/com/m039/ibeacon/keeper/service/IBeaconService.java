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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;

import com.m039.ibeacon.keeper.L;
import com.m039.ibeacon.keeper.U;
import com.m039.ibeacon.keeper.content.IBeaconEntity;
import com.m039.ibeacon.keeper.library.R;
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

    public static final String EXTRA_IBEACON_ENTITY = PACKAGE + "extra.ibeacon_entity";

    private static boolean sSharedPreferencesHasChanged = false;
    private static boolean sSharedPreferencesChangeListenerRegistered = false;

    public static void onApplicationCreate(Context ctx) {
        startServiceByAlarmManager(ctx);
    }

    public static void startService(Context ctx) {
        ctx.startService(new Intent(ctx, IBeaconService.class));
    }

    private static void startServiceByAlarmManager(Context ctx) {
        Intent i = new Intent(ctx, IBeaconService.class);
        PendingIntent pi = PendingIntent.getService(ctx, 0, i, 0);

        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(),
                        getRepeatTimeMs(ctx),
                        pi);

        if (!sSharedPreferencesChangeListenerRegistered) {
            final String keyScanningTimeMs = ctx
                .getString(R.string.ibeacon_keeper__pref_key__scanning_time_ms);
            final String keyIdleTimeMs = ctx
                .getString(R.string.ibeacon_keeper__pref_key__idle_time_ms);

            U.SharedPreferences.getDefaultSharedPreferences(ctx)
                .registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                        @Override
                        public void onSharedPreferenceChanged (SharedPreferences sharedPreferences, String key) {
                            if (key != null &&
                                (key.equals(keyScanningTimeMs) ||
                                 key.equals(keyIdleTimeMs))) {
                                L.d(TAG, "IBeaconService.onSharedPreferenceChanged: %s", key);
                                sSharedPreferencesHasChanged = true;

                                // Todo: only check if value is changed
                            }
                        }
                    });

            sSharedPreferencesChangeListenerRegistered = true;
        }
    }

    private static void restartServiceByAlarmManager(Context ctx)  {
        Intent i = new Intent(ctx, IBeaconService.class);
        PendingIntent pi = PendingIntent.getService(ctx, 0, i, PendingIntent.FLAG_NO_CREATE);

        if (pi != null) {
            // alarm is set => restart

            AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP,
                            System.currentTimeMillis(),
                            getRepeatTimeMs(ctx),
                            pi);

        } else {
            // alarm is not set => do nothing
        }
    }

    private static int getScanningTimeMs(Context ctx) {
        Resources res = ctx.getResources();

        String key = res.getString(R.string.ibeacon_keeper__pref_key__scanning_time_ms);
        int defValue = res.getInteger(R.integer.ibeacon_keeper__scanning_time_ms_default);

        return U.SharedPreferences.getInteger(ctx, key, defValue);
    }

    private static int getIdleTimeMs(Context ctx) {
        Resources res = ctx.getResources();

        String key = res.getString(R.string.ibeacon_keeper__pref_key__idle_time_ms);
        int defValue = res.getInteger(R.integer.ibeacon_keeper__idle_time_ms_default);

        return U.SharedPreferences.getInteger(ctx, key, defValue);
    }

    private static int getRepeatTimeMs(Context ctx) {
        return getIdleTimeMs(ctx) + getScanningTimeMs(ctx);
    }

    private Handler mHandler = new Handler();

    private SimpleLeScanner mSimpleLeScanner = null;
    private SimpleLeScanner.LeScanCallback mLeScanCallback = new SimpleLeScanner.LeScanCallback() {
            @Override
            public void onLeScan(IBeaconEntity iBeaconEntity) {
                sendFoundBeaconBroadcast(iBeaconEntity);
            }
        };

    private long mRunningTimeDebug = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        mRunningTimeDebug = System.currentTimeMillis();

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

        L.d(TAG, "onDestroy: startTime %s runningTime %s",
            mRunningTimeDebug, System.currentTimeMillis() - mRunningTimeDebug);

        mSimpleLeScanner = null;

        if (sSharedPreferencesHasChanged) {
            restartServiceByAlarmManager(this);
            sSharedPreferencesHasChanged = false;
        }
    }

    private void sendFoundBeaconBroadcast(IBeaconEntity iBeaconEntity) {
        Intent intent = new Intent();
        intent.setAction(ACTION_FOUND_IBEACON);
        intent.setPackage(getPackageName());
        intent.putExtra(EXTRA_IBEACON_ENTITY, iBeaconEntity);
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

} // IBeaconService
