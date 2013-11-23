package com.rfduino.examples;

import java.util.ArrayList;
import java.util.List;

import com.rfduino.R;
import com.rfduino.core.BluetoothLEStack;
import com.rfduino.core.RFDuinoSystemCharacteristics;
import com.samsung.bluetoothle.BluetoothLEClientChar;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

/** 
 * LedButtonExample.java
 * 
 * This example Activity is designed to be run with a RFDuino board that has had the "RFDuinoBLE>LedButton" sketch loaded onto
 * it.  See https://github.com/abolger/awesomesauce-rfduino/wiki/Getting%20Started for more details. 
 * 
 * @author adrienne
 * 
 * This library is released under the LGPL. A copy of the license should have been distributed with this library/source code,
 *  if not, you can read it here: (https://github.com/abolger/awesomesauce-rfduino/blob/master/LICENSE)
*/
public class LedButtonExample extends Activity {
	BluetoothLEStack rfduinoConnection;
	BluetoothDevice chosenBluetoothDevice;
	CheckBox checkBox;
	TextView buttonPressEventDisplay;
	
	/** Tell the Bluetooth manager what to do if user decides not to connect anymore. **/
	OnCancelListener onCancelConnectionAttempt = new OnCancelListener(){
		@Override
		public void onCancel(DialogInterface arg0) {
			LedButtonExample.this.finish();
		}
	};
	
	
	
	/**
	 * Creates our main layout for this page (A checkbox that should stay synced with the RFDuino)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_led_toggle_example);
		
		buttonPressEventDisplay = (TextView) findViewById(R.id.buttonPressNotification);
		
		checkBox = (CheckBox) findViewById(R.id.checkBoxLedToggle);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked){
					backgroundTask.post(turnOn);
				}else {
					backgroundTask.post(turnOff);
				}
				
			}
		});
		
		
		//Get the bluetooth device that we put here: this comes from right before we started this activity on the "ListAllExamples.java" screen. 
		chosenBluetoothDevice = (BluetoothDevice) getIntent().getExtras().get("bluetooth_device");
		Log.i(BluetoothLEStack.logTag, "Chosen device is"+ chosenBluetoothDevice);
		
		rfduinoConnection = BluetoothLEStack.connectToBluetoothLEStack(chosenBluetoothDevice, 
				this,
				RFDuinoSystemCharacteristics.RFDUINO_PROFILE_SERVICE_UUID,
				onCancelConnectionAttempt
				);
		
		backgroundTask.postDelayed(runOnceConnected, 1000l);
		
	}
	
	private Runnable runOnceConnected = new Runnable(){
		public void run(){
		if (rfduinoConnection.isConnected()){
			List<String> availableUUIDs = rfduinoConnection.getDiscoveredCharacteristics();
			if (availableUUIDs != null && availableUUIDs.contains(RFDuinoSystemCharacteristics.RFDUINO_PROFILE_RECEIVE_UUID) ){
				//Connected and have the service handle. Register a callback to use the service:
				rfduinoConnection.setOnCharacteristicChangedWatcher(RFDuinoSystemCharacteristics.RFDUINO_PROFILE_RECEIVE_UUID, buttonPressedCallback);
			} else {
				rfduinoConnection.discoverAvailableCharacteristics();
				backgroundTask.postDelayed(this, 1000l);
			}
		}else {
			backgroundTask.postDelayed(this, 1000l);
		}
		}
	};
	
	
	Handler backgroundTask = new Handler();
	
	//A slightly ridiculous nested callback that can be passed to our Bluetooth manager:
	private Runnable buttonPressedCallback = new Runnable(){
		public void run(){
			runOnUiThread(new Runnable(){
				public void run(){
					Log.i("LedButtonExample", "Button was pressed.");
					String newText = buttonPressEventDisplay.getText() + "*";
					buttonPressEventDisplay.setText(newText);
				}
				
			});
			
		}
		
	};
	
	

	
	
	private Runnable turnOff =  new Runnable(){
		public void run(){
			
		//Once we're sure the device offers the ability to write to it, send out the command to write ZERO to the RFDuino:
		List<String> availableUUIDs = rfduinoConnection.getDiscoveredCharacteristics();
		if (availableUUIDs != null && availableUUIDs.contains(RFDuinoSystemCharacteristics.RFDUINO_PROFILE_SEND_UUID)){
			//Try to write the chosen color into this characteristic to send back:
			byte[] sendZero = new byte[]{0};
			rfduinoConnection.writeCharacteristic(RFDuinoSystemCharacteristics.RFDUINO_PROFILE_SEND_UUID, sendZero);
		}
		}
	};
	
	private Runnable turnOn =  new Runnable(){
		public void run(){
			
		//Once we're sure the device offers the ability to write to it, send out the command to write ONE to the RFDuino:
		List<String> availableUUIDs = rfduinoConnection.getDiscoveredCharacteristics();
		if (availableUUIDs != null && availableUUIDs.contains(RFDuinoSystemCharacteristics.RFDUINO_PROFILE_SEND_UUID)){
			//Try to write the chosen color into this characteristic to send back:
			byte[] sendOne = new byte[]{1};
			rfduinoConnection.writeCharacteristic(RFDuinoSystemCharacteristics.RFDUINO_PROFILE_SEND_UUID, sendOne);
		}
		}
	};
	
	
	
	
	@Override 
	public void onDestroy(){
		backgroundTask.removeCallbacks(turnOn);
		backgroundTask.removeCallbacks(turnOff);
		backgroundTask.removeCallbacks(runOnceConnected);
		backgroundTask.removeCallbacks(buttonPressedCallback);
		
		rfduinoConnection.disconnect();
		super.onDestroy();
	}

	@Override 
	public void onResume(){
		if (rfduinoConnection == null){
			rfduinoConnection = BluetoothLEStack.connectToBluetoothLEStack(chosenBluetoothDevice,
					this,
					RFDuinoSystemCharacteristics.RFDUINO_PROFILE_RECEIVE_UUID,
					onCancelConnectionAttempt
				);
		}
		super.onResume();
	}
	

	
}
