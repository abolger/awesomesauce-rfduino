package com.rfduino.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;

import com.samsung.bluetoothle.BluetoothLEClientChar;
import com.samsung.bluetoothle.BluetoothLEClientService;


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

/** This class stores all the code that needed to be implemented to use the hidden Bluetooth Low Energy API that is available starting
 * in some devices as early as Android 4.1, but isn't needed at all for Android 4.3.
 * This class draws heavily from example code available as a published sample application from TI's Help and Support website here:
 *  	http://e2e.ti.com/support/low_power_rf/f/538/p/240097/879328.aspx
 * and on extracted source code from Samsung's BLE SDK.  It has been tested with the Samsung Galaxy S3 running Android 4.1.2.
 * 
 * @author adrienne
 *
 */



public class SamsungBleStack extends BluetoothLEStack{
	private SimpleBlePeripheralService bluetoothLEService;
	private ServiceConnection bluetoothBinding; 
	
	boolean connecting = false;
	boolean successfulConnection = false;
	boolean disconnectCalled = false;
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
			if (str.equals(RFDuinoBLEProfile.CHARACTERISITICS_REFRESH)) {
				bleSemaphore -= 1;
				BluetoothDevice localDevice = (BluetoothDevice) intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (connectedDevice != null && localDevice != null
						&& connectedDevice.equals(localDevice)) {
					for (ParcelUuid pcb : connectedDevice.getUuids()) {
						String logStr = "Found Service with UUID: "
								+ pcb.toString();
						Log.d(logTag, logStr);
						//TODO: handle services in RFDuinoSystemCharacteristics so it doesn't matter which program you run?
					}
				}
			} else if (str.equals(RFDuinoBLEProfile.DEVICE_LED_CONNECTED)) {
				BluetoothDevice localDevice = (BluetoothDevice) intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				connectedDevice = localDevice; 
				successfulConnection = true;
				Log.i(logTag, "Received onLEDeviceConnectedBroadcast");
				ArrayList<String> uuids = new ArrayList<String>();
				for (ParcelUuid p: connectedDevice.getUuids()){
					uuids.add(p.getUuid().toString());
					String logStr = "Found Service with UUID: "
						+ p.getUuid().toString();
					Log.d(logTag, logStr);
				}
				allowedUUIDs =  uuids;
				
				//startReadingRSSI();
				startDiscoveringCharacteristics();
				
				//bluetoothLEService.initializeProfileByUUID(connectedDevice, RFDuinoSystemCharacteristics.RFDUINO_PROFILE_SERVICE_UUID);
				
			} else if (str.equals(RFDuinoBLEProfile.DEVICE_DISCONNECTED) || str.equals(RFDuinoBLEProfile.DEVICE_LINK_LOSS)) {
				// re-connect if there is any sudden disconnection
				bluetoothLEService.connectLEDevice(connectedDevice);
				Log.i(logTag, "Unexpected disconnect notice received");
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
		hostAndroidActivity = hostActivity;
		connectedDevice = device;
		
		
		//Create a binding between our application and the Bluetooth LE service we're about to start. 
		bluetoothBinding = new ServiceConnection()
		{
			public void onServiceConnected(ComponentName className, IBinder service) 
			{
				
				bluetoothLEService = ((SimpleBlePeripheralService.ServiceBinder) service).getService();
				
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

		//Use Android's "Service" API to initialize and keep an ongoing connection to the bluetooth device:
		//See http://developer.android.com/reference/android/app/Service.html
		//Note that for this to work, your service must be declared with the correct package name in the Android Manifest XML file.
		Intent intent = new Intent(hostAndroidActivity, SimpleBlePeripheralService.class);
		hostAndroidActivity.bindService(intent, bluetoothBinding, Context.BIND_AUTO_CREATE);
		
		//Tell Android that when the service is done connecting over bluetooth, we want to handle it with this asynchronous receiver:
		hostAndroidActivity.registerReceiver(onBluetoothConnectedReceiver, new IntentFilter(RFDuinoBLEProfile.DEVICE_LED_CONNECTED));
		hostAndroidActivity.registerReceiver(onBluetoothConnectedReceiver, new IntentFilter(RFDuinoBLEProfile.DEVICE_DISCONNECTED));
		hostAndroidActivity.registerReceiver(onBluetoothConnectedReceiver, new IntentFilter(RFDuinoBLEProfile.DEVICE_LINK_LOSS));
		hostAndroidActivity.registerReceiver(onBluetoothConnectedReceiver,  new IntentFilter(RFDuinoBLEProfile.CHARACTERISITICS_REFRESH));
		
		hostAndroidActivity.registerReceiver(readBroadcastReceiver,  new IntentFilter(RFDuinoBLEProfile.DEVICE_RSSI_VAL));
		
	}
	
	
	
	/** Disconnect from the Bluetooth Device. **/
	public void disconnect(){
		if (!disconnectCalled){
			
			hostAndroidActivity.unbindService(bluetoothBinding);
			hostAndroidActivity.unregisterReceiver(readBroadcastReceiver);
			hostAndroidActivity.unregisterReceiver(onBluetoothConnectedReceiver);
			disconnectCalled = true; 
		}
			
	}
	
	public Handler asyncRequestor = new Handler();
	
	private Runnable connectionRequestor = new Runnable(){
			public void run() {
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
					Log.e(logTag, e.getMessage());
				}
				}
				
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
	
	private Runnable continuousCharRequest = new Runnable() {
		public void run() {
			if (connectedDevice != null && disconnectCalled == false){
				
				if (bleSemaphore == 0){
					bleSemaphore += 1;
					//Async call- results when returned from BT will be handled by readBroadcastReceiver
					bluetoothLEService.discoverCharacteristics(connectedDevice);
				}
				asyncRequestor.postDelayed(continuousCharRequest, 3000L); //Regardless of whether we wait or repost, try again in 3 seconds.
			} else {
				stopDiscoveringCharacteristics(); // Stop our own thread if we disconnect
			}
			
		}
	};

	void startReadingRSSI() {
		continuousRSSIrequest.run();
	}

	void stopReadingRSSI() {
		asyncRequestor.removeCallbacks(continuousRSSIrequest);
	}

	private void startDiscoveringCharacteristics() {
		continuousCharRequest.run();
		
	}
	private void stopDiscoveringCharacteristics() {
		asyncRequestor.removeCallbacks(continuousCharRequest);
		
	}
	
	
	

	
	
	private final BroadcastReceiver readBroadcastReceiver = new BroadcastReceiver() {
	
		@Override
		public void onReceive(Context arg0, Intent intent) {
			String str = intent.getAction();
			
			 if (str.equals(RFDuinoBLEProfile.DEVICE_RSSI_VAL)) {
				 bleSemaphore -= 1;
					
				BluetoothDevice localDevice = (BluetoothDevice) intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (connectedDevice != null  && localDevice != null
						&& connectedDevice.equals(localDevice)) {
					String rssiStr = intent
							.getStringExtra(BluetoothDevice.EXTRA_RSSI);
					latestRSSIValue = Integer.valueOf(rssiStr).intValue();
					
				}
			} else {
				Log.i(logTag, "OnReceive did not handle event:"+ str);
			}
			/* if(str.equals(RFDuinoBLEProfile.ACC_VALUE_REFRESH)){
				MainActivity.this.xVal = intent.getFloatExtra("X", 0.0f);
				MainActivity.this.yVal = intent.getFloatExtra("Y", 0.0f);
				MainActivity.this.zVal = intent.getFloatExtra("Z", 0.0f);
				
				runOnUiThread(new Runnable(){
					public void run(){
						MainActivity.this.setACCText();
					}
				});
			}*/
		}

		
	};

	@Override
	public void readBLECharacteristic(String UUID) {
		// TODO Auto-generated method stub
		
	}

	private static Class samsungBluetoothAdapterClass;
	private static Class samsungBluetoothDeviceClass;
	
	
	
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
	
}

