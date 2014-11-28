/** MainActivity.java ---
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

package com.m039.beacon.keeper.activity;

import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.m039.beacon.keeper.foreground_sample.R;
import com.m039.beacon.keeper.receiver.BeaconReceiver;
import com.m039.beacon.keeper.content.BeaconEntity;

/**
 *
 *
 * Created:
 *
 * @author
 * @version
 * @since
 */
public class MainActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
    }

    private BeaconReceiver mBeaconReceiver = new BeaconReceiver() {

            @Override
            protected void onFoundBeacon(Context ctx, BeaconEntity beaconEntity) {
                android.util.Log.d("MainActivity", "onFoundBeacon | " + beaconEntity.getIBeacon());
            }

        };

    @Override
    protected void onStart() {
        super.onStart();

        mBeaconReceiver.registerReceiver(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mBeaconReceiver.unregisterReceiver(this);
    }
    
    
} // MainActivity
