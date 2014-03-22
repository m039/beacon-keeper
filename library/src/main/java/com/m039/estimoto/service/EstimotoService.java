/** EstimotoService.java ---
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 *
 */

package com.m039.estimoto.service;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

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

    private ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;

    private final EstimotoServiceBinder mEstimotoServiceBinder =
        new EstimotoServiceBinder();

    private OnCountListener mOnCountListener = null;
    private volatile int mCount;
    private boolean mStarted = false;
    private Handler mHandler = new Handler();

    public interface OnCountListener {
        public void onCount(int count);
    }

    public class EstimotoServiceBinder extends Binder {
        public void setCountListener(OnCountListener l) {
            mOnCountListener = l;
        }

        public void unsetCountListener() {
            mOnCountListener = null;
        }
    }

    @Override
    public void onCreate() {
        logd("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        logd("Received start id " + startId + ": " + intent);

        String action = intent.getAction();

        if (action.equals(ACTION_CONTROL)) {

            if (intent.getBooleanExtra(EXTRA_TURN_ON, false)) {

                if (!mStarted) {
                    mCount = 0;
                    mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
                    mScheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
                            @Override
                            public void run() {
                                if (mOnCountListener != null) {
                                    mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mOnCountListener.onCount(mCount);
                                            }
                                        });
                                }

                                logd("count = " + mCount);

                                mCount++;
                            }
                        }, 0, 1000L, TimeUnit.MILLISECONDS);
                    mStarted = true;
                }

            } else if (intent.getBooleanExtra(EXTRA_TURN_OFF, false)) {

                if (mStarted) {
                    mScheduledThreadPoolExecutor.shutdown();
                    mScheduledThreadPoolExecutor = null;
                    mCount = 0;
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

        mStarted = false;
        mOnCountListener = null;

        if (mScheduledThreadPoolExecutor != null)  {
            mScheduledThreadPoolExecutor.shutdown();
            mScheduledThreadPoolExecutor = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mEstimotoServiceBinder;
    }

    private void logd(String msg) {
        android.util.Log.d(TAG, msg);
    }

} // EstimotoService
