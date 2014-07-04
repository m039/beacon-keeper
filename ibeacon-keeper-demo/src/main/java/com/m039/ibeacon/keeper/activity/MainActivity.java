package com.m039.ibeacon.keeper.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.m039.ibeacon.keeper.R;
import com.m039.ibeacon.keeper.adapter.IBeaconEntityAdapter;
import com.m039.ibeacon.keeper.content.IBeaconEntity;
import com.m039.ibeacon.keeper.receiver.IBeaconReceiver;

@SuppressLint("UseSparseArrays")
public class MainActivity extends BaseActivity {

    public static final String TAG = "m039";

    private TextView mText;
    private RecyclerView mRecycler;

    private IBeaconEntityAdapter mIBeaconEntityAdapter = 
        new IBeaconEntityAdapter() {
            @Override
            protected void onClick(IBeaconEntity iBeaconEntity) {
                IBeaconInfoActivity.startActivity(MainActivity.this, iBeaconEntity);
            }
        };

    private boolean mBleEnabled = false;    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        mText = (TextView) findViewById(R.id.text);
        mRecycler = (RecyclerView) findViewById(R.id.recycler);

        if (mRecycler != null) {
            mRecycler.setHasFixedSize(true);
            mRecycler.setLayoutManager(new LinearLayoutManager(this));
            mRecycler.setItemAnimator(new DefaultItemAnimator());
            mRecycler.setAdapter(mIBeaconEntityAdapter);
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
    protected void onPeriodicUpdate() {
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
