package com.rfduino.examples.callbacks;

import java.util.UUID;

import android.bluetooth.*;
import android.util.Log;

/** Example of how to use a callback to talk to the RFDuino. This callback goes with the LedButton.pde example sketch. **/
public class LedButtonCallback extends BluetoothGattCallback{
	private final String UUID_READ_LED_STATUS = "ledbtn"; 
	
	private final String logTag = "LedButtonBT";
	boolean connected = false;
	
	public boolean lastReadLEDStatus;
	
	@Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status,
            int newState) {
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            Log.i(logTag, "Connected to GATT server.");
            Log.i(logTag, "Attempting to start service discovery:" +
                    gatt.discoverServices());
            connected = true;
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
             Log.i(logTag, "Disconnected from GATT server.");
             connected = false;
        }
    }

    @Override
    // New services discovered
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
        	Log.i(logTag, "onServicesDiscovered received the following services:");
        	for (BluetoothGattService service: gatt.getServices()){
        		Log.i(logTag, "   "+ service.toString());
        	}
        } else {
            Log.w(logTag, "onServicesDiscovered received: " + status);
        }
    }

    @Override
    // Result of a characteristic read operation
    public void onCharacteristicRead(BluetoothGatt gatt,
            BluetoothGattCharacteristic characteristic,
            int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
               /*// This is special handling for the Heart Rate Measurement profile. Data
        	    // parsing is carried out as per profile specifications.
        	    if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
        	        int flag = characteristic.getProperties();
        	        int format = -1;
        	        if ((flag & 0x01) != 0) {
        	            format = BluetoothGattCharacteristic.FORMAT_UINT16;
        	            Log.d(TAG, "Heart rate format UINT16.");
        	        } else {
        	            format = BluetoothGattCharacteristic.FORMAT_UINT8;
        	            Log.d(TAG, "Heart rate format UINT8.");
        	        }
        	        final int heartRate = characteristic.getIntValue(format, 1);
        	        Log.d(TAG, String.format("Received heart rate: %d", heartRate));
        	        intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        	    } else {
        	        // For all other profiles, writes the data formatted in HEX.
        	        final byte[] data = characteristic.getValue();
        	        if (data != null && data.length > 0) {
        	            final StringBuilder stringBuilder = new StringBuilder(data.length);
        	            for(byte byteChar : data)
        	                stringBuilder.append(String.format("%02X ", byteChar));
        	            intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
        	                    stringBuilder.toString());
        	        }
        	    }
        	    sendBroadcast(intent);
        	}
        	*/
        	
        	
        }
    }
};
