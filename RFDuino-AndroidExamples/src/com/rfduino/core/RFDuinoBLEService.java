package com.rfduino.core;

import android.bluetooth.BluetoothDevice;

import com.samsung.bluetoothle.BluetoothLEClientChar;
import com.samsung.bluetoothle.BluetoothLEClientService;


public class RFDuinoBLEService extends BluetoothLEClientService {
	public static final String REFRESH_CHAR = "com.rfduino.core.RFDuinoBLEService.action.REFRESH_CHAR";
	public static final String VALUE_REFRESH = "com.rfduino.core.RFDuinoBLEService.action.VALUE_CHAR";
	private static final String TAG = "RFDuinoBLEService";
	
	public static final byte BLE_START_FLAG = 0x1;
	public static final byte BLE_STOP_FLAG = 0x0;
	
	
	public RFDuinoBLEService(String UUID) {
		super(UUID);
	}
	
	
	@Override
	public void onWatcherValueChanged(BluetoothLEClientChar characteristic) {
		super.onWatcherValueChanged(characteristic);
		// TBD
	}
	
	@Override
	public boolean registerWatcher() {
		return super.registerWatcher();
	}

	@Override
	public boolean unregisterWatcher() {
		return super.unregisterWatcher();
	}
	  
	 
}
