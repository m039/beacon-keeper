/** BeaconService.java ---
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.m039.beacon.keeper.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.IBinder;

import com.m039.beacon.keeper.L;
import com.m039.beacon.keeper.U;
import com.m039.beacon.keeper.content.BeaconEntity;
import com.m039.beacon.keeper.library.R;
import com.m039.beacon.keeper.util.SimpleLeScanner;

/**
 *
 *
 * Created: 07/01/14
 *
 * @author Dmitry Mozgin
 * @version
 * @since
 */
public class BeaconService extends Service {

    public static final String TAG = "m039-BeaconService";

    public static final String PACKAGE = "com.m039.beacon.keeper.service.";

    public static final String ACTION_FOUND_BEACON = PACKAGE + "action.FOUND_BEACON";
    public static final String ACTION_BLE_DISABLED = PACKAGE + "action.BLE_DISABLED";
    public static final String ACTION_BLE_ENABLED = PACKAGE + "action.BLE_ENABLED";

    public static final String EXTRA_BEACON_ENTITY = PACKAGE + "extra.beacon_entity";

    private static boolean sSharedPreferencesHasChanged = false;
    private static boolean sSharedPreferencesChangeListenerRegistered = false;

    public static void onApplicationCreate(Context ctx) {
        startServiceByAlarmManager(ctx);
    }

    public static void onBootCompleted(Context ctx) {
        startServiceByAlarmManager(ctx);
    }

    public static void startService(Context ctx) {
        ctx.startService(new Intent(ctx, BeaconService.class));
    }

    private static void startServiceByAlarmManager(Context ctx) {
        Intent i = new Intent(ctx, BeaconService.class);
        PendingIntent pi = PendingIntent.getService(ctx, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(),
                        getRepeatTimeMs(ctx),
                        pi);

        if (!sSharedPreferencesChangeListenerRegistered) {
            final String keyScanningTimeMs = ctx
                .getString(R.string.beacon_keeper__pref_key__scanning_time_ms);
            final String keyIdleTimeMs = ctx
                .getString(R.string.beacon_keeper__pref_key__idle_time_ms);

            U.SharedPreferences.getDefaultSharedPreferences(ctx)
                .registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                        @Override
                        public void onSharedPreferenceChanged (SharedPreferences sharedPreferences, String key) {
                            if (key != null &&
                                (key.equals(keyScanningTimeMs) ||
                                 key.equals(keyIdleTimeMs))) {
                                L.d(TAG, "BeaconService.onSharedPreferenceChanged: %s", key);
                                sSharedPreferencesHasChanged = true;

                                // Todo: only check if value is changed
                            }
                        }
                    });

            sSharedPreferencesChangeListenerRegistered = true;
        }
    }

    private static void restartServiceByAlarmManager(Context ctx)  {
        Intent i = new Intent(ctx, BeaconService.class);
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

        String key = res.getString(R.string.beacon_keeper__pref_key__scanning_time_ms);
        int defValue = res.getInteger(R.integer.beacon_keeper__scanning_time_ms_default);

        return U.SharedPreferences.getInteger(ctx, key, defValue);
    }

    private static int getIdleTimeMs(Context ctx) {
        Resources res = ctx.getResources();

        String key = res.getString(R.string.beacon_keeper__pref_key__idle_time_ms);
        int defValue = res.getInteger(R.integer.beacon_keeper__idle_time_ms_default);

        return U.SharedPreferences.getInteger(ctx, key, defValue);
    }

    private static int getRepeatTimeMs(Context ctx) {
        return getIdleTimeMs(ctx) + getScanningTimeMs(ctx);
    }

    private SimpleLeScanner mSimpleLeScanner = null;
    private SimpleLeScanner.LeScanCallback mLeScanCallback = new SimpleLeScanner.LeScanCallback() {

            @Override
            public void onLeScan(BeaconEntity beaconEntity) {
                sendFoundBeaconBroadcast(beaconEntity);
            }

        };

    private long mRunningTimeDebug = 0;
    private Handler mHandler;
    private boolean mStartedSuccessfully = false;

    @Override
    public void onCreate() {
        super.onCreate();

        HandlerThread ht = new HandlerThread("HandlerThread [" + BeaconService.class.getSimpleName() + "]");
        ht.start();

        mRunningTimeDebug = System.currentTimeMillis();
        mSimpleLeScanner = new SimpleLeScanner(this);

        (mHandler = new Handler(ht.getLooper()))
            .post(new Runnable() {

                @Override
                public void run() {
                    if (mStartedSuccessfully = mSimpleLeScanner.startScan(mLeScanCallback)) {
                        stopScan(false);
                    } else {
                        stopScan(true);
                    }
                }

            });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        L.d(TAG, "onDestroy: startTime %s runningTime %s",
            mRunningTimeDebug, System.currentTimeMillis() - mRunningTimeDebug);

        stopScan(true);

        if (sSharedPreferencesHasChanged) {
            restartServiceByAlarmManager(this);
            sSharedPreferencesHasChanged = false;
        }
    }

    private void stopScan(boolean force) {
        if (mHandler != null) {
            if (force) {
                mHandler.post(mOnStopScanRunnable);
            } else {
                mHandler.postDelayed(mOnStopScanRunnable, getScanningTimeMs(this));
            }

            mHandler.getLooper().quit();
            mHandler = null;
        }
    }

    private Runnable mOnStopScanRunnable = new Runnable() {

            @Override
            public void run() {
                if (mSimpleLeScanner != null) {
                    if (mStartedSuccessfully) {
                        mSimpleLeScanner.stopScan(mLeScanCallback);
                    }
                    mSimpleLeScanner = null;

                    stopSelf();
                }
            }

        };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    private void sendFoundBeaconBroadcast(final BeaconEntity beaconEntity) {
        final Context ctx = getApplicationContext();

        runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setAction(ACTION_FOUND_BEACON);
                    intent.putExtra(EXTRA_BEACON_ENTITY, beaconEntity);
                    ctx.sendBroadcast(intent);
                }

            });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static void runOnUiThread(Runnable run) {
        new Handler(Looper.getMainLooper()).post(run);
    }

    private static void runOnUiThread(Runnable run, long delayMillis) {
        new Handler(Looper.getMainLooper()).postDelayed(run, delayMillis);
    }

} // BeaconService
