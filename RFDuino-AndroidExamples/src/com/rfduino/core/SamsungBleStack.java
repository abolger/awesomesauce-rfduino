package com.rfduino.core;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.ble.bluetoothle.BluetoothAdapterHidden;
import com.ble.bluetoothle.BluetoothDeviceHidden;

/** This class stores all the code that needed to be implemented to use the hidden Bluetooth Low Energy API that is available starting
 * in some devices as early as Android 4.1, but isn't needed at all for Android 4.3.
 * @author adrienne
 *
 */
public class SamsungBleStack {
	private static final int enableBluetoothActivityResult = 1;
	private static final int discoveryActivityResult = 2;
	private static final String TAG = "PairingFragment";

	
	//For Android 4.1, 4.2 (which doesn't have BluetoothAdapter.LeScanCallback class:  
	// broadcast receiver to receive the broadcasts while scanning for bt le devices
	public static final BroadcastReceiver onBluetoothFoundReceiver = new BroadcastReceiver()
	{
		public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
		{
			String str = paramAnonymousIntent.getAction();
			if ("android.bluetooth.device.action.FOUND".equals(str))
			{
				BluetoothDevice localBluetoothDevice = (BluetoothDevice) paramAnonymousIntent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
				if(BluetoothDeviceHidden.getDeviceType(localBluetoothDevice) == 1)
				{
					short s = paramAnonymousIntent.getShortExtra("android.bluetooth.device.extra.RSSI", (short)0);
					for (BluetoothDevice d: BluetoothLEStack.discoveredDevices){
						if (d.getAddress().equals(localBluetoothDevice.getAddress())){
							//We found the same device again- don't re-add it to the list. 
							return; 
						}
					}
					BluetoothLEStack.discoveredDevices.add(localBluetoothDevice);
				}
			}
			
			if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(str))
			{
				if (BluetoothAdapter.getDefaultAdapter().isDiscovering()){
					BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
				}
				
			}
		}
	};
	
	
	/**
	 * Provide an implementation of the BluetoothLE stack using Samsung's com.samsung.ble SDK for Android devices earlier than 4.3
	 * 
	 */
	public static void startLeScan(Context hostActivity){
		// register broadcast receiver to receive scan results
		IntentFilter localIntentFilter = new IntentFilter("android.bluetooth.device.action.FOUND");
		localIntentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
		hostActivity.registerReceiver(onBluetoothFoundReceiver, localIntentFilter);

		// begin scan
		BluetoothAdapterHidden.startLeDiscovery(BluetoothAdapter.getDefaultAdapter());
		
	}


	public static void stopLeScan(Activity hostActivity) {
		//Stop responding to found items:
		hostActivity.unregisterReceiver(onBluetoothFoundReceiver);
		if (BluetoothAdapter.getDefaultAdapter().isDiscovering()){
			BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
		}
	}
	
	 
	
}
