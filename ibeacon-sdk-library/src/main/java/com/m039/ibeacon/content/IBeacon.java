/** IBeacon.java --- 
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 * 
 */

package com.m039.ibeacon.content;

/**
 * 
 *
 * Created: 07/01/14
 *
 * @author Dmitry Mozgin
 * @version 
 * @since 
 */
public final class IBeacon {
    
    protected String    mProximityUuid;
    protected int       mMajor;
    protected int       mMinor;
    protected int       mTxPower;

    public String getProximityUuid() {
        return mProximityUuid;
    }

    public int getMajor() {
        return mMajor;
    }

    public int getMinor() {
        return mMinor;
    }

    public int getTxPower() {
        return mTxPower;
    }

    @Override 
    public int hashCode() {
        int result = 17;

        result = 31 * result + (mProximityUuid == null? 0 : mProximityUuid.hashCode());
        result = 31 * result + mMinor;
        result = 31 * result + mMajor;

        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof IBeacon)) {
            return false;
        }


        IBeacon lhs = (IBeacon) o;

        return (mProximityUuid == null? 
                lhs.mProximityUuid == null : mProximityUuid.equals(lhs.mProximityUuid)) &&
            mMajor == lhs.mMajor &&
            mMinor == lhs.mMinor;
    }

    @Override
    public String toString() {
        return String.format("IBeacon proximityUuid: '%s', major: %d, minor: %d, txPower: %d",
                             mProximityUuid, mMajor, mMinor, mTxPower);
    }

} // IBeacon
