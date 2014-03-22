/** EstimotoService.java ---
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 *
 */

package com.m039.estimoto.service;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

/**
 *
 *
 * Created: 03/22/14
 *
 * @author Dmitry Mozgin
 * @version
 * @since
 */
public class EstimotoService extends Service {

    static String TAG = "m039-EstimotoService";

    static private String PACKAGE = "com.m039.estimoto.service";

    public static final String ACTION_CONTROL = PACKAGE + "action.CONTROL";

    public static final String EXTRA_TURN_ON = PACKAGE + "extra.TURN_ON";
    public static final String EXTRA_TURN_OFF = PACKAGE + "extra.TURN_OFF";

    private final EstimotoServiceBinder mEstimotoServiceBinder =
        new EstimotoServiceBinder();

    private BeaconManager.RangingListener mRangingListener = null;
    private boolean mStarted = false;
    private Handler mHandler = new Handler();
    private BeaconManager mBeaconManager;

    public class EstimotoServiceBinder extends Binder {
        public void setOnBeaconsDiscoveredListener(BeaconManager.RangingListener l) {
            mRangingListener = l;
        }

        public void unsetOnBeaconsDiscoveredListener() {
            mRangingListener = null;
        }
    }

    @Override
    public void onCreate() {
        logd("onCreate");

        beaconOnCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        logd("Received start id " + startId + ": " + intent);

        String action = intent.getAction();

        if (action.equals(ACTION_CONTROL)) {

            if (intent.getBooleanExtra(EXTRA_TURN_ON, false)) {

                if (!mStarted) {
                    beaconTurnOn();
                    mStarted = true;
                }

            } else if (intent.getBooleanExtra(EXTRA_TURN_OFF, false)) {

                if (mStarted) {
                    beaconTurnOff();
                    mStarted = false;
                }
            }

        } else {
            throw new IllegalArgumentException("You must supply action!");
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        logd("onDestroy");

        beaconDestroy();
        mStarted = false;
        mRangingListener = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mEstimotoServiceBinder;
    }

    //
    // Beacon functions
    //

    private static final String ESTIMOTE_PROXIMITY_UUID =
        "B9407F30-F5F8-466E-AFF9-25556B57FE6D";

    private static final Region ALL_ESTIMOTE_BEACONS =
        new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);

    private void beaconOnCreate() {
        mBeaconManager = new BeaconManager(this);
        mBeaconManager.setRangingListener(new BeaconManager.RangingListener() {
                
                int logTimes = 0;
                
                @Override
                public void onBeaconsDiscovered(final Region region, final List<Beacon> beacons) {
                    logd("onBeaconsDiscovered, times = " + logTimes++);

                    mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mRangingListener != null) {
                                    mRangingListener.onBeaconsDiscovered(region, beacons);
                                }
                            }
                        });
                }
            });
    }

    private void beaconTurnOn() {
        mBeaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override public void onServiceReady() {
                    try {
                        mBeaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
                    } catch (RemoteException e) {
                        android.util.Log.e(TAG, "Cannot start ranging", e);
                    }
                }
            });
    }

    private void beaconTurnOff() {
        try {
            mBeaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
        } catch (RemoteException e) {
            android.util.Log.e(TAG, "Cannot stop but it does not matter now", e);
        }

        mBeaconManager.disconnect();
    }

    private void beaconDestroy() {
    }

    //
    // Misc
    // 
    
    private void logd(String msg) {
        android.util.Log.d(TAG, msg);
    }

} // EstimotoService
