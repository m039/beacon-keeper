/** U.java --- 
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

package com.m039.beacon.keeper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;

import com.m039.beacon.keeper.content.BeaconEntity;

/**
 * 
 *
 * Created: 06/26/14
 *
 * @author Dmitry Mozgin
 * @version 
 * @since 
 */
public class U {

    public static final String TAG = "m039-U";

    public static class BLE {

        /**
         * @return true if ble permissions is set
         */
        public static boolean checkPermissions(Context ctx) {
            PackageManager packageManager = ctx.getPackageManager();
            String packageName = ctx.getPackageName();
            
            for (String permission : new String[] {
                    android.Manifest.permission.BLUETOOTH,
                    android.Manifest.permission.BLUETOOTH_ADMIN,
                }) {
                if (packageManager.checkPermission(permission, packageName) 
                    != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
 
            return true;
        }

        /**
         * @return true if ble features is set
         */
        public static boolean checkFeatures(Context ctx) {
            PackageManager packageManager = ctx.getPackageManager();

            if (packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                return true;
            }
            
            return false;
        }

        /**
         * @return check if ble is enabled
         */
        public static boolean isEnabled(Context ctx) {
            BluetoothAdapter ba = getBluetoothAdapter(ctx);
            return ba != null && ba.isEnabled();
        }

        public static boolean enable(Context ctx) {
            BluetoothAdapter ba = getBluetoothAdapter(ctx);
            return ba != null && ba.enable();
        }

        public static boolean disable(Context ctx) {
            BluetoothAdapter ba = getBluetoothAdapter(ctx);
            return ba != null && ba.disable();
        }
        
        public static Intent createRequestEnableIntent() {
            return new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        }
    }

    public static class Service {
        public static Bundle getMetaData(Context ctx, Class<?> clazz) {
            try {
                return ctx
                    .getPackageManager()
                    .getServiceInfo(new ComponentName(ctx, clazz), PackageManager.GET_META_DATA)
                    .metaData;
            } catch (PackageManager.NameNotFoundException e) {
                Log.wtf(TAG, e);
                return null;
            } catch (NullPointerException e) {
                Log.wtf(TAG, e);
                return null;
            }
        }
    }

    public static class SharedPreferences {
        public static android.content.SharedPreferences getDefaultSharedPreferences(Context ctx) {
            return PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        public static int getInteger(Context ctx, String key, int defValue)  {
            return getInteger(getDefaultSharedPreferences(ctx), key, defValue);
        }

        public static int getInteger(android.content.SharedPreferences sp, String key, int defValue)  {
            try {
                return Integer.parseInt(sp.getString(key, String.valueOf(defValue)));
            } catch (NumberFormatException e) {
                return defValue;
            }
        }
    }

    public static class IBeacon {
        public static float calculateAccuracy(int txPower, int rssi) {
            if (rssi == 0) {
                return -1.0f;
            }

            float ratio = (float) rssi / (float) txPower;
            if (ratio < 1.0) {
                return (float) Math.pow(ratio, 10);
            } else {
                return (0.89976f) * (float) Math.pow(ratio, 7.7095f) + 0.111f;    
            }
        }   

        public static CharSequence getLastUpdate(BeaconEntity beaconEntity) {
            return DateUtils
                .getRelativeTimeSpanString(beaconEntity.getLastSeenTimestamp(),
                                           System.currentTimeMillis(),
                                           DateUtils.SECOND_IN_MILLIS);
        }
    }

    public static BluetoothManager getBluetoothManager(Context ctx) {
        return (BluetoothManager) ctx.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    public static BluetoothAdapter getBluetoothAdapter(Context ctx) {
        BluetoothManager bm = getBluetoothManager(ctx);
        return (bm != null)? bm.getAdapter() : null;
    }


    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 3);
        for(byte b: a)
            sb.append(String.format("%02x ", b & 0xff));

        return sb.toString();
    }
    
} // U
