/** AboutActivity.java ---
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

import com.m039.ibeacon.keeper.app.R;

/**
 *
 *
 * Created:
 *
 * @author
 * @version
 * @since
 */
public class AboutActivity extends BaseActivity {
    
    public static void startActivity(Context ctx) {
        ctx.startActivity(new Intent(ctx, AboutActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_about);
    }
    
} // AboutActivity
