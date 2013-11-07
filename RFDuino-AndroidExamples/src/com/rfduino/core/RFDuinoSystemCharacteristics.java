package com.rfduino.core;

public class RFDuinoSystemCharacteristics {
	//16 bit UUID custom RFDuino fields are turned into standard 128bit fields by following Bluetooth Base ID according to the BLE standard: 00000000-0000-1000-8000 00805F9B34FB
	
	//Service id to use to discover available services provided by the RFDuino hardware. Not to be confused with an Android "Service", which is used to run a long, ongoing process on the Android
	public static final String  RFDUINO_PROFILE_SERVICE_UUID = "00002220-0000-1000-8000 00805F9B34FB";
	
	
	public static final String  RFDUINO_PROFILE_SEND_UUID = "00002222-0000-1000-8000 00805F9B34FB";
	public static final String  RFDUINO_PROFILE_RECEIVE_UUID = "00002221-0000-1000-8000 00805F9B34FB";
	public static final String  RFDUINO_DISCONNECT_UUID = "00002223-0000-1000-8000 00805F9B34FB";
	 
}
