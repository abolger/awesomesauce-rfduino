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

public class BluetoothLEStack {
	private final static int ANDROID_BLUETOOTH_ENABLE_REQ = 123;
	
	public static ArrayList<BluetoothDevice> discoveredDevices = new ArrayList<BluetoothDevice>();
	private static boolean scanning = false;
	
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
	public static void beginSearchingForBluetoothDevices(Activity hostActivity){
		//If Bluetooth is not turned on, display notice to the user requesting permission to turn it on. 
		if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    hostActivity.startActivityForResult(enableBtIntent, ANDROID_BLUETOOTH_ENABLE_REQ);
		}
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
			BluetoothAdapter.getDefaultAdapter().startLeScan(AndroidBleStack.onBluetoothFoundCallback);
			scanning = true;
		} else {
			Log.w("RFDuinoBluetooth", "Bluetooth 4.3 API Not supported yet, attempting to use Samsung BLE library instead...");
			SamsungBleStack.startLeScan(hostActivity);
			scanning = true;
		}
	}
	
	@SuppressLint("NewApi")
	public static void stopSearchingForBluetoothDevices(Activity hostActivity){

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
				BluetoothAdapter.getDefaultAdapter().stopLeScan(AndroidBleStack.onBluetoothFoundCallback);
			scanning = false;
		} else {
			Log.w("RFDuinoBluetooth", "Bluetooth 4.3 API Not supported, attempting to use Samsung BLE library instead...");
			SamsungBleStack.stopLeScan(hostActivity);
			scanning = false;
		}
		
	}
	
	
	
	
	
	
	
	
	
//	
//
//
//	private android.bluetooth.BluetoothGatt rfduinoGattServer;
//	private android.bluetooth.BluetoothGattCallback activityGattCallback;
//	private Context hostAndroidActivity;
//	
//	/** Changes callback function for a radio- stops whatever callback function was being performed before and begins performing new set of callbacks.**/
//	public void registerCallbackFunction(android.bluetooth.BluetoothGattCallback customCallbackFunction){
//		activityGattCallback = customCallbackFunction;
//		if (rfduinoGattServer != null) rfduinoGattServer.disconnect();
//		rfduinoGattServer.getDevice().connectGatt(hostAndroidActivity, false, activityGattCallback);
//	}
//	
//	
//	
//	/** Connects to a chosen Bluetooth device by known bluetooth MAC address
//	*   and registers the callback function to be invoked when we get data from this device.
//	 **/
//	public RFDuinoBluetooth(String address, android.bluetooth.BluetoothGattCallback customCallbackFunction, Context hostActivity) {
//		BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
//		hostAndroidActivity = hostActivity;
//		activityGattCallback = customCallbackFunction;
//		rfduinoGattServer = device.connectGatt(hostAndroidActivity, false, activityGattCallback);
//		
//	}
//	
//	/** Connects to a chosen Bluetooth device and registers the callback function to be invoked when we get data from this device. **/
//	public RFDuinoBluetooth(BluetoothDevice device, android.bluetooth.BluetoothGattCallback customCallbackFunction, Context hostActivity) {
//		hostAndroidActivity = hostActivity;
//		activityGattCallback = customCallbackFunction;
//		rfduinoGattServer = device.connectGatt(hostAndroidActivity, false, activityGattCallback);
//		
//		
//	}
//	
	
}

