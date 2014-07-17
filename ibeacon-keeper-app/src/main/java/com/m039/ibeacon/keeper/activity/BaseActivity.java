/** BaseActivity.java ---
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

package com.m039.ibeacon.keeper.activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

/**
 *
 *
 * Created: 06/17/14
 *
 * @author Dmitry Mozgin
 * @version
 * @since
 */
public class BaseActivity extends Activity {

    private static final int WHAT_PERIODIC_UPDATE = 0;

    private Handler mHandler = new Handler() {
            @Override
            public void handleMessage (Message msg) {
                if (msg.what == WHAT_PERIODIC_UPDATE) {
                    onPeriodicUpdate();
                    sendEmptyMessageDelayed(msg.what, 1000);
                }
            }
        };

    @Override
    protected void onStart() {
        super.onStart();

        startHandleMessages();
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopHandleMessages();
    }    

    private void startHandleMessages() {
        mHandler.sendEmptyMessage(WHAT_PERIODIC_UPDATE);
    }

    private void stopHandleMessages() {
        mHandler.removeMessages(WHAT_PERIODIC_UPDATE);
    }

    protected void onPeriodicUpdate() {
    }

} // BaseActivity
