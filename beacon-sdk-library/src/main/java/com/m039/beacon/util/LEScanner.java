/** LEScanner.java --- 
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 * 
 */

package com.m039.beacon.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Handler;

import com.m039.beacon.U;

/**
 * 
 *
 * Created: 
 *
 * @author 
 * @version 
 * @since 
 */
public class LEScanner {
    final private long mPeriod;
    final private Handler mHandler = new Handler();

    public LEScanner() {
        this(1000L);
    }

    public LEScanner(long period) {
        mPeriod = period;
    }

    private Runnable mOnContinueScanRunnable = null;

    public boolean startScan(final Context ctx, final BluetoothAdapter.LeScanCallback callback) {
        BluetoothAdapter ba = U.getBluetoothAdapter(ctx);
        if (ba != null && mOnContinueScanRunnable == null) {
            if (ba.startLeScan(callback)) {
                onStartScan();

                mOnContinueScanRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if (!onContinueScan()) {
                                stopScan(ctx, callback);
                            } else {
                                // rescan
                                mHandler.postDelayed(this, mPeriod);
                            }
                        }
                    };

                mHandler.postDelayed(mOnContinueScanRunnable, mPeriod);

                return true;

            } else {

                onStopScan();

            }
        }

        return false;
    }

    public void stopScan(Context ctx, BluetoothAdapter.LeScanCallback callback) {
        BluetoothAdapter ba = U.getBluetoothAdapter(ctx);
        if (ba != null && mOnContinueScanRunnable != null) {
            mHandler.removeCallbacks(mOnContinueScanRunnable);
            mOnContinueScanRunnable = null;
            ba.stopLeScan(callback);
        }

        onStopScan();
    }

    protected void onStartScan() {
    }

    protected boolean onContinueScan() {
        return true;
    }

    protected void onStopScan() {
    }
} // LEScanner
