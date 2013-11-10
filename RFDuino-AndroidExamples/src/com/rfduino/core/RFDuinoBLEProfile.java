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

public class RFDuinoBLEProfile extends BluetoothLEClientProfile {
	private final String logTag = "RFDuinoBLEProfile";
	private ArrayList<BluetoothLEClientService> eServices = null;
	//private ACCService aService = null;
	private Context mContext;

	

	public RFDuinoBLEProfile(Context baseContext, ArrayList<BluetoothLEClientService> desiredServices) {
		super(baseContext);
		this.mContext = baseContext;
		eServices = desiredServices; 
		registerLEProfile(eServices);
	}

	public void unregister() {
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
		for (BluetoothLEClientService service: eServices){
			BluetoothLEClientChar c = service.getCharbyUUID(device, uuid);
			if (c != null) return c;
		}
		return null; //No such characteristic UUID available. 
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<BluetoothLEClientChar> getAllChars(BluetoothDevice device)
	{
		ArrayList<BluetoothLEClientChar> all= new ArrayList<BluetoothLEClientChar>();
		for (BluetoothLEClientService service: eServices){
			List<BluetoothLEClientChar> chars = service.getAllChars(device);
			if (chars != null){	all.addAll(chars); } 
		}

		return all;
	}

	public void discoverCharacteristics(BluetoothDevice paramBluetoothDevice,
			String uuid) {
		
		for (BluetoothLEClientService service: eServices){
			service.discoverCharacteristics(paramBluetoothDevice, uuid);
		}
			
		
	}

	
	
	 
	
}