package com.rfduino.core;

import com.samsung.bluetoothle.BluetoothLEClientChar;
import com.samsung.bluetoothle.BluetoothLEClientService;


public class RFDuinoBLEService extends BluetoothLEClientService {
	
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
