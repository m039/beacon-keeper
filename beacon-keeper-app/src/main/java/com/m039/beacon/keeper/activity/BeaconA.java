/** BeaconA.java ---
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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import com.m039.beacon.keeper.app.R;
import com.m039.beacon.keeper.content.BeaconEntity;

/**
 *
 *
 * Created:
 *
 * @author Dmitry Mozgin
 * @version 1
 * @since Sat Dec 20 06:38:54 2014
 */
public class BeaconA extends BaseActivity {

    public static final String EXTRA_BEACON_ENTITY =
        "com.m039.beacon.keeper.activity.extra.beacon_entity";

    public static void startActivity(Activity a, BeaconEntity beaconEntity) {
        Intent i = new Intent(a, BeaconA.class);
        i.putExtra(EXTRA_BEACON_ENTITY, beaconEntity);
        a.startActivity(i);

        a.overridePendingTransition (R.anim.enter_in, R.anim.enter_out);
    }

    private BeaconEntity mBeaconEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_beacon);

        mBeaconEntity = (BeaconEntity) getIntent().getParcelableExtra(EXTRA_BEACON_ENTITY);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition (R.anim.finish_in, R.anim.finish_out);
    }

    @Override
    protected void onFoundBeacon(BeaconEntity beaconEntity) {
        if (mBeaconEntity.equals(beaconEntity)) {
            mBeaconEntity = beaconEntity;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        onUpdate();
    }

    @Override
    protected void onPeriodicUpdate() {
        onUpdate();
    }

    private void onUpdate() {
        List<Pair<Integer, String>> pairs = new ArrayList<Pair<Integer, String>>();

        pairs.add(Pair.create(R.id.producer_name, mBeaconEntity.getProducerName()));
        pairs.add(Pair.create(R.id.uuid, mBeaconEntity.getUuid()));
        pairs.add(Pair.create(R.id.major, String.valueOf(mBeaconEntity.getMajor())));
        pairs.add(Pair.create(R.id.minor, String.valueOf(mBeaconEntity.getMinor())));
        pairs.add(Pair.create(R.id.tx_power, String.valueOf(mBeaconEntity.getTxPower())));
        pairs.add(Pair.create(R.id.distance, getString(mBeaconEntity.getDistanceStringId())));
        pairs.add(Pair.create(R.id.accuracy, String.valueOf(mBeaconEntity.getAccuracy())));
        pairs.add(Pair.create(R.id.last_update, String.valueOf(mBeaconEntity.getLastSeenTimestamp())));

        for (Pair<Integer, String> p: pairs) {
            TextView tv = (TextView) findViewById(p.first);
            if (tv != null) {
                tv.setText(p.second);
            }
        }

        ((ImageView) findViewById(R.id.producer_icon)).setImageResource(findProducerIcon());
    }

    private int findProducerIcon() {
        switch (mBeaconEntity.getProducer()) {
        case BeaconEntity.PRODUCER_ESTIMOTE:
            return R.drawable.beacon_entity__producer__estimote;
        case BeaconEntity.PRODUCER_KONTAKT:
            return R.drawable.beacon_entity__producer__kontakt;
        case BeaconEntity.PRODUCER_QUALCOMM:
            return R.drawable.beacon_entity__producer__qualcomm;
        case BeaconEntity.PRODUCER_STICKNFIND:
            return R.drawable.beacon_entity__producer__sticknfind;
        case BeaconEntity.PRODUCER_UNKNOWN:
        default:
            return R.drawable.beacon_entity__producer__unknown;
        }
    }

} // BeaconA
