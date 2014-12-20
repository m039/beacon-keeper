/** BeaconsActivity.java ---
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
import android.os.Bundle;
import android.os.Handler;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;

import com.m039.beacon.keeper.U;
import com.m039.beacon.keeper.app.R;
import com.m039.beacon.keeper.fragment.SplashFragment;

// import com.m039.beacon.keeper.L;

/**
 *
 *
 * Created:
 *
 * @author Dmitry Mozgin
 * @version 1
 * @since Sat Dec 20 00:52:19 2014
 */
public class BeaconsActivity extends BaseActivity 
    implements SplashFragment.OnSwitchBluetooth
{

    private ViewGroup mTop;
    private ViewGroup mBottom;
    private ViewGroup mOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_beacons);

        mTop = (ViewGroup) findViewById(R.id.top);
        mBottom = (ViewGroup) findViewById(R.id.bottom);
        mOverlay = (ViewGroup) findViewById(R.id.overlay);

        if (savedInstanceState == null) {
            getFragmentManager()
                .beginTransaction()
                .add(R.id.overlay, SplashFragment.newInstance())
                .commit();
        }

        // Default State

        mOverlay.setVisibility(View.VISIBLE);
        mTop.setVisibility(View.INVISIBLE);
        mBottom.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (U.BLE.isEnabled(this)) {
            new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAnimatorHelper.open();
                    }
                }, 700);            
        } else {
            mAnimatorHelper.close();            
        }
    }

    public static class AnimatorHelper {

        private static final long DURATION = 500L;
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
            if (mAnimator.isRunning()) {
                mAnimator.cancel();
            }

            mAnimator.setFloatValues(mValue, 1);
            mAnimator.start();
        }

        public void close() {
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
    public void onBluetoothSwitchOn() {
        mAnimatorHelper.open();
    }

    @Override
    public void onBluetoothSwitchOff() {
        mAnimatorHelper.close();
    }

} // BeaconsActivity
