package com.ble.bluetoothle;

import java.util.ArrayList;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.samsung.bluetoothle.BluetoothLEClientChar;
import com.samsung.bluetoothle.BluetoothLEClientProfile;
import com.samsung.bluetoothle.BluetoothLEClientService;

public class ExtendedBluetoothLEClientProfile extends BluetoothLEClientProfile
{
	private Context context;
	private ExtendedBluetoothLEClientService clientService;
	private static final String TAG = "ExtendedBluetoothLEClientProfile";

	public static final String onLEDeviceConnectedBroadcast = "com.ble.ExtendedBluetoothLEClientProfile.onLEDeviceConnected";
	public static final String onDiscoverCharacteristicsBroadcast = "com.ble.ExtendedBluetoothLEClientProfile.onDiscoverCharacteristics";
	
	public static final String bluetoothDeviceExtra = "com.ble.ExtendedBluetoothLEClientProfile.bluetoothDeviceExtra";
	
	public ExtendedBluetoothLEClientProfile(Context paramContext, String serviceUUID128)
	{
		super(paramContext);
		this.context = paramContext;
		clientService = new ExtendedBluetoothLEClientService(paramContext, serviceUUID128);
		ArrayList<BluetoothLEClientService> services = new ArrayList<BluetoothLEClientService>();
		services.add(clientService);
		registerLEProfile(services);
	}

	@Override
	public void onLEDeviceConnected(BluetoothDevice paramBluetoothDevice)
	{
		Log.i(TAG, "onLEDeviceConnected");

		Intent localIntent = new Intent();
		localIntent.setAction(onLEDeviceConnectedBroadcast);
		localIntent.putExtra(bluetoothDeviceExtra, paramBluetoothDevice);
		context.sendBroadcast(localIntent);

		super.onLEDeviceConnected(paramBluetoothDevice);
	}

	@Override
	public void onLEDeviceDisconnected(BluetoothDevice paramBluetoothDevice)
	{
		Log.i(TAG, "onLEDeviceDisconnected");

		super.onLEDeviceDisconnected(paramBluetoothDevice);
	}

	@Override
	public void onDiscoverCharacteristics(BluetoothDevice paramBluetoothDevice)
	{
		Log.i(TAG, "onDiscoverCharacteristics");

		Intent localIntent = new Intent();
		localIntent.setAction(onDiscoverCharacteristicsBroadcast);
		localIntent.putExtra(bluetoothDeviceExtra, paramBluetoothDevice);
		context.sendBroadcast(localIntent);

		super.onDiscoverCharacteristics(paramBluetoothDevice);
	}

	@Override
	public void onGetRssiValue(BluetoothDevice paramBluetoothDevice, String paramString)
	{
		Log.i(TAG, "onGetRssiValue");

		super.onGetRssiValue(paramBluetoothDevice, paramString);
	}

	@Override
	public void onLELinkLoss(BluetoothDevice paramBluetoothDevice)
	{
		Log.i(TAG, "onLELinkLoss");

		super.onLELinkLoss(paramBluetoothDevice);
	}

	public BluetoothLEClientChar getCharbyUUID(BluetoothDevice device, String uuid)
	{
		return clientService.getCharbyUUID(device, uuid);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<BluetoothLEClientChar> getAllChars(BluetoothDevice device)
	{
		return clientService.getAllChars(device);
	}

	/*
	 * paramInt == 1 means WRITE_REQ. No idea what 0 does.
	 * fill in the data to be written in paramBluetoothLEClientChar.setCharValue(byte[] bytes)
	 */
	public int writeCharValue(BluetoothLEClientChar paramBluetoothLEClientChar, int paramInt)
	{
		return clientService.writeCharValue(paramBluetoothLEClientChar, paramInt);
	}
	
	public int writeClientConfigDesc(BluetoothLEClientChar paramBluetoothLEClientChar)
	{
		return clientService.writeClientConfigDesc(paramBluetoothLEClientChar);
	}

	public void discoverCharacteristics(BluetoothDevice paramBluetoothDevice)
	{
		super.discoverCharacteristics(paramBluetoothDevice);
	}

	public void registerWatcher()
	{
		clientService.registerWatcher();
	}

	public void unregisterWatcher()
	{
		clientService.unregisterWatcher();
	}

	public void unregister()
	{
		unregisterLEProfile();
	}

	public void discoverCharacteristics(BluetoothDevice paramBluetoothDevice, String uuid128)
	{
		clientService.discoverCharacteristics(paramBluetoothDevice, uuid128);
	}
}
