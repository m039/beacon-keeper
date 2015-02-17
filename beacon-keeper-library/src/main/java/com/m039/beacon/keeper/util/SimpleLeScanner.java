/** SimpleLeScanner.java --- 
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

package com.m039.beacon.keeper.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.m039.beacon.keeper.U;
import com.m039.beacon.keeper.L;
import com.m039.beacon.keeper.content.BeaconEntity;
import com.m039.beacon.keeper.content.BeaconFactory;

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
            BeaconEntity beaconEntity = BeaconFactory.decode(device, rssi, scanRecord);
            if (beaconEntity != null) {
                onLeScan(beaconEntity);
            }
        }

        public abstract void onLeScan(BeaconEntity beaconEntity);
    }

    private boolean mIsScanning = false;
    final private Context mContext;

    public SimpleLeScanner(Context ctx) {
        mContext = ctx.getApplicationContext();
    }

    public boolean startScan(LeScanCallback callback) {
        BluetoothAdapter ba = U.getBluetoothAdapter(mContext);
        if (ba != null && ba.isEnabled() && !mIsScanning && callback != null) {
            if (ba.startLeScan(callback)) {
                mIsScanning = true;
                onStartScan();
                return true;
            }
        }

        L.wtf(TAG, "Failed to startScan");

        return false;
    }

    public void stopScan(LeScanCallback callback) {
        BluetoothAdapter ba = U.getBluetoothAdapter(mContext);
        if (ba != null && mIsScanning && callback != null) {
            ba.stopLeScan(callback);
            mIsScanning = false;
            onStopScan();
        } else {
            L.wtf(TAG, "Failed to stopScan");
        }
    }

    protected void onStartScan() {
    }

    protected void onStopScan() {
    }

} // SimpleLeScanner
