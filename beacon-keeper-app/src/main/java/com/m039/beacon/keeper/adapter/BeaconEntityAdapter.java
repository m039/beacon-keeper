/** Beacon.java ---
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

package com.m039.beacon.keeper.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m039.beacon.keeper.app.R;
import com.m039.beacon.keeper.content.BeaconEntity;

/**
 *
 *
 * Created:
 *
 * @author
 * @version
 * @since
 */
public class BeaconEntityAdapter
    extends RecyclerView.Adapter<BeaconEntityAdapter.ViewHolder>
            implements View.OnClickListener
{

    private List<BeaconEntity> mBeaconEntities;

    public BeaconEntityAdapter() {
        this(new ArrayList<BeaconEntity>());
    }

    public BeaconEntityAdapter(List<BeaconEntity> beaconEntities) {
        if (beaconEntities == null) {
            throw new IllegalArgumentException("beaconEntities is null");
        }

        mBeaconEntities = beaconEntities;
    }

    public boolean replace(BeaconEntity beaconEntity) {
        int index = mBeaconEntities.indexOf(beaconEntity);
        boolean result = false;

        if (index == -1) {
            result = mBeaconEntities.add(beaconEntity);
            if (result) {
                notifyItemInserted(mBeaconEntities.size() - 1);
            }

        } else {
            mBeaconEntities.set(index, beaconEntity);
            result = true;
            notifyItemChanged(index);
        }

        return result;
    }

    public void clear () {
        int size = mBeaconEntities.size();
        mBeaconEntities.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void removeOld(int timeToLiveMs) {
        List<BeaconEntity> toRemove = null;
        long currentTimeMs = System.currentTimeMillis();

        for (BeaconEntity beaconEntity : mBeaconEntities) {
            if ((currentTimeMs - beaconEntity.getTimestamp()) > timeToLiveMs) {
                if (toRemove == null) {
                    toRemove = new ArrayList<BeaconEntity>();
                }

                toRemove.add(beaconEntity);
            }
        }

        if (toRemove != null) {
            mBeaconEntities.removeAll(toRemove);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView uuid;
        TextView major;
        TextView minor;
        TextView producerName;

        ViewHolder(View v) {
            super(v);

            uuid = (TextView) v.findViewById(R.id.uuid);
            major = (TextView) v.findViewById(R.id.major);
            minor = (TextView) v.findViewById(R.id.minor);
            producerName = (TextView) v.findViewById(R.id.producer_name);
        }

    }

    @Override
    public BeaconEntityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent
            .getContext()
            .getSystemService (Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.e_list, parent, false);

        ViewHolder vh = new ViewHolder(v);

        v.setOnClickListener(this);
        v.setTag(vh);

        return vh;
    }

    @Override
    public void onClick(View view) {
        ViewHolder vh = (ViewHolder) view.getTag();
        onClick(mBeaconEntities.get(vh.getPosition()));
    }

    protected void onClick(BeaconEntity beaconEntity) {
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        BeaconEntity beaconEntity = mBeaconEntities.get(position);

        h.uuid.setText(beaconEntity.getUuid());
        h.major.setText(String.valueOf(beaconEntity.getMajor()));
        h.minor.setText(String.valueOf(beaconEntity.getMinor()));
        h.producerName.setText(beaconEntity.getProducerName());
    }

    @Override
    public int getItemCount() {
        return mBeaconEntities.size();
    }

} // BeaconEntityAdapter
