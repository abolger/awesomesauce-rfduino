package com.rfduino.examples;

import com.rfduino.core.BluetoothLEStack;
import com.rfduino.examples.callbacks.LedButtonCallback;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

public class LedButtonExample extends Activity {
	
	
	
	
	/**
	 * Creates our main layout for this page (checkbox), starts searching for bluetooth devices, and shows a list of 
	 *  available bluetooth devices.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_led_blink_example);
		
		//Get the bluetooth device that we put here: this comes from right before we started this activity on the "ListAllExamples.java" screen. 
		BluetoothDevice chosenBluetoothDevice = (BluetoothDevice) getIntent().getExtras().get("bluetooth_device");
	//	new RFDuinoBluetooth(chosenBluetoothDevice, new LedButtonCallback() , LedButtonExample.this);
		Log.i("LEDBlinkExample", "Chose to connect to bluetooth device:" + chosenBluetoothDevice.getName()+" "+chosenBluetoothDevice.getAddress() );
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_led_blink_example, menu);
		return true;
	}

	public void onLedCheckboxClicked(View view){
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
