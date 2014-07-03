/** IBeacon.java --- 
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 * 
 */

package com.m039.ibeacon.keeper.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.m039.ibeacon.keeper.R;
import com.m039.ibeacon.keeper.content.IBeaconEntity;
/**
 * 
 *
 * Created: 
 *
 * @author 
 * @version 
 * @since 
 */
public class IBeaconEntityAdapter extends BaseAdapter {

    private List<IBeaconEntity> mIBeaconEntities;

    public IBeaconEntityAdapter() {
        mIBeaconEntities = new ArrayList<IBeaconEntity>();
    }

    public IBeaconEntityAdapter(List<IBeaconEntity> iBeaconEntities) {
        if (iBeaconEntities == null) {
            throw new IllegalArgumentException("iBeaconEntities is null");
        }

        mIBeaconEntities = iBeaconEntities;
    }

    public boolean replace(IBeaconEntity iBeaconEntity) {
        int index = mIBeaconEntities.indexOf(iBeaconEntity);

        if (index == -1) {
            return mIBeaconEntities.add(iBeaconEntity);
        } else {
            mIBeaconEntities.set(index, iBeaconEntity);
            return true;
        }
    }

    public void clear () {
        mIBeaconEntities.clear();
    }

    @Override
    public int getCount() {
        return mIBeaconEntities.size();
    }

    @Override
    public Object getItem(int index) {
        return mIBeaconEntities.get(index);
    }

    @Override
    public long getItemId (int position) {
        return -1;
    }

    private static class Holder {
        TextView proximityUuid;
        TextView major;
        TextView minor;
        TextView txPower;
        TextView accuracy;
        TextView distance;
        ImageView producer;
    }

    @Override
    public View getView (int position, View v, ViewGroup parent) {
        IBeaconEntity iBeaconEntity = (IBeaconEntity) getItem(position);

        Holder h;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) parent
                .getContext().getSystemService (Context.LAYOUT_INFLATER_SERVICE);

            v = inflater.inflate(R.layout.e_list, parent, false);

            h = new Holder();

            h.proximityUuid = (TextView) v.findViewById(R.id.proximity_uuid);
            h.major = (TextView) v.findViewById(R.id.major);
            h.minor = (TextView) v.findViewById(R.id.minor);
            h.txPower = (TextView) v.findViewById(R.id.tx_power);
            h.accuracy = (TextView) v.findViewById(R.id.accuracy);
            h.distance = (TextView) v.findViewById(R.id.distance);
            h.producer = (ImageView) v.findViewById(R.id.producer);

            v.setTag(h);
        } else {
            h = (Holder) v.getTag();
        }

        h.proximityUuid.setText(iBeaconEntity.getProximityUuid());
        h.major.setText(String.valueOf(iBeaconEntity.getMajor()));
        h.minor.setText(String.valueOf(iBeaconEntity.getMinor()));
        h.txPower.setText(String.valueOf(iBeaconEntity.getTxPower()));
        h.accuracy.setText(String.format("%.2f", iBeaconEntity.getAccuracy()));
        h.distance.setText(String.valueOf(iBeaconEntity.getDistance()));

        return v;
    }

} // IBeaconEntityAdapter
