/** MainReceiver.java ---
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 *
 */

package com.m039.ibeacon.keeper.receiver;

import com.m039.ibeacon.keeper.content.IBeaconEntity;
import com.m039.ibeacon.keeper.service.IBeaconService;


import android.content.Context;
import android.content.Intent;

/**
 *
 *
 * Created:
 *
 * @author
 * @version
 * @since
 */
public class MainReceiver extends com.m039.ibeacon.keeper.receiver.IBeaconReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            IBeaconService.onBootCompleted(context);
            android.util.Log.d("MainReceiver", "onBootCompleted");
        } else {
            super.onReceive(context, intent);
        }
    }

    @Override
    protected void onFoundIBeacon(IBeaconEntity iBeaconEntity) {
        android.util.Log.d("MainReceiver", "onFoundIBeacon | " + iBeaconEntity.getIBeacon());
    }

} // MainReceiver
