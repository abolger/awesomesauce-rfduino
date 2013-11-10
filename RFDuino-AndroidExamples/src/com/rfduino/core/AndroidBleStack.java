package com.rfduino.core;

import com.samsung.bluetoothle.BluetoothLEClientChar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;

/** Class that holds all the standard Bluetooth SDK functions that became available with Android 4.3.
 *  These functions will get called from the RFDuinoBluetooth class if the Android device is running Android 4.3 or later.
 *  
 **/

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class AndroidBleStack extends BluetoothLEStack{
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
	

	private android.bluetooth.BluetoothGatt rfduinoGattServer;
	private android.bluetooth.BluetoothGattCallback activityGattCallback;
	
	/** Changes callback function for a radio- stops whatever callback function was being performed before and begins performing new set of callbacks.**/
	public void registerCallbackFunction(Context hostAndroidActivity, android.bluetooth.BluetoothGattCallback customCallbackFunction){
		activityGattCallback = customCallbackFunction;
		if (rfduinoGattServer != null) rfduinoGattServer.disconnect();
		rfduinoGattServer.getDevice().connectGatt(hostAndroidActivity, false, activityGattCallback);
	}
	
	
	/** Connects to a chosen Bluetooth device and registers the callback function to be invoked when we get data from this device. **/
	public AndroidBleStack(BluetoothDevice device, android.bluetooth.BluetoothGattCallback customCallbackFunction, Context hostActivity) {
		hostAndroidActivity = hostActivity;
		activityGattCallback = customCallbackFunction;
		rfduinoGattServer = device.connectGatt(hostAndroidActivity, false, activityGattCallback);
		
		
	}


	public static void stopLeScan() {
		BluetoothAdapter.getDefaultAdapter().stopLeScan(AndroidBleStack.onBluetoothFoundCallback);
	}


	public static void startLeScan() {
		BluetoothAdapter.getDefaultAdapter().startLeScan(AndroidBleStack.onBluetoothFoundCallback);
		
	}


	@Override
	public void disconnect() {
		rfduinoGattServer.disconnect();
		//hostAndroidActivity.unregisterReceiver(receiver);
		
	}


	@Override
	public boolean discoverAvailableCharacteristics() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void selectCharacteristicToRead(String UUID) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void startReadingRSSI() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void stopReadingRSSI() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Integer getLatestRSSIValue() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected void connect(BluetoothDevice device, Activity hostActivity) {
		// TODO Auto-generated method stub
		
	}

	
}
