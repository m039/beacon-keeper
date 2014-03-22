/** EstimotoServiceUtil.java ---
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 *
 */

package com.m039.estimoto.util;

import android.content.Context;
import android.content.Intent;

import com.m039.estimoto.service.EstimotoService;

/**
 *
 *
 * Created: 03/22/14
 *
 * @author Dmitry Mozgin
 * @version
 * @since
 */
public class EstimotoServiceUtil {

    public static void turnOn(Context ctx) {
        Intent intent = newEstimotoIntent(ctx, EstimotoService.ACTION_CONTROL);
        intent.putExtra(EstimotoService.EXTRA_TURN_ON, true);
        ctx.startService(intent);
    }

    public static void turnOff(Context ctx) {
        Intent intent = newEstimotoIntent(ctx, EstimotoService.ACTION_CONTROL);
        intent.putExtra(EstimotoService.EXTRA_TURN_OFF, true);
        ctx.startService(intent);
        ctx.stopService(intent); // hack
    }

    private static Intent newEstimotoIntent(Context ctx, String action) {
        Intent intent = new Intent(ctx, EstimotoService.class);
        intent.setAction(action);
        return intent;
    }

} // EstimotoServiceUtil
