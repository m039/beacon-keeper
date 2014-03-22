package com.m039.estimoto;

import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.m039.estimoto.service.EstimotoService;
import com.m039.estimoto.service.EstimotoService.EstimotoServiceBinder;
import com.m039.estimoto.util.EstimotoServiceUtil;

public class DemoActivity extends Activity {

    TextView mStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        mStatus = (TextView) findViewById(R.id.status);

        initButtons();
    }

    void initButtons() {
        for (int buttonId : new int[] {
                R.id.turn_on,
                R.id.turn_off,
                R.id.turn_bluetooth_on
            }) {
            View button = findViewById(buttonId);
            if (button != null) {
                button.setOnClickListener(mOnButtonsClickListener);
            }
        }
    }

    View.OnClickListener mOnButtonsClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();

                Context ctx = v.getContext();

                if (id == R.id.turn_on) {
                    setStatusText("Service is on");
                    EstimotoServiceUtil.turnOn(ctx);
                } else if (id == R.id.turn_off) {
                    setStatusText("Service is off");
                    EstimotoServiceUtil.turnOff(ctx);
                } else if (id == R.id.turn_bluetooth_on) {
                    startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
                }
            }
        };

    private EstimotoServiceBinder mEstimotoServiceBinder = null;
    private boolean mIsBound = false;

    private void setStatusText(String text) {
        if (mStatus != null) {
            mStatus.setText(text);
        }
    }

    private ServiceConnection mEstimotoServiceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                mEstimotoServiceBinder = (EstimotoService.EstimotoServiceBinder) service;
                mEstimotoServiceBinder
                    .setOnBeaconsDiscoveredListener(new BeaconManager.RangingListener() {
                            @Override
                            public void onBeaconsDiscovered(final Region region, final List<Beacon> beacons) {
                                StringBuilder sb = new StringBuilder();

                                for (Beacon b : beacons) {
                                    sb.append(b.toString() + "\n\n");
                                }

                                setStatusText(sb.toString());
                            }
                        });
            }

            public void onServiceDisconnected(ComponentName className) {
                if (mEstimotoServiceBinder != null) {
                    mEstimotoServiceBinder.unsetOnBeaconsDiscoveredListener();
                }

                setStatusText("disconnected");

                mEstimotoServiceBinder = null;
            }
        };

    void doBindService() {
        bindService(new Intent(this, EstimotoService.class), 
                    mEstimotoServiceConnection, 
                    Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(mEstimotoServiceConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        doBindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        doUnbindService();
    }
}
