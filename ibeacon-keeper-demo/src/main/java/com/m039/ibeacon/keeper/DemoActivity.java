package com.m039.ibeacon.keeper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.m039.ibeacon.keeper.adapter.IBeaconEntityAdapter;
import com.m039.ibeacon.keeper.content.IBeaconEntity;
import com.m039.ibeacon.keeper.receiver.IBeaconReceiver;

@SuppressLint("UseSparseArrays")
public class DemoActivity extends Activity {

    public static final String TAG = "m039";
    public static final int REQUEST_CODE = 0;

    private TextView mText;
    private ListView mList;

    IBeaconEntityAdapter mIBeaconEntityAdapter = new IBeaconEntityAdapter();

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
                mIBeaconEntityAdapter.notifyDataSetChanged();
            }

            @Override
            protected void onBleEnabled() {
                if (mText != null) {
                    mText.setText(R.string.a_demo__ble_enabled);
                }
            }

            @Override
            protected void onBleDisabled() {
                if (mText != null) {
                    mText.setText(R.string.a_demo__ble_disabled);
                }

                mIBeaconEntityAdapter.clear();
                mIBeaconEntityAdapter.notifyDataSetChanged();
            }
        };


    @Override
    protected void onStart() {
        super.onStart();

        IBeaconReceiver.registerReceiver(this, mIBeaconReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();

        IBeaconReceiver.unregisterReceiver(this, mIBeaconReceiver);
    }
}
