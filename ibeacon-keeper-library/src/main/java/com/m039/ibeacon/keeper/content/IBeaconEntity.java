/** IBeaconEntity.java ---
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 *
 */

package com.m039.ibeacon.keeper.content;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 *
 * Created: 07/03/14
 *
 * @author Dmitry Mozgin
 * @version
 * @since
 */
public class IBeaconEntity
    implements Parcelable
{

    public static final int PRODUCER_UNKNOWN = 0;
    public static final int PRODUCER_ESTIMOTE = 1;
    public static final int PRODUCER_KONTAKT = 2;

    public static final int DISTANCE_FAR = 0;
    public static final int DISTANCE_NEAR = 1;
    public static final int DISTANCE_IMMEDIATE = 2;

    final protected IBeacon mIBeacon;

    protected int mProducer = PRODUCER_UNKNOWN;
    protected long mTimestamp = -1;
    protected BluetoothDevice mBluetoothDevice;
    protected int mRssi = 0;
    protected int mDistance = DISTANCE_FAR;
    protected float mAccuracy = -1.0f;

    protected byte[] mScanDataDebug = null;

    public IBeaconEntity(IBeacon iBeacon) {
        if (iBeacon == null) {
            throw new IllegalArgumentException("ibeacon should be not null");
        }

        mIBeacon = iBeacon;
    }

    public IBeacon getIBeacon() {
        return mIBeacon;
    }

    public String getProximityUuid() {
        return mIBeacon.getProximityUuid();
    }

    public int getMajor() {
        return mIBeacon.getMajor();
    }

    public int getMinor() {
        return mIBeacon.getMinor();
    }

    public int getTxPower() {
        return mIBeacon.getTxPower();
    }

    public int getProducer() {
        return mProducer;
    }

    public int getDistance() {
        return mDistance;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public long getLastSeenTimestamp() {
        return getTimestamp();
    }

    public BluetoothDevice getBluetoothDevice() {
        return mBluetoothDevice;
    }

    public int getRssi() {
        return mRssi;
    }

    public float getAccuracy() {
        return mAccuracy;
    }

    /**
     * @return scanData if C.DEBUG is true
     */
    public byte[] getScanDataDebug() {
        return mScanDataDebug;
    }

    @Override
    public int hashCode() {
        return mIBeacon.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof IBeaconEntity)) {
            return false;
        }

        IBeaconEntity lhs = (IBeaconEntity) o;

        return (mIBeacon == null? 
                lhs.mIBeacon == null : mIBeacon.equals(lhs.mIBeacon));
    }

    //
    // Parcelable
    //

    private IBeaconEntity(Parcel in) {
        mIBeacon = (IBeacon) in.readParcelable(IBeacon.class.getClassLoader());
        mBluetoothDevice = (BluetoothDevice) in.readParcelable(BluetoothDevice.class.getClassLoader());
        mProducer = in.readInt();
        mRssi = in.readInt();
        mDistance = in.readInt();
        mTimestamp = in.readLong();
        mAccuracy = in.readFloat();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(mIBeacon, flags);
        out.writeParcelable(mBluetoothDevice, flags);
        out.writeInt(mProducer);
        out.writeInt(mRssi);
        out.writeInt(mDistance);
        out.writeLong(mTimestamp);
        out.writeFloat(mAccuracy);
    }

    public static final Parcelable.Creator<IBeaconEntity> CREATOR = new Parcelable.Creator<IBeaconEntity>() {
            public IBeaconEntity createFromParcel(Parcel in) {
                return new IBeaconEntity(in);
            }

            public IBeaconEntity[] newArray(int size) {
                return new IBeaconEntity[size];
            }
        };

} // IBeaconEntity
