package com.ble.bluetoothle;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.samsung.bluetoothle.BluetoothLEClientChar;
import com.samsung.bluetoothle.BluetoothLEClientService;

public class ExtendedBluetoothLEClientService extends BluetoothLEClientService
{
	private static final String TAG = "ExtendedBluetoothLEClientService";
	private Context mContext = null;
	
	public static final String onDiscoverCharacteristicsBroadcast = "com.ble.ExtendedBluetoothLEClientService.onDiscoverCharacteristics";
	public static final String onWatcherValueChangedBroadcast = 	"com.ble.ExtendedBluetoothLEClientService.onWatcherValueChanged";
	
	// The extra seems to be total rubbish.
	// It returns a BluetoothLEClientChar with everything uninitialized!!!! not even the uuid is set...
	public static final String bluetoothLEClientCharExtra = "com.ble.ExtendedBluetoothLEClientService.bluetoothLEClientCharExtra";

	public ExtendedBluetoothLEClientService(Context context, String serviceUUID128)
	{
		super(serviceUUID128);
		mContext = context;
	}

	@Override
	public void onDiscoverCharacteristics(BluetoothLEClientChar paramBluetoothLEClientChar)
	{
		Log.i(TAG, "onDiscoverCharacteristics");
		
		Intent localIntent = new Intent(onDiscoverCharacteristicsBroadcast);
		localIntent.putExtra(bluetoothLEClientCharExtra, paramBluetoothLEClientChar);
		mContext.sendBroadcast(localIntent);
		
		super.onDiscoverCharacteristics(paramBluetoothLEClientChar);
	}

	@Override
	public void onWatcherValueChanged(BluetoothLEClientChar paramBluetoothLEClientChar)
	{
		Log.i(TAG, "onWatcherValueChanged");
		
		Intent localIntent = new Intent(onWatcherValueChangedBroadcast);
		localIntent.putExtra(bluetoothLEClientCharExtra, paramBluetoothLEClientChar);
		mContext.sendBroadcast(localIntent);
		
		super.onWatcherValueChanged(paramBluetoothLEClientChar);
	}

	public boolean registerWatcher()
	{
		return super.registerWatcher();
	}

	public boolean unregisterWatcher()
	{
		return super.unregisterWatcher();
	}
}
