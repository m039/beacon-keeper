/** MainActivity.java ---
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

import android.os.Bundle;

import com.m039.beacon.keeper.app.R;
import com.m039.beacon.keeper.fragment.SplashFragment;

/**
 *
 *
 * Created:
 *
 * @author Dmitry Mozgin
 * @version 1
 * @since Fri Aug  8 22:57:52 2014
 */
public class MainActivity extends BaseActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        if (savedInstanceState == null) {
            getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment, SplashFragment.newInstance())
                .commit();
        }
    }    
    
} // MainActivity
