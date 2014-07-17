/** MainApplication.java ---
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 *
 */

package com.m039.ibeacon.keeper.app;

import android.app.Application;

import com.m039.ibeacon.keeper.service.IBeaconService;

/**
 *
 *
 * Created:
 *
 * @author
 * @version
 * @since
 */
public class MainApplication extends Application {

    @Override
    public void onCreate () {
        super.onCreate ();

        IBeaconService.onApplicationCreate(this);
    }
    
} // MainApplication
