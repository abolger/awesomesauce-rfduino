package com.rfduino.examples;

import java.util.List;
import java.util.Map;

import net.yougli.shakethemall.ColorPickerDialog;
import net.yougli.shakethemall.ColorPickerDialog.OnColorChangedListener;

import com.rfduino.R;
import com.rfduino.core.BluetoothLEStack;
import com.rfduino.core.RFDuinoSystemCharacteristics;
import com.rfduino.examples.callbacks.LedButtonCallback;
import com.samsung.bluetoothle.BluetoothLEClientChar;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

public class ColorWheelExample extends Activity {
	BluetoothLEStack rfduinoConnection;
	BluetoothDevice chosenBluetoothDevice;
	ColorPickerDialog colorPicker;
	int chosenColor = Color.RED;
	
	/** Tell the Bluetooth manager what to do if user decides not to connect anymore. **/
	OnCancelListener onCancelConnectionAttempt = new OnCancelListener(){
		@Override
		public void onCancel(DialogInterface arg0) {
			ColorWheelExample.this.finish();
		}
	};
	
	
	
	/**
	 * Creates our main layout for this page (Color Wheel widget)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		colorPicker = new ColorPickerDialog(this,onColorSelected, "chosen_led_color" , Color.RED, Color.BLUE); 
		
		colorPicker.show();
		
		//Get the bluetooth device that we put here: this comes from right before we started this activity on the "ListAllExamples.java" screen. 
		chosenBluetoothDevice = (BluetoothDevice) getIntent().getExtras().get("bluetooth_device");
		Log.i(BluetoothLEStack.logTag, "Chosen device is"+ chosenBluetoothDevice);
		
		rfduinoConnection = BluetoothLEStack.connectToBluetoothLEStack(chosenBluetoothDevice, 
				this,
				RFDuinoSystemCharacteristics.RFDUINO_PROFILE_SERVICE_UUID,
				onCancelConnectionAttempt
				);
		

		
		
		
		
		backgroundTask.postDelayed(showColorPicker, 0);
	}
	
	Handler backgroundTask = new Handler();
	private Runnable showColorPicker = new Runnable(){
		public void run(){
		//See if we're done connecting. If we are, show the available colors:
		if (rfduinoConnection.isConnected() && !colorPicker.isShowing()){
			colorPicker.show();		
		}
		backgroundTask.postDelayed(this, 500l);
		
		}
		
	};
	
	private Runnable setToChosen=  new Runnable(){
		public void run(){
			
		//Once we're sure the device offers the ability to write to it, send out the command to write the last color we picked:
		List<String> availableUUIDs = rfduinoConnection.getDiscoveredCharacteristics();
		if (availableUUIDs != null && availableUUIDs.contains(RFDuinoSystemCharacteristics.RFDUINO_PROFILE_SEND_UUID)){
			//Try to write the chosen color into this characteristic to send back:
			byte[] color = new byte[]{(byte) Color.red(chosenColor), (byte) Color.green(chosenColor), (byte) Color.blue(chosenColor)};
			rfduinoConnection.writeCharacteristic(RFDuinoSystemCharacteristics.RFDUINO_PROFILE_SEND_UUID, color);
		}
		}
	};
	
	private OnColorChangedListener onColorSelected = new OnColorChangedListener(){

		@Override
		public void colorChanged(String key, int color) {
			if (key.equals("chosen_led_color")){
				if (chosenColor != color){
					chosenColor = color;
					backgroundTask.postDelayed(setToChosen, 0);
				}
			}
			
		}
		
		
		
	};
	
	@Override 
	public void onDestroy(){
		backgroundTask.removeCallbacks(setToChosen);
		colorPicker.dismiss();
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
