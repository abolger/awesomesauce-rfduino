package com.rfduino.core;

import com.samsung.bluetoothle.BluetoothLEClientChar;
import com.samsung.bluetoothle.BluetoothLEClientService;


public class RFDuinoBLEService extends BluetoothLEClientService {
	public static final String REFRESH_CHAR = "com.rfduino.core.RFDuinoBLEService.action.REFRESH_CHAR";
	public static final String VALUE_REFRESH = "com.rfduino.core.RFDuinoBLEService.action.VALUE_CHAR";
	
	
	private static final String TAG = "RFDuinoBLEService";
	
	public RFDuinoBLEService() {
		super(RFDuinoSystemCharacteristics.RFDUINO_PROFILE_SERVICE_UUID);
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
