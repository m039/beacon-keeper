/** SettingsA.java ---
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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.View;

import com.m039.beacon.keeper.app.R;

/**
 *
 *
 * Created:
 *
 * @author Dmitry Mozgin
 * @version 1
 * @since Sat Dec 20 08:11:50 2014
 */
public class SettingsA extends BaseActivity {
    
    public static void startActivity(Activity a) {
        a.startActivity(new Intent(a, SettingsA.class));
        a.overridePendingTransition (R.anim.enter_in, R.anim.enter_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_settings);

        if (savedInstanceState == null) {
            getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, new SettingsFragment())
                .commit();
        }

        findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AboutA.startActivity(SettingsA.this);
                }
            });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition (R.anim.finish_in, R.anim.finish_out);
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }

    }
    
} // SettingsA
