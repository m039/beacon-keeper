/** L.java --- 
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 * 
 */

package com.m039.ibeacon.keeper;

import android.util.Log;

/**
 * 
 *
 * Created: 07/03/14
 *
 * @author Dmitry Mozgin
 * @version 
 * @since 
 */
public class L {

    public static final String TAG = "m039";

    public static int d (String msg) {
        return d(TAG, msg);
    }

    public static int d (String tag, String msg) {
        return d(tag, msg, (Object[]) null);
    }

    public static int d(String tag, String fmt, Object ... args) {
        if (C.DEBUG) {
            return Log.d(tag, String.format(fmt, args));
        }

        return -1;        
    }

    public static int wtf(String tag, String msg) {
        if (C.DEBUG) {
            return Log.wtf(tag, msg);
        }

        return -1;
    }
    
} // L
