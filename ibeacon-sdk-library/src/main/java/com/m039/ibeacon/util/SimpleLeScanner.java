/** SimpleLeScanner.java --- 
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 * 
 */

package com.m039.ibeacon.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.m039.ibeacon.U;
import com.m039.ibeacon.content.IBeacon;
import com.m039.ibeacon.content.IBeaconFactory;

/**
 * 
 *
 * Created: 
 *
 * @author 
 * @version 
 * @since 
 */
public class SimpleLeScanner {

    public static final String TAG = "m039-SimpleLeScanner";

    public static abstract class LeScanCallback 
        implements BluetoothAdapter.LeScanCallback {
        @Override
        public void onLeScan (BluetoothDevice device, int rssi, byte[] scanRecord) {
            IBeacon ibeacon = IBeaconFactory.decodeScanRecord(scanRecord);
            if (ibeacon != null) {
                onLeScan(device, rssi, ibeacon);
            }
        }

        public abstract void onLeScan(BluetoothDevice device, int rssi, IBeacon ibeacon);
    }

    final private long mScanningTimeMs;
    final private Handler mHandler = new Handler();

    private boolean mIsScanning = false;
    private Runnable mOnStopScanRunnable = null;

    public SimpleLeScanner() {
        mScanningTimeMs = -1;
    }

    public SimpleLeScanner(long scanningTimeMs) {
        mScanningTimeMs = scanningTimeMs;
    }

    public boolean startScan(final Context ctx, final LeScanCallback callback) {
        BluetoothAdapter ba = U.getBluetoothAdapter(ctx);
        if (ba != null && ba.isEnabled() && !mIsScanning) {
            if (ba.startLeScan(callback)) {
                mIsScanning = true;
                onStartScan();

                if (mScanningTimeMs != -1) {
                    mOnStopScanRunnable = new Runnable() {
                            @Override
                            public void run() {
                                stopScan(ctx, callback);
                            }
                        };

                    mHandler.postDelayed(mOnStopScanRunnable, mScanningTimeMs);
                }

                return true;
            }
        }

        Log.wtf(TAG, "Failed to startScan");

        return false;
    }

    public void stopScan(Context ctx, LeScanCallback callback) {
        BluetoothAdapter ba = U.getBluetoothAdapter(ctx);
        if (ba != null && mIsScanning) {
            if (mOnStopScanRunnable != null) {
                mHandler.removeCallbacks(mOnStopScanRunnable);
                mOnStopScanRunnable = null;
            }

            ba.stopLeScan(callback);
            onStopScan();
            mIsScanning = false;
        } else {
            Log.wtf(TAG, "Failed to stopScan");
        }
    }

    protected void onStartScan() {
    }

    protected void onStopScan() {
    }

} // SimpleLeScanner
