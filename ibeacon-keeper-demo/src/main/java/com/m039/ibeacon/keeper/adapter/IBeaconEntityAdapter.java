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
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.text.format.DateUtils;
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
        boolean result = false;

        if (index == -1) {
            result = mIBeaconEntities.add(iBeaconEntity);
        } else {
            mIBeaconEntities.set(index, iBeaconEntity);
            result = true;
        }

        if (result) {
            Collections.sort(mIBeaconEntities);
        }

        return result;
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
        TextView lastUpdate;
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
            h.lastUpdate = (TextView) v.findViewById(R.id.last_update);
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
        h.lastUpdate.setText(getLastUpdate(iBeaconEntity));
        h.distance.setText(iBeaconEntity.getDistanceStringId());
        h.producer.setImageResource(getProducerDrawableId(iBeaconEntity));

        return v;
    }

    private static CharSequence getLastUpdate(IBeaconEntity iBeaconEntity) {
        return DateUtils
            .getRelativeTimeSpanString(iBeaconEntity.getLastSeenTimestamp(),
                                       System.currentTimeMillis(),
                                       DateUtils.SECOND_IN_MILLIS);
    }

    private static int getProducerDrawableId(IBeaconEntity iBeaconEntity) {
        if (iBeaconEntity.getProducer() == IBeaconEntity.PRODUCER_ESTIMOTE) {
            return R.drawable.ibeacon_entity__producer__estimote;
        } else if (iBeaconEntity.getProducer() == IBeaconEntity.PRODUCER_KONTAKT) {
            return R.drawable.ibeacon_entity__producer__kontakt;
        } else {
            return R.drawable.ibeacon_entity__producer__unknown;
        }
    }

} // IBeaconEntityAdapter
