/** MainReceiver.java ---
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
