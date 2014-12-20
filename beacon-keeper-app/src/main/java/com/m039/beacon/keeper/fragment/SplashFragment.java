/** SplashFragment.java ---
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

package com.m039.beacon.keeper.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.m039.beacon.keeper.U;
import com.m039.beacon.keeper.app.R;

/**
 *
 *
 * Created:
 *
 * @author Dmitry Mozgin
 * @version 1
 * @since Fri Aug  8 23:05:19 2014
 */
public class SplashFragment extends BaseFragment {

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Switch mSwitcher;
    private View mSwitcherPb;

    private ViewGroup mEnabled;
    private ViewGroup mDisabled;
    private TextView mDisabledLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_splash, parent, false);

        mEnabled = (ViewGroup) view.findViewById(R.id.enabled);
        mDisabled = (ViewGroup) view.findViewById(R.id.disabled);

        mDisabledLabel = (TextView) mDisabled.findViewById(R.id.disabled_label);

        mSwitcherPb = mDisabled.findViewById(R.id.switcher_pb);

        mSwitcher = (Switch) mDisabled.findViewById(R.id.switcher);
        mSwitcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged (CompoundButton v, boolean isChecked) {
                    Context ctx = v.getContext();

                    if (v.isChecked() && !U.BLE.isEnabled(ctx)) {
                        U.BLE.enable(ctx);
                        v.setEnabled(false);
                    } else if (!v.isChecked() && U.BLE.isEnabled(ctx)) {
                        U.BLE.disable(ctx);
                        v.setEnabled(false);
                    }
                }

            });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mSwitcher = null;
    }

    public interface OnSwitchBluetooth {
        public void onBluetoothSwitchOn();
        public void onBluetoothSwitchOff();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent == null)
                    return;

                String action = intent.getAction();
                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                    if (state != -1) {
                        switch (state) {
                        case BluetoothAdapter.STATE_OFF:
                            mDisabledLabel.setText(R.string.f_splash_bluetooth_disabled);
                            mSwitcher.setChecked(false);
                            mSwitcher.setEnabled(true);
                            mSwitcher.setVisibility(View.VISIBLE);
                            mSwitcherPb.setVisibility(View.INVISIBLE);

                            mEnabled.setVisibility(View.INVISIBLE);
                            mDisabled.setVisibility(View.VISIBLE);

                            {
                                Object a = getActivity();
                                if (a instanceof OnSwitchBluetooth) {
                                    ((OnSwitchBluetooth) a).onBluetoothSwitchOff();
                                }
                            }

                            break;
                        case BluetoothAdapter.STATE_ON:
                            mDisabledLabel.setText(R.string.f_splash_bluetooth_enabled);
                            mSwitcher.setChecked(true);
                            mSwitcher.setEnabled(true);
                            mSwitcher.setVisibility(View.VISIBLE);
                            mSwitcherPb.setVisibility(View.INVISIBLE);

                            mEnabled.setVisibility(View.VISIBLE);
                            mDisabled.setVisibility(View.INVISIBLE);
                            
                            {
                                Object a = getActivity();
                                if (a instanceof OnSwitchBluetooth) {
                                    ((OnSwitchBluetooth) a).onBluetoothSwitchOn();
                                }
                            }
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            mDisabledLabel.setText(R.string.f_splash_bluetooth_turning_off);
                            mSwitcher.setEnabled(false);
                            mSwitcher.setVisibility(View.INVISIBLE);
                            mSwitcherPb.setVisibility(View.VISIBLE);
                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:
                            mDisabledLabel.setText(R.string.f_splash_bluetooth_turning_on);
                            mSwitcher.setEnabled(false);
                            mSwitcher.setVisibility(View.INVISIBLE);
                            mSwitcherPb.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                }
            }
        };

    public void onStart() {
        super.onStart();

        Activity a = getActivity();

        if (a != null) {
            if (U.BLE.isEnabled(a)) {
                mSwitcher.setChecked(true);
                mEnabled.setVisibility(View.VISIBLE);
                mDisabled.setVisibility(View.INVISIBLE);
            } else {
                mSwitcher.setChecked(false);
                mEnabled.setVisibility(View.INVISIBLE);
                mDisabled.setVisibility(View.VISIBLE);
            }

            mSwitcher.setEnabled(true);
            mSwitcherPb.setVisibility(View.INVISIBLE);

            a.registerReceiver(mBroadcastReceiver,
                               new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        Activity a = getActivity();
        if (a != null) {
            a.unregisterReceiver(mBroadcastReceiver);
        }
    }


} // SplashFragment
