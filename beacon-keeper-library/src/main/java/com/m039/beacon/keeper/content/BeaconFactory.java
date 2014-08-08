/** BeaconFactory.java ---
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

import java.util.Arrays;

import android.bluetooth.BluetoothDevice;

import com.m039.beacon.keeper.C;
import com.m039.beacon.keeper.U;

/**
 *
 *
 * Created: 07/01/14
 *
 * @author Dmitry Mozgin
 * @version
 * @since
 */
public class BeaconFactory {

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
     * @return null or an instance of an <code>IBeacon</code>
     */
    public static IBeacon decodeScanRecord(byte[] scanRecord) {
        if (scanRecord != null && scanRecord.length >= SCANRECORD_SIZE) {

            byte iBeaconPrefix[] = Arrays
                .copyOfRange(scanRecord, IBEACON_PREFIX_START, IBEACON_PREFIX_END);

            if (!Arrays.equals(iBeaconPrefix, IBEACON_PREFIX)) {
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

            IBeacon iBeacon = new IBeacon();

            iBeacon.mProximityUuid = toProximityUuidString(proximityUuid);
            iBeacon.mMajor = (major[0] & 0xff) * 0x100 + (major[1] & 0xff);
            iBeacon.mMinor = (minor[0] & 0xff) * 0x100 + (minor[1] & 0xff);
            iBeacon.mTxPower = txPower[0];

            return iBeacon;
        }

        return null;
    }

    /**
     * @param device
     * @param rssi
     * @param scanRecord the actiual packet bytes
     * @return null or an instance of an <code>BeaconEntity</code>
     * @see android.bluetooth.BluetoothAdapter.LeScanCallback
     */
    public static BeaconEntity decode(BluetoothDevice device, int rssi, byte[] scanRecord) {
        IBeacon iBeacon = decodeScanRecord(scanRecord);
        if (iBeacon == null) {
            return null;
        }

        BeaconEntity beaconEntity = new BeaconEntity(iBeacon);

        beaconEntity.mBluetoothDevice = device;

        fillProducer(beaconEntity);
        fillTimestamp(beaconEntity);
        fillRssi(beaconEntity, rssi);

        if (C.DEBUG) {
            beaconEntity.mScanDataDebug = Arrays.copyOf(scanRecord, scanRecord.length);
        }

        return beaconEntity;
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

    private static void fillProducer(BeaconEntity beaconEntity) {
        int producer = BeaconEntity.PRODUCER_UNKNOWN;

        String proximityUuid = beaconEntity.getProximityUuid();

        if (proximityUuid.equalsIgnoreCase("b9407f30-f5f8-466e-aff9-25556b57fe6d")) {
            producer = BeaconEntity.PRODUCER_ESTIMOTE;
        } else if (proximityUuid.equalsIgnoreCase("f7826da6-4fa2-4e98-8024-bc5b71e0893e")) {
            producer = BeaconEntity.PRODUCER_KONTAKT;
        }

        beaconEntity.mProducer = producer;
    }

    private static void fillTimestamp(BeaconEntity beaconEntity) {
        beaconEntity.mTimestamp = System.currentTimeMillis();
    }

    private static void fillRssi(BeaconEntity beaconEntity, int rssi) {
        beaconEntity.mRssi = rssi;
        beaconEntity.mAccuracy = U.IBeacon.calculateAccuracy(beaconEntity.getTxPower(), rssi);

        if (beaconEntity.mAccuracy < 1) {
            beaconEntity.mDistance = BeaconEntity.DISTANCE_IMMEDIATE;
        } else if (beaconEntity.mAccuracy > 10) {
            beaconEntity.mDistance = BeaconEntity.DISTANCE_FAR;
        } else {
            beaconEntity.mDistance = BeaconEntity.DISTANCE_NEAR;
        }
    }

} // BeaconFactory
