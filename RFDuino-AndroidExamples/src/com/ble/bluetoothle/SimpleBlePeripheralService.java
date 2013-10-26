package com.ble.bluetoothle;

import java.util.ArrayList;
import java.util.Set;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.samsung.bluetoothle.BluetoothLEClientChar;

public class SimpleBlePeripheralService extends Service
{
	// UUIDs. all must be of 128bit length 
	public static final String serviceUUID128 						= "f000fff0-0451-4000-b000-000000000000"; // very important to get this right. Discover characteristic will fail otherwise.
	public static final String SIMPLEPROFILE_CHAR1_UUID 			= "f000fff1-0451-4000-b000-000000000000";
	public static final String SIMPLEPROFILE_CHAR2_UUID 			= "f000fff2-0451-4000-b000-000000000000";
	public static final String SIMPLEPROFILE_CHAR3_UUID 			= "f000fff3-0451-4000-b000-000000000000";
	public static final String SIMPLEPROFILE_CHAR4_UUID 			= "f000fff4-0451-4000-b000-000000000000";
	public static final String SIMPLEPROFILE_CHAR5_UUID 			= "f000fff5-0451-4000-b000-000000000000";
	public static final String SIMPLEPROFILE_NOTIFY_WORKAROUND_UUID = "f000fff6-0451-4000-b000-000000000000";

	private static final String TAG = "SimpleBlePeripheralService";
	private final IBinder binder = new BluetoothLEBinder();

	public ExtendedBluetoothLEClientProfile bluetoothLEClientProfile = null;

	public BluetoothDevice connectedDevice = null;
	private boolean watcherRegistered = false;

	public boolean connectLEDevice(BluetoothDevice paramBluetoothDevice)
	{
		// set remote device
		setRemoteDevice(paramBluetoothDevice);

		// unbond everything including non-bluetooth LE devices
		Set<BluetoothDevice> bondedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
		for (BluetoothDevice bluetoothDevice : bondedDevices) 
		{
			Log.i(TAG, "unbonding "+ bluetoothDevice.getAddress());
			BluetoothDeviceHidden.removeBond(bluetoothDevice);
		}

		// create bond
		boolean bondCreation = BluetoothDeviceHidden.createBond(paramBluetoothDevice);
		Log.i(TAG, "createBond result is " + bondCreation);

		// perform the actual connection
		Log.i(TAG, "Connecting to " + paramBluetoothDevice.getAddress());
		if(bluetoothLEClientProfile.connectLEDevice(paramBluetoothDevice) == true)
		{
			connectedDevice = paramBluetoothDevice;
			return true;
		}
		else
			return false;
	}

	public void disconnectLEDevice(BluetoothDevice paramBluetoothDevice)
	{
		connectedDevice = null;
		bluetoothLEClientProfile.disconnectLEDevice(paramBluetoothDevice);
	}

	public void discoverCharacteristics(BluetoothDevice paramBluetoothDevice)
	{			
		bluetoothLEClientProfile.discoverCharacteristics(paramBluetoothDevice);
	}

	public void discoverCharacteristics(BluetoothDevice paramBluetoothDevice, String uuid128)
	{			
		bluetoothLEClientProfile.discoverCharacteristics(paramBluetoothDevice, uuid128);
	}

	public BluetoothDevice getConnectedDevice()
	{
		return bluetoothLEClientProfile.getConnectedLEDevice();
	}

	public BluetoothDevice getConnectedLEDevice()
	{
		return bluetoothLEClientProfile.getConnectedLEDevice();
	}

	public int getProfileState()
	{
		return bluetoothLEClientProfile.getLEProfileState();
	}

	public void onCreate()
	{
		bluetoothLEClientProfile = new ExtendedBluetoothLEClientProfile(getBaseContext(), serviceUUID128);
	}

	public void onDestroy()
	{		
		if(getProfileState() == 2 && connectedDevice != null)
		{
			bluetoothLEClientProfile.unregisterWatcher();
			disconnectLEDevice(connectedDevice);
		}

		bluetoothLEClientProfile.unregister();
		bluetoothLEClientProfile.finish();
		super.onDestroy();
	}

	public void registerWatcher()
	{
		if(bluetoothLEClientProfile != null && watcherRegistered == false)
		{
			bluetoothLEClientProfile.registerWatcher();
			watcherRegistered = true;
		}

	}

	public void setRemoteDevice(BluetoothDevice paramBluetoothDevice)
	{
		bluetoothLEClientProfile.setRemoteDevice(paramBluetoothDevice);
	}

	public void unregisterWatcher()
	{
		if(bluetoothLEClientProfile != null && watcherRegistered == true)
		{
			bluetoothLEClientProfile.unregisterWatcher();
			watcherRegistered = false;
		}
	}

	public BluetoothLEClientChar getCharbyUUID(BluetoothDevice device, String uuid)
	{
		return bluetoothLEClientProfile.getCharbyUUID(device, uuid);
	}

	public ArrayList<BluetoothLEClientChar> getAllChars(BluetoothDevice device)
	{
		return bluetoothLEClientProfile.getAllChars(device);
	}

	// Not working!!! 
	public int writeClientConfigDesc(BluetoothLEClientChar paramBluetoothLEClientChar)
	{
		return bluetoothLEClientProfile.writeClientConfigDesc(paramBluetoothLEClientChar);
	}

	/*
	 * paramInt == 1 means WRITE_REQ. No idea what 0 does.
	 * fill in the data to be written in paramBluetoothLEClientChar.setCharValue(byte[] bytes)
	 */
	public int writeCharValue(BluetoothLEClientChar paramBluetoothLEClientChar, int paramInt)
	{
		return bluetoothLEClientProfile.writeCharValue(paramBluetoothLEClientChar, paramInt);
	}



	@Override
	public IBinder onBind(Intent arg0)
	{
		return binder;
	}

	public class BluetoothLEBinder extends Binder
	{
		public BluetoothLEBinder()
		{
			super();
		}

		public SimpleBlePeripheralService getService()
		{
			return SimpleBlePeripheralService.this;
		}
	}
}
