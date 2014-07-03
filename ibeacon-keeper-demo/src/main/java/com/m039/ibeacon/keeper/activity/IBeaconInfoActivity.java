/** IBeaconInfoActivity.java ---
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 *
 */

package com.m039.ibeacon.keeper.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.m039.ibeacon.keeper.R;
import com.m039.ibeacon.keeper.U;
import com.m039.ibeacon.keeper.content.IBeaconEntity;
import com.m039.ibeacon.keeper.receiver.IBeaconReceiver;

/**
 *
 *
 *
 * @author Dmitry Mozgin
 * @version
 * @since
 */
public class IBeaconInfoActivity extends BaseActivity {

    public static final String EXTRA_IBEACON_ENTITY = 
        "com.m039.ibeacon.keeper.activity.extra.ibeacon_entity";

    private TextView mProximityUuid;
    private TextView mMajor;
    private TextView mMinor;
    private TextView mTxPower;
    private TextView mAccuracy;
    private TextView mDistance;
    private TextView mLastUpdate;
    private ImageView mProducer;

    private IBeaconEntity mIBeaconEntity;

    public static void startActivity(Context ctx, IBeaconEntity iBeaconEntity) {
        Intent i = new Intent(ctx, IBeaconInfoActivity.class);
        i.putExtra(EXTRA_IBEACON_ENTITY, iBeaconEntity);
        ctx.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_ibeacon_info);
        
        mIBeaconEntity = (IBeaconEntity) getIntent()
            .getParcelableExtra(EXTRA_IBEACON_ENTITY);
    }

    private IBeaconReceiver mIBeaconReceiver = new IBeaconReceiver() {

            @Override
            protected void onFoundIBeacon(IBeaconEntity iBeaconEntity) {
                if (mIBeaconEntity.equals(iBeaconEntity)) {
                    mIBeaconEntity = iBeaconEntity;
                }
            }

        };

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mProximityUuid = (TextView) findViewById(R.id.proximity_uuid);
        mMajor = (TextView) findViewById(R.id.major);
        mMinor = (TextView) findViewById(R.id.minor);
        mTxPower = (TextView) findViewById(R.id.tx_power);
        mAccuracy = (TextView) findViewById(R.id.accuracy);
        mDistance = (TextView) findViewById(R.id.distance);
        mLastUpdate = (TextView) findViewById(R.id.last_update);
        mProducer = (ImageView) findViewById(R.id.producer);        

        onPeriodicUpdate();
    }

    @Override
    protected void onPeriodicUpdate() {
        mProximityUuid.setText(mIBeaconEntity.getProximityUuid());
        mMajor.setText(String.valueOf(mIBeaconEntity.getMajor()));
        mMinor.setText(String.valueOf(mIBeaconEntity.getMinor()));
        mTxPower.setText(String.valueOf(mIBeaconEntity.getTxPower()));
        mAccuracy.setText(String.format("%.2f", mIBeaconEntity.getAccuracy()));
        mLastUpdate.setText(U.IBeacon.getLastUpdate(mIBeaconEntity));
        mDistance.setText(mIBeaconEntity.getDistanceStringId());
    }

    @Override
    protected void onStart() {
        super.onStart();

        mIBeaconReceiver.registerReceiver(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mIBeaconReceiver.unregisterReceiver(this);
    }

} // IBeaconInfoActivity
