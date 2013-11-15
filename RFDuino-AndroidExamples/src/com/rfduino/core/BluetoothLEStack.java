package com.rfduino.core;
/*
Copyright (c) 2013 Adrienne Bolger.  All right reserved.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;

/** 
 * 
 * BluetoothLEStack.java
 * 
 *  Provide Simplified Android bindings to link an RFDuino Board to an Android application over Bluetooth. 
 *  This stack supports the following devices:
 *  1. Any Android device that has the Bluetooth LE hardware AND is running Android 4.3+. (JELLY BEAN, MR2)
 *  2. Samsung Android 4.1+ devices that come equipped with a Bluetooth LE hardware. 
 *  
 * @author adrienne
 *
 * This library is released under the LGPL. A copy of the license should have been distributed with this library/source code,
 *  if not, you can read it here: (https://github.com/abolger/awesomesauce-rfduino/blob/master/LICENSE)
*/
public abstract class BluetoothLEStack {
	//String constants
	public static final String CHARACTERISTICS_REFRESH = "com.rfduino.core.bleprofile.action.refresh";
	public static final String DEVICE_LED_CONNECTED = "com.rfduino.core.bleprofile.action.connected";
	public static final String DEVICE_DISCONNECTED = "com.rfduino.core.bleprofile.action.disconnected";
	public static final String DEVICE_LINK_LOSS = "com.rfduino.core.bleprofile.action.linkloss";
	public static final String DEVICE_RSSI_VAL = "com.rfduino.core.bleprofile.rssi";
	public final static String logTag = "BluetoothLEStack";
	private final static int ANDROID_BLUETOOTH_ENABLE_REQ = 123;
	
	
	
	//Variables usable before a specific connection has been created:
	protected static BluetoothLEStack singleton; //Only allow us to have one BluetoothLEStack connection at a time. 
	public static ArrayList<BluetoothDevice> discoveredDevices = new ArrayList<BluetoothDevice>();
	public static ProgressDialog bluetoothConnectionWaitWindow; 
	protected static boolean scanning = false;
	
	
	
	protected boolean disconnectCalled = false;
	protected boolean successfulConnection = false;
	protected Context hostAndroidActivity;
	protected BluetoothDevice connectedDevice;
	protected HashMap<String, byte[]> uuidsTolatestValues = new HashMap<String, byte[]>();
	protected List<String> allowedUUIDs;
	protected String presentUUIDtoRead;
	protected String presentUUIDtoWrite;
	
	
	
	public static void showFoundBluetoothDevices(Context hostActivity, OnClickListener rfduinoChosenListener) {
		//An ArrayAdapter is a connector class: the adapter automatically checks for updates in our underlying datastructure and 
		// populates the results (Bluetooth radios in our case) to the ListView to be seen by the user. 
		ArrayAdapter<BluetoothDevice> adapter = new ArrayAdapter<BluetoothDevice>(hostActivity, android.R.layout.select_dialog_item, BluetoothLEStack.discoveredDevices);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(hostActivity);
		    builder.setTitle("Choose an available RFDuino radio:")
		    	   .setAdapter(adapter, rfduinoChosenListener);
		   
		builder.show();
	}
	
	
	
	
	
