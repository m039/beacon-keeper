/** BeaconsA.java ---
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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m039.beacon.keeper.U;
import com.m039.beacon.keeper.adapter.BeaconEntityAdapter;
import com.m039.beacon.keeper.app.R;
import com.m039.beacon.keeper.content.BeaconEntity;
import com.m039.beacon.keeper.fragment.SplashF;
import com.m039.beacon.keeper.widget.DividerItemDecoration;

/**
 *
 *
 * Created:
 *
 * @author Dmitry Mozgin
 * @version 1
 * @since Sat Dec 20 00:52:19 2014
 */
public class BeaconsA extends BaseActivity
    implements SplashF.OnSwitchBluetooth
{

    private static final int WHAT_RADAR_UPDATE = 0;

    private ViewGroup mTop;
    private ViewGroup mBottom;
    private ViewGroup mOverlay;

    private TextView mNumberOfBeacons;
    private RecyclerView mRecycler;
    private View mRadar;

    private Handler mRadarHandler = new Handler() {

            @Override
            public void handleMessage (Message msg) {
                if (msg.what == WHAT_RADAR_UPDATE) {
                    onRadarUpdate();
                    sendEmptyMessageDelayed(msg.what, 100);
                }
            }

        };

    private BeaconEntityAdapter mBeaconEntityAdapter =
        new BeaconEntityAdapter() {

            @Override
            protected void onClick(BeaconEntity beaconEntity) {
                BeaconA.startActivity(BeaconsA.this, beaconEntity);
            }

        };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_beacons);

        mTop = (ViewGroup) findViewById(R.id.top);
        mBottom = (ViewGroup) findViewById(R.id.bottom);
        mOverlay = (ViewGroup) findViewById(R.id.overlay);

        mNumberOfBeacons = (TextView) findViewById(R.id.number_of_beacons);
        mRecycler = (RecyclerView) findViewById(R.id.recycler);
        mRadar = findViewById(R.id.radar);

        if (mRecycler != null) {
            mRecycler.setHasFixedSize(true);
            mRecycler.setLayoutManager(new LinearLayoutManager(this));
            mRecycler.setItemAnimator(new DefaultItemAnimator());
            mRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            mRecycler.setAdapter(mBeaconEntityAdapter);
        }

        findViewById(R.id.settings).setOnClickListener(mOnButtonClickListener);

        // Default State

        mOverlay.setVisibility(View.VISIBLE);
        mTop.setVisibility(View.INVISIBLE);
        mBottom.setVisibility(View.INVISIBLE);

        // fragments

        if (savedInstanceState == null) {
            getFragmentManager()
                .beginTransaction()
                .add(R.id.overlay, SplashF.newInstance())
                .commit();
        }
    }

    View.OnClickListener mOnButtonClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.settings) {
                    SettingsA.startActivity(BeaconsA.this);
                }
            }

        };

    @Override
    protected void onFoundBeacon(BeaconEntity beaconEntity) {
        mBeaconEntityAdapter.replace(beaconEntity);
    }

    @Override
    protected void onPeriodicUpdate() {
        Resources res = getResources();

        mBeaconEntityAdapter
            .removeOld(U.SharedPreferences.getInteger(this, res.getString(R.string.beacon_keeper__pref_key__beacon_ttl_ms),res.getInteger(R.integer.beacon_keeper__beacon_ttl_ms_default)));
        mBeaconEntityAdapter
            .notifyDataSetChanged();

        if (mNumberOfBeacons != null) {
            mNumberOfBeacons.setText(String.valueOf(mBeaconEntityAdapter.getItemCount()));
        }
    }

    public static class AnimatorHelper {

        private static final long DURATION = 500L;
        private static final long START_DELAY = 700L;
        private static final String PROPERTY_NAME = "value";

        private float mValue = 0; // initial state

        private ObjectAnimator mAnimator;

        Property<AnimatorHelper, Float> mProperty =
            new Property<AnimatorHelper, Float>(Float.class, PROPERTY_NAME) {

                @Override
                public Float get (AnimatorHelper object) {
                    return onGet();
                }

                @Override
                public void set(AnimatorHelper obj, Float v) {
                    onSet(v);
                }

            };

        AnimatorListenerAdapter mAnimatorListenerAdapter =
            new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart (Animator animation) {
                    AnimatorHelper.this.onAnimationStart(mValue);
                }

                @Override
                public void onAnimationEnd (Animator animation) {
                    AnimatorHelper.this.onAnimationEnd(mValue);
                }
            };

        {
            mAnimator = new ObjectAnimator();
            mAnimator.addListener(mAnimatorListenerAdapter);
            mAnimator.setDuration(DURATION);
            mAnimator.setStartDelay(START_DELAY);
            mAnimator.setProperty(mProperty);
            mAnimator.setPropertyName(PROPERTY_NAME);
            mAnimator.setTarget(this);
        }

        protected float onGet() {
            return mValue;
        }

        protected void onSet(float v) {
            mValue = v;
        }

        protected void onAnimationStart(float v) {
        }

        protected void onAnimationEnd(float v) {
        }

        public void open() {
            if (mValue == 1)
                return;         // don't do anything

            if (mAnimator.isRunning()) {
                mAnimator.cancel();
            }            

            mAnimator.setFloatValues(mValue, 1);
            mAnimator.start();
        }

        public void close() {
            if (mValue == 0)
                return;         // don't do anything

            if (mAnimator.isRunning()) {
                mAnimator.cancel();
            }

            mAnimator.setFloatValues(mValue, 0);
            mAnimator.start();
        }

    }

    private AnimatorHelper mAnimatorHelper =
        new AnimatorHelper() {

            float barHeight = -1;
            float moveAwayDistance = -1;

            @Override
            protected float onGet() {
                return super.onGet();
            }

            @Override
            protected void onSet(float v) {
                super.onSet(v);

                float iv = 1 - v;

                if (barHeight == -1 || moveAwayDistance == -1) {
                    barHeight = getResources().getDimensionPixelOffset(R.dimen.bar_height);
                    moveAwayDistance = getResources().getDimensionPixelOffset(R.dimen.move_away_distance);
                }

                mTop.getLayoutParams().height = (int) (barHeight * v);
                mTop.requestLayout();

                mOverlay.setAlpha(iv);
                mOverlay.setTranslationY(moveAwayDistance * v);

                mBottom.setAlpha(v);
            }

            @Override
            protected void onAnimationStart(float v) {
                mOverlay.setVisibility(View.VISIBLE);
                mTop.setVisibility(View.VISIBLE);
                mBottom.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onAnimationEnd(float v) {
                if (v == 0) {

                    mOverlay.setVisibility(View.VISIBLE);
                    mTop.setVisibility(View.INVISIBLE);
                    mBottom.setVisibility(View.INVISIBLE);

                } else {
                    // v == 1;

                    mOverlay.setVisibility(View.INVISIBLE);
                    mTop.setVisibility(View.VISIBLE);
                    mBottom.setVisibility(View.VISIBLE);
                }
            }

        };

    @Override
    protected void onResume() {
        super.onResume();

        if (U.BLE.isEnabled(this)) {
            mAnimatorHelper.open();
        } else {
            mAnimatorHelper.close();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mRadarHandler.sendEmptyMessage(WHAT_RADAR_UPDATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mRadarHandler.removeMessages(WHAT_RADAR_UPDATE);
    }

    protected void onRadarUpdate() {
        if (mTop.getVisibility() == View.VISIBLE) {
            mRadar.setRotation(mRadar.getRotation() + 4);
        }
    }

    @Override
    public void onBluetoothSwitchOn() {
        mAnimatorHelper.open();
    }

    @Override
    public void onBluetoothSwitchOff() {
        mAnimatorHelper.close();
    }

} // BeaconsA
