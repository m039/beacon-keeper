/** L.java --- 
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
