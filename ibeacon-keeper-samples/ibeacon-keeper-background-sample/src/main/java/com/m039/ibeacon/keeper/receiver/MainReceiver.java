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
import com.m039.ibeacon.keeper.receiver.IBeaconReceiver;

/**
 *
 *
 * Created:
 *
 * @author
 * @version
 * @since
 */
public class MainReceiver extends IBeaconReceiver {

    @Override
    protected void onFoundIBeacon(IBeaconEntity iBeaconEntity) {
        android.util.Log.d("MainReceiver", "onFoundIBeacon | " + iBeaconEntity.getIBeacon());
    }

} // MainReceiver
