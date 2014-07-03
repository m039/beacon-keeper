/** BaseActivity.java ---
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
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
                    sendEmptyMessageDelayed(msg.what, 500);
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

    private void stopHandleMessages() {
        mHandler.removeMessages(WHAT_PERIODIC_UPDATE);
    }

    private void startHandleMessages() {
        mHandler.sendEmptyMessage(WHAT_PERIODIC_UPDATE);
    }

    protected void onPeriodicUpdate() {
    }

} // BaseActivity