	/** Non-blocking function that scans for all Bluetooth devices available to the Android phone. Any resulting devices are stored in discoveredDevices.
	 *  Note that actively searching for devices can take battery and processing power, so the developer is reponsible for calling 
	 *  "stopSearchingForBluetoothDevices" function when they are done.
	 * **/
	@SuppressLint("NewApi")
	public static boolean beginSearchingForBluetoothDevices(Activity hostActivity){
		//If Bluetooth is not turned on, display notice to the user requesting permission to turn it on. 
		if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    hostActivity.startActivityForResult(enableBtIntent, ANDROID_BLUETOOTH_ENABLE_REQ);
		}
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
			AndroidBleStack.startLeScan(); 
			scanning = true;
		} else {
			
			try {
				SamsungBleStack.loadSamsungLibraries(hostActivity.getApplicationContext());
				SamsungBleStack.startLeScan(hostActivity);
				scanning = true;
				Log.w(logTag, "Starting BLE device scan");
				
			} catch (Exception e){
				Log.w(logTag, "Failed to use Samsung libraries to support BLE. No BLE support available for this device");
				scanning = false;
			}
			
			
		}
		return scanning;
	}
	
	@SuppressLint("NewApi")
	public static void stopSearchingForBluetoothDevices(Activity hostActivity){

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
			AndroidBleStack.stopLeScan();	
			scanning = false;
		} else {
			Log.w("RFDuinoBluetooth", "Bluetooth 4.3 API Not supported, attempting to use Samsung BLE library instead...");
			SamsungBleStack.stopLeScan(hostActivity);
			scanning = false;
		}
		
	}
	
	
	
	
	/** Connect to a chosen Bluetooth Device using whatever BLE Stack is available. **/
	public static BluetoothLEStack connectToBluetoothLEStack(BluetoothDevice device, Activity hostActivity, String uuidStringRepresentation, OnCancelListener onCancelConnectionAttempt){
		bluetoothConnectionWaitWindow = new ProgressDialog(hostActivity);
		bluetoothConnectionWaitWindow.setTitle("Connecting to Bluetooth Radio " + device.getAddress());
		bluetoothConnectionWaitWindow.setMessage("Please wait...");
		bluetoothConnectionWaitWindow.setCancelable(false);
		
		if (onCancelConnectionAttempt != null){
			bluetoothConnectionWaitWindow.setCancelable(true);
			bluetoothConnectionWaitWindow.setOnCancelListener(onCancelConnectionAttempt);
		}
		bluetoothConnectionWaitWindow.setIndeterminate(true);
		bluetoothConnectionWaitWindow.show();
		
		
		if (singleton != null){
			singleton.disconnect();
			singleton.connect(device, hostActivity);
			
		} else {
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
			singleton =  new AndroidBleStack(device, null, hostActivity);
			
		} else {
			//See if Samsung (Broadcom-based) Bluetooth Low Energy stack will work- works on Android 4.1 and 4.2
			singleton = new SamsungBleStack(device, hostActivity);
		}
		}
		return singleton;
	}

	
	
	protected abstract void connect(BluetoothDevice device, Activity hostActivity);

	public abstract void disconnect();
	
	public boolean isConnected(){
		return successfulConnection && !disconnectCalled;
	}
	
	
	/** Gets the latest available services from the Bluetooth Device. Returns false if the update could not be performed, true if the update is underway. 
	 * Resulting services are accessible using getDiscoveredCharacteristics();
	 * **/
	public abstract boolean discoverAvailableCharacteristics();
	

	/** Returns UUIDs of all characteristics that can be read from this BluetoothDevice. What the UUID translates to  
	 * is specified in the Bluetooth Profile standard here:https://developer.bluetooth.org/gatt/profiles/Pages/ProfilesHome.aspx
	 * or may be custom to the hardware. 
	 **/
	public List<String> getDiscoveredCharacteristics() {
		return allowedUUIDs;
	}
	
	public abstract void selectCharacteristicToRead(String UUID);
	
	/** Returns true if write was successful, false if write failed. **/
	public abstract boolean writeCharacteristic(String uuid, byte[] value);
	
	
	public Map<String, byte[]> getLatestCharacteristics(){
		return uuidsTolatestValues;
	}
	
	public abstract void startReadingRSSI();
	public abstract void stopReadingRSSI();
	public abstract Integer getLatestRSSIValue();
	
	
	/**
	 * Bluetooth LE Characteristics are transmitted strangley, so provide a utility method to translate: 
	 * Translates character array returned from Bluetooth LE Characteristic into an array of Big-Endian integers: 
	 * ['1','2','3','4'] will return [0x12, 0x34]. 
	 */
	public static int[] hexStringToIntArray(byte[] charArray){
		String sRepr = new String(charArray);
		int result[] = new int[charArray.length/2];

		for(int i=0; i<charArray.length; i+=2)
		{
			result[i/2]  = Integer.parseInt( sRepr.substring(i, i+2), 16);
		}

		return result;
	}
	
	
}

