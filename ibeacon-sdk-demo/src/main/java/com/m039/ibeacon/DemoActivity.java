package com.m039.ibeacon;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.m039.ibeacon.content.IBeacon;
import com.m039.ibeacon.service.IBeaconService;
import com.m039.ibeacon.util.LEScanner;
import com.m039.ibeacon.util.OnEnableBluetoothCallback;
import com.m039.ibeacon.R;

@SuppressLint("UseSparseArrays")
public class DemoActivity extends Activity {

    public static final String TAG = "m039";
    public static final int REQUEST_CODE = 0;

    private LEScanner mLEScanner = new LEScanner(1000) {

            boolean mStarted = false;
            long mContinue = 0;

            @Override
            protected void onStartScan() {
                mStarted = true;
                updateText();
            }

            @Override
            protected void onStopScan() {
                mStarted = false;
                updateText();
            }

            @Override
            protected boolean onContinueScan() {
                mContinue = System.currentTimeMillis();
                updateText();
                return true;
            }

            void updateText() {
                if (mText != null) {
                    if (mStarted) {
                        mText.setText("LEScanner working (c: " + mContinue + ")");
                    } else {
                        mText.setText("LEScanner is not working");
                    }
                }
            }
        };

    private HashMap<Integer, String> mReceivedData =
        new HashMap<Integer, String>();

    private ArrayList<String> mItems = new ArrayList<String>();
    private Handler mHandler = new Handler();

    private LEScanner.LeScanCallback mLeScanCallback =
        new LEScanner.LeScanCallback() {

            volatile boolean posted = false;

            private Runnable mNotifyDataSetChangedRunnable = new Runnable() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void run() {
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) mList.getAdapter();
                        if (adapter != null) {
                            mItems.clear();
                            mItems.addAll(mReceivedData.values());
                            adapter.notifyDataSetChanged();
                        }

                        posted = false;
                    }
                };

            @SuppressWarnings("unchecked")
            @Override
            public void onLeScan (BluetoothDevice device, int rssi, IBeacon ibeacon) {
                if (mList != null) {
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) mList.getAdapter();
                    if (adapter == null) {
                        adapter = new ArrayAdapter<String>(DemoActivity.this, R.layout.e_list, mItems);
                        mList.setAdapter(adapter);
                    }

                    mReceivedData.put(ibeacon.hashCode(),
                                      String.format("ibeacon:\n" +
                                                    "proximityUuid: %s\n" +
                                                    "major: %s\n" +
                                                    "minor: %s\n" +
                                                    "txPower: %s",
                                                    ibeacon.getProximityUuid(),
                                                    ibeacon.getMajor(),
                                                    ibeacon.getMinor(),
                                                    ibeacon.getTxPower()));

                    if (!posted) {
                        mHandler.postDelayed(mNotifyDataSetChangedRunnable, 100);
                        posted = true;
                    }
                }
            }
        };

    private OnEnableBluetoothCallback mOnEnableBluetoothCallback =
        new OnEnableBluetoothCallback() {

            @Override
            public void onBluetoothEnabled() {
                android.util.Log.d(TAG, "onBluetoothEnabled");
                mLEScanner.startScan(DemoActivity.this, mLeScanCallback);
            }

            @Override
            public void onBluetoothDisabled() {
                android.util.Log.d(TAG, "onBluetoothDisabled");
                mLEScanner.stopScan(DemoActivity.this, mLeScanCallback);
            }
        };

    private TextView mText;
    private ListView mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        mText = (TextView) findViewById(R.id.text);
        mList = (ListView) findViewById(R.id.list);

        mOnEnableBluetoothCallback.onCreate(this, savedInstanceState, REQUEST_CODE);

        IBeaconService.startServiceByAlarmManager(this);
        IBeaconService.startServiceByAlarmManager(this);
        IBeaconService.startServiceByAlarmManager(this);
        IBeaconService.startServiceByAlarmManager(this);
        IBeaconService.startServiceByAlarmManager(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mOnEnableBluetoothCallback.onActivityResult (this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mLEScanner.stopScan(this, mLeScanCallback);
    }

}
