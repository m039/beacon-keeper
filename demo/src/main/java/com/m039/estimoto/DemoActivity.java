package com.m039.estimoto;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

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
                R.id.turn_off
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
                    EstimotoServiceUtil.turnOn(ctx);
                } else if (id == R.id.turn_off) {
                    EstimotoServiceUtil.turnOff(ctx);
                } 
            }
        };

    private EstimotoServiceBinder mEstimotoServiceBinder = null;
    private boolean mIsBound = false;

    private ServiceConnection mEstimotoServiceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                mEstimotoServiceBinder = (EstimotoService.EstimotoServiceBinder) service;
                mEstimotoServiceBinder.setCountListener(new EstimotoService.OnCountListener() {
                        @Override
                        public void onCount(int count) {
                            if (mStatus != null) {
                                mStatus.setText("count = " + count);
                            }
                        }
                    });
            }

            public void onServiceDisconnected(ComponentName className) {
                if (mEstimotoServiceBinder != null) {
                    mEstimotoServiceBinder.unsetCountListener();
                }

                if (mStatus != null) {
                    mStatus.setText("disconnected");
                }

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
