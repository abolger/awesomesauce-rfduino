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
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.ArrayAdapter;

public abstract class BluetoothLEStack {
	public final static String logTag = "BluetoothLEStack";
	private final static int ANDROID_BLUETOOTH_ENABLE_REQ = 123;
	
	public static ArrayList<BluetoothDevice> discoveredDevices = new ArrayList<BluetoothDevice>();
	protected static boolean scanning = false;
	protected Context hostAndroidActivity;
	protected BluetoothDevice connectedDevice;
	public List<String> allowedUUIDs = null;
	protected String presentUUIDtoRead;
	
	
	
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
	public static BluetoothLEStack connectToBluetoothLEStack(BluetoothDevice device, Activity hostActivity, String uuidStringRepresentation){
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
			return new AndroidBleStack(device, null, hostActivity);
			
		} else {
			//See if Samsung (Broadcom-based) Bluetooth Low Energy stack will work- works on Android 4.1 and 4.2
			return new SamsungBleStack(device, hostActivity);
		}
		
	}


	public abstract void disconnect();
	
	
	public abstract void readBLECharacteristic(String UUID);
	

	
}

