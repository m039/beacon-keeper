/** IBeacon.java ---
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

package com.m039.beacon.keeper.content;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 *
 * Created: 07/01/14
 *
 * @author Dmitry Mozgin
 * @version
 * @since
 */
public final class IBeacon
    implements Parcelable,
               Comparable<IBeacon>
{

    protected String    mUuid;
    protected int       mMajor;
    protected int       mMinor;
    protected int       mTxPower;

    protected IBeacon() {
    }

    public IBeacon(String uuid, int major, int minor, int txPower) {
        mUuid = uuid;
        mMajor = major;
        mMinor = minor;
        mTxPower = txPower;
    }

    public String getUuid() {
        return mUuid;
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

        result = 31 * result + (mUuid == null? 0 : mUuid.hashCode());
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

        return (mUuid == null?
                lhs.mUuid == null : mUuid.equalsIgnoreCase(lhs.mUuid)) &&
            mMajor == lhs.mMajor &&
            mMinor == lhs.mMinor;
    }

    @Override
    public String toString() {
        return String.format("IBeacon uuid: '%s', major: %d, minor: %d, txPower: %d",
                             mUuid, mMajor, mMinor, mTxPower);
    }

    @Override
    public int compareTo (IBeacon another) {
        if (!(another instanceof IBeacon)) {
            throw new ClassCastException("A IBeacon object expected.");
        }
        
        int compare;

        compare = mUuid.compareTo(another.mUuid);
        if (compare != 0) {
            return compare;
        }

        compare = Integer.compare(mMajor, another.mMajor);
        if (compare != 0) {
            return compare;
        }

        return Integer.compare(mMinor, another.mMinor);
    }

    //
    // Parcelable
    //

    private IBeacon(Parcel in) {
        mUuid = in.readString();
        mMajor = in.readInt();
        mMinor = in.readInt();
        mTxPower = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mUuid);
        out.writeInt(mMajor);
        out.writeInt(mMinor);
        out.writeInt(mTxPower);
    }

    public static final Parcelable.Creator<IBeacon> CREATOR = new Parcelable.Creator<IBeacon>() {
            public IBeacon createFromParcel(Parcel in) {
                return new IBeacon(in);
            }

            public IBeacon[] newArray(int size) {
                return new IBeacon[size];
            }
        };

} // IBeacon
