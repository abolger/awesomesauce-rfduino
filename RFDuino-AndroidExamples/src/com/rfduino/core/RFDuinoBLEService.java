package com.rfduino.core;

import com.samsung.bluetoothle.BluetoothLEClientChar;
import com.samsung.bluetoothle.BluetoothLEClientService;

/** 
 * RFDuinoBLEService.java
 * 
 * Spare implementation of Samsung's abstracted BluetoothLEClientService API that allows us to connect to a BluetoothLEDevice. 
 * 
 * This class stores code that needed to be implemented to use the hidden Bluetooth Low Energy API that is available starting
 * in some Samsung Android devices as early as Android 4.1. This class is completely unnecessary if your Android platform is running Android 4.3 or later.
 * 
 * @author adrienne
 *
 * This library is released under the LGPL. A copy of the license should have been distributed with this library/source code,
 *  if not, you can read it here: (https://github.com/abolger/awesomesauce-rfduino/blob/master/LICENSE)
*/
public class RFDuinoBLEService extends BluetoothLEClientService {
	
	public RFDuinoBLEService(String UUID) {
		super(UUID);
	}
	
	
/*	@Override
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
	*/  
	 
}
