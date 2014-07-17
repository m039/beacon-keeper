/** BluetoothEnableButtonFragment.java ---
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 *
 */

package com.m039.ibeacon.keeper.fragment;

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

import com.m039.ibeacon.keeper.app.R;
import com.m039.ibeacon.keeper.U;


/**
 *
 *
 * Created:
 *
 * @author
 * @version
 * @since
 */
public class BluetoothEnableButtonFragment extends BaseFragment {

    public static BluetoothEnableButtonFragment newInstance() {
        return new BluetoothEnableButtonFragment();
    }

    private Switch mSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.f_bluetooth_enable, parent, false);

        mSwitch = (Switch) v.findViewById(R.id.bluetooth_switch);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mSwitch = null;
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
                            mSwitch.setChecked(false);
                            mSwitch.setEnabled(true);
                            break;
                        case BluetoothAdapter.STATE_ON:
                            mSwitch.setChecked(true);
                            mSwitch.setEnabled(true);
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                        case BluetoothAdapter.STATE_TURNING_ON:
                            mSwitch.setEnabled(false);
                            break;
                        }
                    }
                }
            }
        };

    @Override
    public void onStart() {
        super.onStart();

        Activity a = getActivity();

        if (a != null) {
            if (U.BLE.isEnabled(a)) {
                mSwitch.setChecked(true);
            } else {
                mSwitch.setChecked(false);
            }

            mSwitch.setEnabled(true);

            a.registerReceiver(mBroadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
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

} // BluetoothEnableButtonFragment
