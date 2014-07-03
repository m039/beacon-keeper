/** IBeaconFactory.java --- 
 *
 * Copyright (C) 2014 Dmitry Mozgin
 *
 * Author: Dmitry Mozgin <m0391n@gmail.com>
 *
 * 
 */

package com.m039.ibeacon.keeper.content;

import java.util.Arrays;

/**
 * 
 *
 * Created: 07/01/14
 *
 * @author Dmitry Mozgin
 * @version 
 * @since 
 */
public class IBeaconFactory {

    private static byte[] IBEACON_PREFIX = new byte[] {
        (byte) 0x02, (byte) 0x01, (byte) 0x06, (byte) 0x1a,
        (byte) 0xff, (byte) 0x4c, (byte) 0x00, (byte) 0x02, 
        (byte) 0x15
    };

    private static int IBEACON_PREFIX_START = 0;
    private static int IBEACON_PREFIX_LENGTH = 9;
    private static int IBEACON_PREFIX_END = IBEACON_PREFIX_START + IBEACON_PREFIX_LENGTH;

    private static int PROXIMITY_UUID_START = IBEACON_PREFIX_END;
    private static int PROXIMITY_UUID_LENGTH = 16;
    private static int PROXIMITY_UUID_END = PROXIMITY_UUID_START + PROXIMITY_UUID_LENGTH;

    private static int MAJOR_START = PROXIMITY_UUID_END;
    private static int MAJOR_LENGTH = 2;
    private static int MAJOR_END = MAJOR_START + MAJOR_LENGTH;

    private static int MINOR_START = MAJOR_END;
    private static int MINOR_LENGTH = 2;
    private static int MINOR_END = MINOR_START + MINOR_LENGTH;

    private static int TX_POWER_START = MINOR_END;
    private static int TX_POWER_LENGTH = 1;
    private static int TX_POWER_END = TX_POWER_START + TX_POWER_LENGTH;

    private static int SCANRECORD_SIZE = TX_POWER_END;

    /**
     * @param scanRecord the actual packet bytes
     * @return null or an instance of of an <code>IBeacon</code>
     */
    public static IBeacon decodeScanRecord(byte[] scanRecord) {
        if (scanRecord != null && scanRecord.length >= SCANRECORD_SIZE) {

            byte ibeaconPrefix[] = Arrays
                .copyOfRange(scanRecord, IBEACON_PREFIX_START, IBEACON_PREFIX_END);

            if (!Arrays.equals(ibeaconPrefix, IBEACON_PREFIX)) {
                return null;
            }

            byte proximityUuid[] = Arrays
                .copyOfRange(scanRecord, PROXIMITY_UUID_START, PROXIMITY_UUID_END);
            byte major[] = Arrays
                .copyOfRange(scanRecord, MAJOR_START, MAJOR_END);
            byte minor[] = Arrays
                .copyOfRange(scanRecord, MINOR_START, MINOR_END);
            byte txPower[] = Arrays
                .copyOfRange(scanRecord, TX_POWER_START, TX_POWER_END);

            IBeacon ibeacon = new IBeacon();

            ibeacon.mProximityUuid = toProximityUuidString(proximityUuid);
            ibeacon.mMajor = (major[0] & 0xff) * 0x100 + (major[1] & 0xff);
            ibeacon.mMinor = (minor[0] & 0xff) * 0x100 + (minor[1] & 0xff);
            ibeacon.mTxPower = txPower[0];            

            return ibeacon;
        }

        return null;
    }

    private static String toProximityUuidString(byte proximityUuid[]) {
        if (proximityUuid == null || proximityUuid.length != 16) {
            return "00000000-0000-0000-0000-000000000000";
        }

        StringBuilder sb = new StringBuilder();

        int k = 0;

        for (int i = 0; i < 4; i++) {
            sb.append(Integer.toHexString(proximityUuid[k++] & 0xff));
        }
        
        sb.append('-');

        for (int i = 0; i < 2; i++) {
            sb.append(Integer.toHexString(proximityUuid[k++] & 0xff));
        }

        sb.append('-');

        for (int i = 0; i < 2; i++) {
            sb.append(Integer.toHexString(proximityUuid[k++] & 0xff));
        }

        sb.append('-');

        for (int i = 0; i < 2; i++) {
            sb.append(Integer.toHexString(proximityUuid[k++] & 0xff));
        }

        sb.append('-');

        for (int i = 0; i < 6; i++) {
            sb.append(Integer.toHexString(proximityUuid[k++] & 0xff));
        }

        return sb.toString();
    }

} // IBeaconFactory
