package com.m039.ibeacon.keeper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.TextView;

import com.m039.ibeacon.keeper.adapter.IBeaconEntityAdapter;
import com.m039.ibeacon.keeper.content.IBeaconEntity;
import com.m039.ibeacon.keeper.receiver.IBeaconReceiver;

@SuppressLint("UseSparseArrays")
public class DemoActivity extends Activity {

    public static final String TAG = "m039";
    public static final int WHAT_UPDATE = 0;

    private TextView mText;
    private ListView mList;

    private IBeaconEntityAdapter mIBeaconEntityAdapter = 
        new IBeaconEntityAdapter();

    private boolean mBleEnabled = false;

    Handler mHandler = new Handler() {
            @Override
            public void handleMessage (Message msg) {
                if (msg.what == WHAT_UPDATE) {
                    onUpdate();
                    sendEmptyMessageDelayed(msg.what, 500);
                }
            }

            void onUpdate() {
                if (mText != null) {
                    if (mBleEnabled) {
                        mText.setText(R.string.a_demo__ble_enabled);
                    } else {
                        mText.setText(R.string.a_demo__ble_disabled);
                        mIBeaconEntityAdapter.clear();
                    }
                }

                mIBeaconEntityAdapter.notifyDataSetChanged();
            }
        };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_demo);

        mText = (TextView) findViewById(R.id.text);
        mList = (ListView) findViewById(R.id.list);

        if (mList != null) {
            mList.setAdapter(mIBeaconEntityAdapter);
        }
    }

    private BroadcastReceiver mIBeaconReceiver = new IBeaconReceiver() {

            @Override
            protected void onFoundIBeacon(IBeaconEntity iBeaconEntity) {
                mIBeaconEntityAdapter.replace(iBeaconEntity);
            }

            @Override
            protected void onBleEnabled() {
                mBleEnabled = true;
            }

            @Override
            protected void onBleDisabled() {
                mBleEnabled = false;
            }
        };


    @Override
    protected void onStart() {
        super.onStart();

        IBeaconReceiver.registerReceiver(this, mIBeaconReceiver);
        startHandleMessages();
    }

    void startHandleMessages() {
        mHandler.sendEmptyMessage(WHAT_UPDATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopHandleMessages();
        IBeaconReceiver.unregisterReceiver(this, mIBeaconReceiver);
    }

    void stopHandleMessages() {
        mHandler.removeMessages(WHAT_UPDATE);
    }
}
