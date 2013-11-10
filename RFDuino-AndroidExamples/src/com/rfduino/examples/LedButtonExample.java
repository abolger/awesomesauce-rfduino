package com.rfduino.examples;

import java.util.List;
import java.util.Map;

import com.rfduino.R;
import com.rfduino.core.BluetoothLEStack;
import com.rfduino.core.RFDuinoSystemCharacteristics;
import com.rfduino.examples.callbacks.LedButtonCallback;
import com.samsung.bluetoothle.BluetoothLEClientChar;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

public class LedButtonExample extends Activity {
	BluetoothLEStack rfduinoConnection;
	BluetoothDevice chosenBluetoothDevice;
	
	/** Tell the Bluetooth manager what to do if user decides not to connect anymore. **/
	OnCancelListener onCancelConnectionAttempt = new OnCancelListener(){
		@Override
		public void onCancel(DialogInterface arg0) {
			LedButtonExample.this.finish();
		}
	};
	
	
	
	/**
	 * Creates our main layout for this page (checkbox), starts searching for bluetooth devices, and shows a list of 
	 *  available bluetooth devices.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_led_blink_example);
		
		//Get the bluetooth device that we put here: this comes from right before we started this activity on the "ListAllExamples.java" screen. 
		chosenBluetoothDevice = (BluetoothDevice) getIntent().getExtras().get("bluetooth_device");
		Log.i(BluetoothLEStack.logTag, "Chosen device is"+ chosenBluetoothDevice);
		
		rfduinoConnection = BluetoothLEStack.connectToBluetoothLEStack(chosenBluetoothDevice, 
				this,
				RFDuinoSystemCharacteristics.RFDUINO_PROFILE_SERVICE_UUID,
				onCancelConnectionAttempt
				);
		
		
	}
	
	@Override 
	public void onDestroy(){
		rfduinoConnection.disconnect();
		rfduinoConnection = null;
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_led_blink_example, menu);
		return true;
	}

	public void onLedCheckboxClicked(View view){
		
		//Now that we're connected, send out the command to actually do the reading:
		List<String> availableUUIDs = rfduinoConnection.getDiscoveredCharacteristics();
		if (availableUUIDs != null && availableUUIDs.contains(RFDuinoSystemCharacteristics.RFDUINO_PROFILE_RECEIVE_UUID)){
		  rfduinoConnection.selectCharacteristicToRead(RFDuinoSystemCharacteristics.RFDUINO_PROFILE_RECEIVE_UUID);
		}
		
		
		Map<String, Object> latestRead = rfduinoConnection.getLatestCharacteristics();
		BluetoothLEClientChar c = ((BluetoothLEClientChar) latestRead.get(RFDuinoSystemCharacteristics.RFDUINO_PROFILE_RECEIVE_UUID));
		if (c == null){
			Toast.makeText(LedButtonExample.this, "Error reading value.", Toast.LENGTH_LONG);
			return;
		}
		Log.i("LedButtonExample", "Received value:"+ BluetoothLEStack.hexStringToIntArray(c.getCharVaule()));
		
		
		
		
		// Is the view now checked?
		    boolean checked = ((CheckBox) view).isChecked();
		    
		    // Check which checkbox was clicked
		    switch(view.getId()) {
		        case R.id.checkBoxLedBlink:
		            if (checked){
		                //Send a message to turn on the LED
		            	
		            	
		            } else {
		               //Send a message to turn off the LED
		            }
		            break;
		        /*TODO: Multiple possible blink rates
		         * case R.id.checkBoxLedBlinkFaster:
		            break;
		       */
		        
		    }
		
	}
	
}
