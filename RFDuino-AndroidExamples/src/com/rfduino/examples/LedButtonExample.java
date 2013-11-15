package com.rfduino.examples;

import java.util.List;

import com.rfduino.R;
import com.rfduino.core.BluetoothLEStack;
import com.rfduino.core.RFDuinoSystemCharacteristics;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

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
		setContentView(R.layout.activity_led_blink_example);
		
		checkBox = (CheckBox) findViewById(R.id.checkBoxLedBlink);
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
		
		//rfduinoConnection.selectCharacteristicToRead(RFDuinoSystemCharacteristics.RFDUINO_PROFILE_RECEIVE_UUID); //Make sure we're listening on the right channel
		
		backgroundTask.postDelayed(seeIfButtonPressed, 0);
	}
	
	Handler backgroundTask = new Handler();
	private Runnable seeIfButtonPressed = new Runnable(){
		public void run(){
		//See if we're done connecting. If we are,try to read from the RFDuino's "Receive" service:
		if (rfduinoConnection.isConnected()){
			List<String> availableUUIDs = rfduinoConnection.getDiscoveredCharacteristics();
			if (availableUUIDs != null && availableUUIDs.contains(RFDuinoSystemCharacteristics.RFDUINO_PROFILE_RECEIVE_UUID)){
				byte[] receivedValue = rfduinoConnection.getLatestCharacteristics().get(RFDuinoSystemCharacteristics.RFDUINO_PROFILE_RECEIVE_UUID);
				if (receivedValue[0] == '0'){
					Log.i("LedButtonExample", "Received 0: Physical Button not pressed.");
					
				}	else if (receivedValue[0] == '1'){
					Log.i("LedButtonExample", "Received 1: Physical Button pressed.");
				}
				
				
			}
		}
			backgroundTask.postDelayed(this, 500l);
		
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
		backgroundTask.removeCallbacks(seeIfButtonPressed);
		backgroundTask.removeCallbacks(turnOn);
		backgroundTask.removeCallbacks(turnOff);
		
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
