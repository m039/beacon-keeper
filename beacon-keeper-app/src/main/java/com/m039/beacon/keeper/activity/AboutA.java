/** AboutA.java ---
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 *
 */

package com.m039.beacon.keeper.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.m039.beacon.keeper.app.R;

/**
 *
 *
 * Created:
 *
 * @author Dmitry Mozgin
 * @version 1
 * @since Sat Dec 20 08:45:26 2014
 */
public class AboutA extends BaseActivity {
    
    public static void startActivity(Activity a) {
        a.startActivity(new Intent(a, AboutA.class));
        a.overridePendingTransition (R.anim.enter_in, R.anim.enter_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_about);

        // set versionName

        try {
            ((TextView) findViewById(R.id.version_name))
                .setText("v" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
        }

        // set link button listener

        findViewById(R.id.link).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://github.com/m039/beacon-keeper"));
                    startActivity(i);
                }
            });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition (R.anim.finish_in, R.anim.finish_out);
    }
    
} // AboutA
