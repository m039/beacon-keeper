/** SplashFragment.java ---
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

package com.m039.beacon.keeper.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.m039.beacon.keeper.app.R;

/**
 *
 *
 * Created:
 *
 * @author Dmitry Mozgin
 * @version 1
 * @since Fri Aug  8 23:05:19 2014
 */
public class SplashFragment extends BaseFragment {
    
    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    private View mLogo;
    private View mText;

    private static float sTextHeight = -1;
    private static long DURATION = 1000L;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (sTextHeight == -1) {
            sTextHeight = getResources().getDimensionPixelSize(R.dimen.f_splash__text_height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_splash, parent, false);

        mLogo = view.findViewById(R.id.logo);
        mText = view.findViewById(R.id.text);
        mText.animate().setListener(mTextAnimatorListener);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hideText(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mLogo = null;
        mText.animate().setListener(null);
        mText = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        showText(true);
    }

    private Animator.AnimatorListener mTextAnimatorListener = 
        new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd (Animator animation) {
                SplashFragment.this.onTextAnimationEnd();
            }
        };

    protected void onTextAnimationEnd() {
        Toast.makeText(getActivity(), "END", Toast.LENGTH_SHORT).show();
    }

    private void hideText(boolean withAnimation) {
        if (!withAnimation) {
            mText.setTranslationY(-sTextHeight);
        }
    }

    private void showText(boolean withAnimation) {
        if (withAnimation) {
            if (mText.getTranslationY() != 0) {
                mText.animate().setDuration(DURATION).translationY(0);
            }
        }
    }
    
    
} // SplashFragment
