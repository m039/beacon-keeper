/** BootReceiver.java --- 
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 * 
 */

package com.m039.ibeacon.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 
 *
 * Created: 07/01/14
 *
 * @author Dmitry Mozgin
 * @version 
 * @since 
 */
public class BootReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Log.d("m039", "**********started************");
    }

} // BootReceiver
