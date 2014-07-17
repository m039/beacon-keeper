/** IBeaconReceiver.java ---
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 *
 */

package com.m039.ibeacon.keeper.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.m039.ibeacon.keeper.content.IBeaconEntity;
import com.m039.ibeacon.keeper.service.IBeaconService;

/**
 *
 *
 * Created: 07/01/14
 *
 * @author Dmitry Mozgin
 * @version
 * @since
 */
public class IBeaconReceiver extends BroadcastReceiver
{
    public static final String TAG = "m039-IBeaconReceiver";

    public static Intent registerReceiver(Context ctx, BroadcastReceiver br) {
        IntentFilter filter = new IntentFilter();

        filter.addAction(IBeaconService.ACTION_FOUND_IBEACON);
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

        if (action.equals(IBeaconService.ACTION_FOUND_IBEACON)) {
            onFoundIBeacon((IBeaconEntity) intent
                           .getParcelableExtra(IBeaconService.EXTRA_IBEACON_ENTITY));
        } else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            if (state != -1) {
                onBluetoothStateChanged(state);
            }
        }
    }

    //
    // Todo: add getContext function or Context parameter to function
    // 
    protected void onFoundIBeacon(IBeaconEntity iBeaconEntity) {
        // log
    }

    /**
     * @see android.bluetooth.BluetoothAdapter#ACTION_STATE_CHANGED
     */
    protected void onBluetoothStateChanged(int state) {
    }

} // IBeaconReceiver
