/** BeaconInfoActivity.java ---
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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.m039.beacon.keeper.U;
import com.m039.beacon.keeper.app.R;
import com.m039.beacon.keeper.content.BeaconEntity;
import com.m039.beacon.keeper.receiver.BeaconReceiver;

/**
 *
 *
 *
 * @author Dmitry Mozgin
 * @version
 * @since
 */
public class BeaconInfoActivity extends BaseActivity {

    public static final String EXTRA_BEACON_ENTITY =
        "com.m039.beacon.keeper.activity.extra.beacon_entity";

    private TextView mProximityUuid;
    private TextView mMajor;
    private TextView mMinor;
    private TextView mTxPower;
    private TextView mAccuracy;
    private TextView mDistance;
    private TextView mLastUpdate;

    @SuppressWarnings("unused")
    private ImageView mProducer;

    private BeaconEntity mBeaconEntity;

    public static void startActivity(Context ctx, BeaconEntity beaconEntity) {
        Intent i = new Intent(ctx, BeaconInfoActivity.class);
        i.putExtra(EXTRA_BEACON_ENTITY, beaconEntity);
        ctx.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_beacon_info);

        mBeaconEntity = (BeaconEntity) getIntent()
            .getParcelableExtra(EXTRA_BEACON_ENTITY);
    }

    private BeaconReceiver mBeaconReceiver = new BeaconReceiver() {

            @Override
            protected void onFoundBeacon(Context ctx, BeaconEntity beaconEntity) {
                if (mBeaconEntity.equals(beaconEntity)) {
                    mBeaconEntity = beaconEntity;
                }
            }

        };

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // mProximityUuid = (TextView) findViewById(R.id.proximity_uuid);
        // mMajor = (TextView) findViewById(R.id.major);
        // mMinor = (TextView) findViewById(R.id.minor);
        // mTxPower = (TextView) findViewById(R.id.tx_power);
        // mAccuracy = (TextView) findViewById(R.id.accuracy);
        // mDistance = (TextView) findViewById(R.id.distance);
        // mLastUpdate = (TextView) findViewById(R.id.last_update);
        // mProducer = (ImageView) findViewById(R.id.producer);

        onPeriodicUpdate();
    }

    @Override
    protected void onPeriodicUpdate() {
        // mProximityUuid.setText(mBeaconEntity.getProximityUuid());
        // mMajor.setText(String.valueOf(mBeaconEntity.getMajor()));
        // mMinor.setText(String.valueOf(mBeaconEntity.getMinor()));
        // mTxPower.setText(String.valueOf(mBeaconEntity.getTxPower()));
        // mAccuracy.setText(String.format("%.2f", mBeaconEntity.getAccuracy()));
        // mLastUpdate.setText(U.IBeacon.getLastUpdate(mBeaconEntity));
        // mDistance.setText(mBeaconEntity.getDistanceStringId());
    }

    @Override
    protected void onStart() {
        super.onStart();

        mBeaconReceiver.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mBeaconReceiver.unregister(this);
    }

} // BeaconInfoActivity
