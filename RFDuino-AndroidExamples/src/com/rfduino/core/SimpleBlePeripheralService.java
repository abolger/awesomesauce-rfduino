package com.rfduino.core;

import java.util.ArrayList;

import com.samsung.bluetoothle.BluetoothLEClientChar;
import com.samsung.bluetoothle.BluetoothLEClientService;

import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


public class SimpleBlePeripheralService extends Service {
	
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

	@Override
	public void onCreate() {
		startForeground(999, new Notification());
	}

	@Override
	public void onDestroy() {
		disconnectLEDevice(mBluetoothProfile.getConnectedLEDevice());
		super.onDestroy();
	}

	public void connectLEDevice(BluetoothDevice paramBluetoothDevice) {
		if (mBluetoothProfile != null)
			mBluetoothProfile.connectLEDevice(paramBluetoothDevice);
	}

	public void disconnectLEDevice(BluetoothDevice paramBluetoothDevice) {
		if (mBluetoothProfile != null){
			mBluetoothProfile.disconnectLEDevice(paramBluetoothDevice);
			mBluetoothProfile.finish();
			mBluetoothProfile = null;
		}
	}

	public void discoverCharacteristics(BluetoothDevice paramBluetoothDevice) {
		mBluetoothProfile.discoverCharacteristics(paramBluetoothDevice);
	}

	public void discoverCharacteristics(BluetoothDevice paramBluetoothDevice,
			String uuid) {
		mBluetoothProfile.discoverCharacteristics(paramBluetoothDevice, uuid);
		
	}

	
	
	public void setRemoteDevice(BluetoothDevice paramBluetoothDevice) {
		mBluetoothProfile = new RFDuinoBLEProfile(getBaseContext());
		
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

	public BluetoothLEClientChar getCharbyUUID(BluetoothDevice device, String uuid)
	{
		return mBluetoothProfile.getCharbyUUID(device, uuid);
	}

	public ArrayList<BluetoothLEClientChar> getAllChars(BluetoothDevice device)
	{
		return mBluetoothProfile.getAllChars(device);
	}

	/*
	 * paramInt == 1 means WRITE_REQ. 
	 * fill in the data to be written in paramBluetoothLEClientChar.setCharValue(byte[] bytes)
	 */
	public int writeCharValue(BluetoothLEClientChar paramBluetoothLEClientChar, int paramInt)
	{
		return mBluetoothProfile.writeCharValue(paramBluetoothLEClientChar, paramInt);
	}


	
	

}
