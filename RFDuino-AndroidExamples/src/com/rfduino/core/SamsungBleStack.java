package com.rfduino.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream.PutField;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;

import com.samsung.bluetoothle.BluetoothLEClientChar;



import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;

/** 
 * SamsungBleStack.java
 * 
 * This class stores all the code that needed to be implemented to use the hidden Bluetooth Low Energy API that is available starting
 * in some Samsung Android devices as early as Android 4.1. This class is completely unnecessary if your Android platform is running Android 4.3 or later.
 * This class draws heavily from example code available as a published sample application from TI's Help and Support website here:
 *  	http://e2e.ti.com/support/low_power_rf/f/538/p/240097/879328.aspx
 * and on extracted source code from Samsung's BLE SDK.  It has been tested with the Samsung Galaxy S3 running Android 4.1.2.
 * 
 * @author adrienne
 *
 * This library is released under the LGPL. A copy of the license should have been distributed with this library/source code,
 *  if not, you can read it here: (https://github.com/abolger/awesomesauce-rfduino/blob/master/LICENSE)
*/
 public class SamsungBleStack extends BluetoothLEStack{
	private SimpleBlePeripheralService bluetoothLEService;
	private ServiceConnection bluetoothBinding; 
	private HashMap<String, Runnable> desiredCallbacks= new HashMap<String, Runnable>();
	
	
	Integer bleSemaphore = 0; //Only 1 cmd/response pair can be going at once on the Samsung stack, so enforce it with a lock.
	Integer latestRSSIValue = null;
	
	//For Android 4.1, 4.2 (which doesn't have BluetoothAdapter.LeScanCallback class:  
	// broadcast receiver to receive the broadcasts while scanning for bt le devices
	public final static BroadcastReceiver onBluetoothFoundReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
		{
			
			
			String str = paramAnonymousIntent.getAction();
			
			
			if (BluetoothDevice.ACTION_FOUND.equals(str))
			{
				BluetoothDevice localBluetoothDevice = (BluetoothDevice) paramAnonymousIntent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				//Get the hidden method for bluetooth Low Energy device categorization
				int deviceClass = 0;
				deviceClass = (Integer) bluetoothDeviceHiddenApi("getDeviceType", localBluetoothDevice);
				if(deviceClass == 1)
				{
					short s = paramAnonymousIntent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short)0);
					for (BluetoothDevice d: BluetoothLEStack.discoveredDevices){
						if (d.getAddress().equals(localBluetoothDevice.getAddress())){
							//We found the same device again- don't re-add it to the list. 
							return; 
						}
					}
					BluetoothLEStack.discoveredDevices.add(localBluetoothDevice);
				}
			}
			
			if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(str))
			{
				if (BluetoothAdapter.getDefaultAdapter().isDiscovering()){
					BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
				}
				
			}
		}
	};
	

	
	public final BroadcastReceiver onBluetoothConnectedReceiver = new BroadcastReceiver(){
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String str = intent.getAction();
			if (str.equals(BluetoothLEStack.DEVICE_LED_CONNECTED)) {
				BluetoothDevice localDevice = (BluetoothDevice) intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				connectedDevice = localDevice; 
				Log.i(logTag, "Received onLEDeviceConnectedBroadcast");
				successfulConnection = true;
				
				//If the GUI window is showing for this step, we can hide it now:
				if (bluetoothConnectionWaitWindow != null && bluetoothConnectionWaitWindow.isShowing()){
					bluetoothConnectionWaitWindow.dismiss();
				}
				
				discoverAvailableCharacteristics();
				
				
			} else  if (str.equals(BluetoothLEStack.DEVICE_DISCONNECTED) || str.equals(BluetoothLEStack.DEVICE_LINK_LOSS)) {
				Log.i(logTag, "Unexpected disconnect notice received");
				//Notify user of issue:
				//If the GUI window is showing for this step, we can hide it now:
				/*if (bluetoothConnectionWaitWindow != null && bluetoothConnectionWaitWindow.isShowing()){
					bluetoothConnectionWaitWindow.setMessage("Could not connect to Bluetooth device.");
					disconnect();
					bluetoothConnectionWaitWindow.cancel();
					
				} else {
					disconnect();
				}*/
				
				
				
			} else {
				Log.w(logTag, "onBluetoothConnectedReceiver received intent action "+str+" but does not handle it.");
			}
		}

		
	};
	
	/**
	 * Provide an implementation of the BluetoothLE stack using Samsung's com.samsung.ble SDK for Android devices earlier than 4.3
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * 
	 */
	public static void startLeScan(Context hostActivity) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		// register broadcast receiver to receive scan results
		IntentFilter localIntentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		localIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		
		hostActivity.registerReceiver(onBluetoothFoundReceiver, localIntentFilter);
		scanning = true;
		// begin scan using a hidden method available in the Samsung SDK. 
		samsungBluetoothAdapterClass.getDeclaredMethod("startLeDiscovery").invoke(BluetoothAdapter.getDefaultAdapter());
		
		
	}

	/** Stop looking for Bluetooth Low Energy devices. **/
	public static void stopLeScan(Activity hostActivity) {
		if (scanning){
		//Stop responding to found items:
		hostActivity.unregisterReceiver(onBluetoothFoundReceiver);
		if (BluetoothAdapter.getDefaultAdapter().isDiscovering()){
			BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
		}
		scanning = false;
		}
	}
	
	/** Connect to a Low Energy Bluetooth device with a certain service profile UUID.
	 *  
	 * @param device  Bluetooth Low Energy device to connect to. 
	 * @param hostActivity the Android activity that will be using the information from this connection. 
	 * @param deviceServiceUUID UUID that identifies the serivce on your peripheral you are trying to connect to. UUIDs are either 16 or 128 bit and are specific to the peripheral 
	 */
	public SamsungBleStack(BluetoothDevice device, Activity hostActivity){
		//Create a binding between our application and the Bluetooth LE service we're about to start. 
		bluetoothBinding = new ServiceConnection()
		{
			public void onServiceConnected(ComponentName className, IBinder service) 
			{
				bluetoothLEService = ((SimpleBlePeripheralService.ServiceBinder) service).getService();
				if (bluetoothLEService == null){
					disconnect(); //We failed, stop trying to connect
					return;
				}
				
				
				//Remove existing connections before continuing:
				Iterator<BluetoothDevice> iterator = BluetoothAdapter.getDefaultAdapter().getBondedDevices()
				.iterator();
				while (iterator.hasNext()) {
					BluetoothDevice device = iterator.next();
					if ((Boolean) bluetoothDeviceHiddenApi("isLEDeviceConnected",device)){
						if (device.getBondState() != BluetoothDevice.BOND_BONDED){
							bluetoothDeviceHiddenApi("createBond", device);
						}
					}
					bluetoothLEService.setRemoteDevice(device);
					bluetoothLEService.disconnectLEDevice(device);
					if (device.getBondState() == BluetoothDevice.BOND_BONDED){
						bluetoothDeviceHiddenApi("removeBond", device);
					}
				}
				
				Log.v(logTag, "Trying to connect to Bluetooth device"+ connectedDevice.getName() +" " +connectedDevice.getAddress());
				//Use Samsung API to connect to the bluetooth Low Energy device (whether we are successful or not is handled
				//asynchroously in onBluetoothConnectedReceiver
				bluetoothLEService.setRemoteDevice(connectedDevice);
				asyncRequestor.postDelayed(connectionRequestor, 3000L); //Now that we're connected to the ongoing Android service, try to have the service connect to the bluetooth device 
				
			}

			public void onServiceDisconnected(ComponentName className) 
			{
				bluetoothLEService.disconnectLEDevice(connectedDevice);
				Log.v(logTag, "Disconnected from Bluetooth device"+ connectedDevice.getName() +" " +connectedDevice.getAddress());
				bluetoothLEService.stopSelf();
				connectedDevice = null;
				bluetoothLEService = null;
				latestRSSIValue = null;
				
			}
		};

		connect(device, hostActivity);
				
	}
	
	
	
	/** Disconnect from the Bluetooth Device. **/
	public void disconnect(){
		if (!disconnectCalled){
			successfulConnection = false;
			hostAndroidActivity.unbindService(bluetoothBinding);
			hostAndroidActivity.unregisterReceiver(readBroadcastReceiver);
			hostAndroidActivity.unregisterReceiver(onBluetoothConnectedReceiver);
			disconnectCalled = true; 
			
		}
			
	}
	

	@Override
	protected void connect(BluetoothDevice device, Activity hostActivity) {
		disconnectCalled = false; //Starting a new connection now. 
		hostAndroidActivity = hostActivity;
		connectedDevice = device;
		
		//Use Android's "Service" API to initialize and keep an ongoing connection to the bluetooth device:
		//See http://developer.android.com/reference/android/app/Service.html
		//Note that for this to work, your service must be declared with the correct package name in the Android Manifest XML file.
		Intent intent = new Intent(hostAndroidActivity, SimpleBlePeripheralService.class);
		hostAndroidActivity.bindService(intent, bluetoothBinding, Context.BIND_AUTO_CREATE);
		
		//Tell Android that when the service is done connecting over bluetooth, we want to handle it with this asynchronous receiver:
		IntentFilter connectionFilter = new IntentFilter();
		connectionFilter.addAction(BluetoothLEStack.DEVICE_LED_CONNECTED);
		connectionFilter.addAction(BluetoothLEStack.DEVICE_LINK_LOSS);
		connectionFilter.addAction(BluetoothLEStack.DEVICE_DISCONNECTED);
		hostAndroidActivity.registerReceiver(onBluetoothConnectedReceiver, connectionFilter);
		
		IntentFilter readFilter = new IntentFilter(BluetoothLEStack.CHARACTERISTICS_REFRESH);
		readFilter.addAction(BluetoothLEStack.DEVICE_RSSI_VAL);
		hostAndroidActivity.registerReceiver(readBroadcastReceiver, readFilter);

	}
	
	public Handler asyncRequestor = new Handler();
	
	private Runnable connectionRequestor = new Runnable(){
			public void run() {
				if (!disconnectCalled){
				try {
				
				int p = bluetoothLEService.getProfileState();
				if (connectedDevice.getBondState() == BluetoothDevice.BOND_NONE) {
						bluetoothDeviceHiddenApi("createBond", connectedDevice);
				} 
				else if (connectedDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
						if (p == RFDuinoBLEProfile.GATT_STATE_DISCONNECTED) {
							bluetoothLEService.connectLEDevice(connectedDevice);
						}
						if (p  == RFDuinoBLEProfile.GATT_STATE_CONNECTING) {
							Log.d(logTag, "Still GATT_CONNECTING");
						}
						if ((Boolean) bluetoothDeviceHiddenApi("isLEDeviceConnected", connectedDevice)) {
							asyncRequestor.removeCallbacks(this);
							return;
						}
					}
				
				asyncRequestor.postDelayed(this, 3000L);
				} catch (Exception e){
					Log.e(logTag, "Samsung BLE Stack failed to call hidden method.");
					Log.e(logTag, ""+e);
				}
				}}
				
	};
	
	
	private Runnable continuousRSSIrequest = new Runnable() {
		public void run() {
			if (connectedDevice != null && disconnectCalled == false){
				//Async call- results when returned from BT will be handled by readBroadcastReceiver
				if (bleSemaphore == 0){
					bleSemaphore += 1;
					bluetoothLEService.getRssiValue(connectedDevice); 
				}
			} else {
				stopReadingRSSI(); // Stop our own thread if we disconnect
			}
			asyncRequestor.postDelayed(continuousRSSIrequest, 3000L);
		}
	};
	
	

	public void startReadingRSSI() {
		continuousRSSIrequest.run();
	}

	public void stopReadingRSSI() {
		asyncRequestor.removeCallbacks(continuousRSSIrequest);
	}

	public Integer getLatestRSSI(){
		return latestRSSIValue;
	}
	
	/**Select which discovered characteristic you want to read the latest value from. Updated values are accessible from 
	 * the function getLatestCharacteristics() **/
	@Override
	public void selectCharacteristicToRead(String uuid){
		presentUUIDtoRead = uuid;
		if (successfulConnection){ //If we're already connected, discover characteristics now. Otherwise we'll start reading when connection is completed (see connect method)
			//Async call- results when returned from BT will be handled by readBroadcastReceiver
			if (bleSemaphore == 0){
				bleSemaphore += 1;
				bluetoothLEService.discoverCharacteristics(connectedDevice, presentUUIDtoRead);
				
			}}
	}

	@Override
	public boolean writeCharacteristic(String uuid, byte[] value) {
		presentUUIDtoWrite = uuid;
		BluetoothLEClientChar c = bluetoothLEService.getCharbyUUID(connectedDevice, uuid);
		if(c == null)
		{
			return false;
		} else {
			c.setCharValue(value);
			bluetoothLEService.writeCharValue(c, 1);
			return true;
		}
	}
	
	
	private final BroadcastReceiver readBroadcastReceiver = new BroadcastReceiver() {
	
		@Override
		public void onReceive(Context arg0, Intent intent) {
			String str = intent.getAction();
			if (str.equals(BluetoothLEStack.DEVICE_RSSI_VAL)) {
				 bleSemaphore -= 1;
					
				BluetoothDevice localDevice = (BluetoothDevice) intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (connectedDevice != null  && localDevice != null
						&& connectedDevice.equals(localDevice)) {
					String rssiStr = intent
							.getStringExtra(BluetoothDevice.EXTRA_RSSI);
					latestRSSIValue = Integer.valueOf(rssiStr).intValue();
					
				}
			} else if (str.equals(BluetoothLEStack.CHARACTERISTICS_REFRESH)) {
				bleSemaphore -= 1;
				BluetoothDevice localDevice = (BluetoothDevice) intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (connectedDevice != null && localDevice != null
						&& connectedDevice.equals(localDevice)) {
					
					//Update our available services by UUID:
					ArrayList<String> uuids = new ArrayList<String>();
					for (BluetoothLEClientChar c : bluetoothLEService.getAllChars(connectedDevice)){
						uuids.add(c.getCharUUID());
						
					}
					allowedUUIDs =  uuids;	
					for (BluetoothLEClientChar c : bluetoothLEService.getAllChars(connectedDevice)){
							uuidsTolatestValues.put(c.getCharUUID(), c.getCharVaule());
					}
					
					
					
				}	
				
			} 
		}
	};

	
	

	@Override
	public boolean discoverAvailableCharacteristics() {
		if (successfulConnection && bleSemaphore == 0){
			bleSemaphore +=1;
			bluetoothLEService.discoverCharacteristics(connectedDevice);
			return true;
		} else {
			return false;
		}
	}

	

	@Override
	public Integer getLatestRSSIValue() {
		return latestRSSIValue;
	}
	

	private static Class<?> samsungBluetoothAdapterClass;
	private static Class<?> samsungBluetoothDeviceClass;
	
	
	
	/** Function to use our version of BluetoothAdapter and BluetoothDevice if the native Android OS doesn't have 4.3 yet. 
	 * @throws IOException 
	 * @throws ClassNotFoundException **/
	public static void loadSamsungLibraries(Context hostActivityContext) throws IOException, ClassNotFoundException{
		File internalStoragePath = hostActivityContext.getFileStreamPath("com.samsung.ble.sdk-1.0.jar");
		if (!internalStoragePath.exists()){
			
		
		  //We'll copy the SDK to disk so we can open the JAR file:
		  // it has to be first copied from asset resource to a storage location.
		  InputStream jar = hostActivityContext.getAssets().open("com.samsung.ble.sdk-1.0.jar", Context.MODE_PRIVATE);
		  FileOutputStream outputStream = hostActivityContext.openFileOutput("com.samsung.ble.sdk-1.0.jar", Context.MODE_PRIVATE);
		     
		  int size = 0;
		    // Read the entire resource into a local byte buffer.
		    byte[] buffer = new byte[1024];
		    while((size=jar.read(buffer,0,1024))>=0){
		      outputStream.write(buffer,0,size);
		    }
		    jar.close();
		   
		}
		    
		  Log.i(logTag, internalStoragePath.getAbsolutePath()+" exists? "+ internalStoragePath.exists());
		  
          URL[] urls = { new URL("jar:file:" + internalStoragePath.getAbsolutePath()+"!/") };
          URLClassLoader cl  = URLClassLoader.newInstance(urls);
          
          samsungBluetoothAdapterClass = cl.loadClass("android.bluetooth.BluetoothAdapter");
          samsungBluetoothDeviceClass = cl.loadClass("android.bluetooth.BluetoothDevice");
  	
	}
	
	private static Object bluetoothDeviceHiddenApi(String hiddenMethodName, BluetoothDevice bluetoothDevice){
		try {
			return samsungBluetoothDeviceClass.getDeclaredMethod(hiddenMethodName).invoke(bluetoothDevice);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setOnCharacteristicChangedWatcher(String uuid, Runnable callback) {
		desiredCallbacks.put(uuid, callback);
		bluetoothLEService.registerCallbackOnCharacteristicChanged(uuid, callback);
	}

}

