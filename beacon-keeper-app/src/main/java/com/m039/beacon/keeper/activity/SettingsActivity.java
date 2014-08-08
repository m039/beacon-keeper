/** SettingsActivity.java ---
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
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.m039.beacon.keeper.app.R;

/**
 *
 *
 * Created:
 *
 * @author
 * @version
 * @since
 */
public class SettingsActivity extends BaseActivity {

    public static void startActivity(Context ctx) {
        ctx.startActivity(new Intent(ctx, SettingsActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager()
            .beginTransaction()
            .replace(android.R.id.content, new SettingsFragment())
            .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }

    }
    
} // SettingsActivity
