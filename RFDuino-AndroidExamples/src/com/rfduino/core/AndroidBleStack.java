package com.rfduino.core;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Build;

/** Class that holds all the standard Bluetooth SDK functions that became available with Android 4.3.
 *  These functions will get called from the RFDuinoBluetooth class if the Android device is running Android 4.3 or later.
 *  
 **/

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class AndroidBleStack {
	public static BluetoothAdapter.LeScanCallback onBluetoothFoundCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
			for (BluetoothDevice d: BluetoothLEStack.discoveredDevices){
				if (d.getAddress().equals(device.getAddress())){
					//We found the same device again- don't re-add it to the list. 
					return; 
				}
			}
			BluetoothLEStack.discoveredDevices.add(device);
			
		}
	};
	
}
