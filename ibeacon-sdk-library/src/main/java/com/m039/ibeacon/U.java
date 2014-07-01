/** U.java --- 
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 * 
 */

package com.m039.ibeacon;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

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
