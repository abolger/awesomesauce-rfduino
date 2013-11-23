package com.rfduino.core;

import java.util.ArrayList;
import java.util.List;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.samsung.bluetoothle.BluetoothLEClientChar;
import com.samsung.bluetoothle.BluetoothLEClientProfile;
import com.samsung.bluetoothle.BluetoothLEClientService;
import com.samsung.bluetoothle.BluetoothLENamespace;

/** 
 * RFDuinoBLEProfile.java
 * 
 * Spare implementation of Samsung's abstracted BluetoothLEClientProfile API that allows us to connect to a BluetoothLEDevice. 
 * 
 * This class stores code that needed to be implemented to use the hidden Bluetooth Low Energy API that is available starting
 * in some Samsung Android devices as early as Android 4.1. This class is completely unnecessary if your Android platform is running Android 4.3 or later.
 * 
 * 
 * @author adrienne
 *
 * This library is released under the LGPL. A copy of the license should have been distributed with this library/source code,
 *  if not, you can read it here: (https://github.com/abolger/awesomesauce-rfduino/blob/master/LICENSE)
*/
public class RFDuinoBLEProfile extends BluetoothLEClientProfile {
	private final String logTag = "RFDuinoBLEProfile";
	private RFDuinoBLEService rfduinoService = null;
	
	private boolean watcherRegistered = false;
	
	//private ACCService aService = null;
	private Context mContext;

	

	public RFDuinoBLEProfile(Context baseContext) {
		super(baseContext);
		this.mContext = baseContext;
		ArrayList<BluetoothLEClientService> potentialRfduinoServices = new ArrayList<BluetoothLEClientService>();
		rfduinoService = new RFDuinoBLEService(RFDuinoSystemCharacteristics.RFDUINO_PROFILE_SERVICE_UUID);
		
		potentialRfduinoServices.add (rfduinoService);
		
		registerLEProfile(potentialRfduinoServices);
		
	}

	public void unregister() {
		if (watcherRegistered){
			rfduinoService.unregisterWatcher();
		}
		unregisterLEProfile();
		
	}

	@Override
	public void discoverCharacteristics(BluetoothDevice device) {
		Log.d(logTag, "Device"+ device.getAddress() +" discover characteristics called.");
		super.discoverCharacteristics(device);
		
	}

	

	@Override
	public void onDiscoverCharacteristics(BluetoothDevice device) {
		super.onDiscoverCharacteristics(device);
		Intent localIntent = new Intent();
		localIntent.setAction(BluetoothLEStack.CHARACTERISTICS_REFRESH).putExtra(
				BluetoothDevice.EXTRA_DEVICE, device);
		this.mContext.sendBroadcast(localIntent);

		
	}
	
	
	@Override
	public void onGetRssiValue(BluetoothDevice device, String value) {
		Intent localIntent = new Intent();
		localIntent.setAction(BluetoothLEStack.DEVICE_RSSI_VAL)
				.putExtra(BluetoothDevice.EXTRA_DEVICE, device)
				.putExtra(BluetoothDevice.EXTRA_RSSI, value);
		this.mContext.sendBroadcast(localIntent);
	}

	@Override
	public void onLEDeviceConnected(BluetoothDevice device) {
		super.onLEDeviceConnected(device);
		Intent localIntent = new Intent();
		localIntent.setAction(BluetoothLEStack.DEVICE_LED_CONNECTED).putExtra(
				BluetoothDevice.EXTRA_DEVICE, device);
		this.mContext.sendBroadcast(localIntent);
	}

	@Override
	public void onLEDeviceDisconnected(BluetoothDevice device) {
		rfduinoService.unregisterWatcher(); //Tells the service to pay attention when whatever values we happen to be reading change.
		super.onLEDeviceDisconnected(device);
		Intent localIntent = new Intent();
		localIntent.setAction(BluetoothLEStack.DEVICE_DISCONNECTED).putExtra(
				BluetoothDevice.EXTRA_DEVICE, device);
		this.mContext.sendBroadcast(localIntent);
	}

	@Override
	public void onLELinkLoss(BluetoothDevice device) {
		Intent localIntent = new Intent();
		localIntent.setAction(BluetoothLEStack.DEVICE_LINK_LOSS).putExtra(
				BluetoothDevice.EXTRA_DEVICE, device);
		this.mContext.sendBroadcast(localIntent);
	}


	public BluetoothLEClientChar getCharbyUUID(BluetoothDevice device, String uuid)
	{
		BluetoothLEClientChar c = rfduinoService.getCharbyUUID(device, uuid);
		if (c != null) return c;
		
		return null; //No such characteristic UUID available. 
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<BluetoothLEClientChar> getAllChars(BluetoothDevice device)
	{
		ArrayList<BluetoothLEClientChar> all= new ArrayList<BluetoothLEClientChar>();
		
		List<BluetoothLEClientChar> chars = rfduinoService.getAllChars(device);
		if (chars != null){	all.addAll(chars); } 
		
		
		return all;
	}

	public void discoverCharacteristics(BluetoothDevice paramBluetoothDevice,
			String uuid) {
		rfduinoService.discoverCharacteristics(paramBluetoothDevice, uuid);
		
	}
	
	public void registerOnCharChangedCallback(String uuid, Runnable callback){
		if (watcherRegistered){
			rfduinoService.unregisterWatcher();
		}
		rfduinoService.registerWatcher(); //Tells the service to pay attention when whatever values we happen to be reading change.
		watcherRegistered = true;
		
		//Enable this UUID to notify us on change:
		BluetoothLEClientChar c = getCharbyUUID(getConnectedLEDevice(), uuid);
		if(c != null){
			c.setClientConfigDesc(new byte[]{1});
			int enabled = writeClientConfigDesc(c);
		}
		
		rfduinoService.callbackMap.put(uuid, callback);
	}
	

	/*
	 * paramInt == 1 means WRITE_REQ. 
	 * fill in the data to be written in paramBluetoothLEClientChar.setCharValue(byte[] bytes)
	 */
	public int writeCharValue(BluetoothLEClientChar paramBluetoothLEClientChar, int paramInt)
	{
		return rfduinoService.writeCharValue(paramBluetoothLEClientChar,paramInt);
	}

	public int writeClientConfigDesc(BluetoothLEClientChar c) {
		return  rfduinoService.writeClientConfigDesc(c);
	}
	
	
	
	 
	
}