package com.rfduino.core;

import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


public class SimpleBlePeripheralService extends Service {

	private static final String TAG = "ECGAndroidService";
	private final IBinder mBinder = new ServiceBinder();
	private RFDuinoBLEProfile mBluetoothProfile = null;


	public class ServiceBinder extends Binder {
		public SimpleBlePeripheralService getService() {
			return SimpleBlePeripheralService.this;
		}
	}

	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public void onCreate() {
		mBluetoothProfile = new RFDuinoBLEProfile(getBaseContext());
		startForeground(999, new Notification());
	}

	public void onDestroy() {
		mBluetoothProfile.unregister();
		mBluetoothProfile.finish();
		super.onDestroy();
	}

	public void connectLEDevice(BluetoothDevice paramBluetoothDevice) {
		if (mBluetoothProfile != null)
			mBluetoothProfile.connectLEDevice(paramBluetoothDevice);
	}

	public void disconnectLEDevice(BluetoothDevice paramBluetoothDevice) {
		if (mBluetoothProfile != null)
			mBluetoothProfile.disconnectLEDevice(paramBluetoothDevice);
	}

	public void discoverCharacteristics(BluetoothDevice paramBluetoothDevice) {
		mBluetoothProfile.discoverCharacteristics(paramBluetoothDevice);
	}

	public void discoverCharByUuid(BluetoothDevice paramBluetoothDevice,
			String paramString) {
		mBluetoothProfile.discoverCharByUuid(paramBluetoothDevice,
				paramString);
	}

	public void setRemoteDevice(BluetoothDevice paramBluetoothDevice) {
		mBluetoothProfile.setRemoteDevice(paramBluetoothDevice);
	}

	public BluetoothDevice getConnectedLEDevice() {
		return mBluetoothProfile.getConnectedLEDevice();
	}

	public int getProfileState() {
		return mBluetoothProfile.getLEProfileState();
	}

	public void getRssiValue(BluetoothDevice paramBluetoothDevice) {
		mBluetoothProfile.getRssiValue(paramBluetoothDevice);
	}
	/*
	 * 
	public void startECGRecording(BluetoothDevice device, int startFlag){
		mBluetoothProfile.startECGRecording(device, startFlag);	
	}*/
}
