/** MainApplication.java ---
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

package com.m039.beacon.keeper.app;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.m039.beacon.keeper.service.BeaconService;

/**
 *
 *
 * Created: 03/22/14
 *
 * @author Dmitry Mozgin
 * @version
 * @since
 */
public class MainApplication extends Application {

    @Override
    public void onCreate () {
        super.onCreate ();

        BeaconService.onApplicationCreate(this);

        CalligraphyConfig.initDefault("fonts/Intro-Regular.otf", R.attr.fontPath);
    }

    Tracker mTracker = null;

    public synchronized Tracker getTracker() {
        if (mTracker == null) {
            mTracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.google_analytics_tracker);
        }

        return mTracker;
    }


} // MainApplication
