/** OnEnableBluetoothCallback.java --- 
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
