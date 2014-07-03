/** OnEnableBluetoothCallback.java --- 
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 * 
 */

package com.m039.ibeacon.keeper.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.m039.ibeacon.keeper.U;

/**
 * 
 *
 * Created: 06/26/14
 *
 * @author Dmitry Mozgin
 * @version 
 * @since 
 */
public abstract class OnEnableBluetoothCallback {

    private int mRequestCode = -1;

    public void onCreate(Activity a, Bundle savedInstanceState, int requestCode) {
        mRequestCode = requestCode;

        if (U.BLE.checkPermissions(a) &&
            U.BLE.checkFeatures(a)) {

            if (U.BLE.isEnabled(a)) {
                onBluetoothEnabled();
            } else {
                a.startActivityForResult(U.BLE.createRequestEnableIntent(), requestCode);
            }

        } else {
            onBluetoothDisabled();
        }
    }

    public void onActivityResult (Activity a, int requestCode, int resultCode, Intent data) {
        if (requestCode == mRequestCode &&
            resultCode == Activity.RESULT_OK) {
            onBluetoothEnabled();
        } else {
            onBluetoothDisabled();
        }
    }

    abstract public void onBluetoothEnabled();
    abstract public void onBluetoothDisabled();
    

} // OnEnableBluetoothCallback
