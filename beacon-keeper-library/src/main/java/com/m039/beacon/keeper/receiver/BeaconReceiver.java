/** BeaconReceiver.java ---
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

package com.m039.beacon.keeper.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.m039.beacon.keeper.content.BeaconEntity;
import com.m039.beacon.keeper.service.BeaconService;

/**
 *
 *
 * Created: 07/01/14
 *
 * @author Dmitry Mozgin
 * @version
 * @since
 */
public class BeaconReceiver extends BroadcastReceiver
{
    public static final String TAG = "m039-BeaconReceiver";

    public static Intent registerReceiver(Context ctx, BroadcastReceiver br) {
        IntentFilter filter = new IntentFilter();

        filter.addAction(BeaconService.ACTION_FOUND_BEACON);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        return ctx.registerReceiver(br, filter);
    }

    public static void unregisterReceiver(Context ctx, BroadcastReceiver br) {
        ctx.unregisterReceiver(br);
    }

    public Intent registerReceiver(Context ctx) {
        return registerReceiver(ctx, this);
    }

    public void unregisterReceiver(Context ctx) {
        unregisterReceiver(ctx, this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(BeaconService.ACTION_FOUND_BEACON)) {
            onFoundBeacon(context, (BeaconEntity) intent.getParcelableExtra(BeaconService.EXTRA_BEACON_ENTITY));
        } else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            if (state != -1) {
                onBluetoothStateChanged(context, state);
            }
        }
    }

    protected void onFoundBeacon(Context ctx, BeaconEntity beaconEntity) {
    }

    /**
     * @see android.bluetooth.BluetoothAdapter#ACTION_STATE_CHANGED
     */
    protected void onBluetoothStateChanged(Context ctx, int state) {
    }

} // BeaconReceiver
