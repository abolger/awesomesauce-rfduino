package com.rfduino.examples;

import com.rfduino.R;
import com.rfduino.core.BluetoothLEStack;
import com.rfduino.examples.callbacks.LedButtonCallback;

import android.util.Log;
import android.view.View;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListAllExamples extends ListActivity {

	Intent chosenExample;
	BluetoothDevice chosenBluetoothDevice;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		
		//Get a list of all the Example activities that we want to be able to launch. 
		setListAdapter(new ArrayAdapter<String>(this,
	            android.R.layout.simple_list_item_1,
	            getResources().getStringArray(R.array.example_activities)
				));
		
		setupListElementListener();
		
	}

	
	private void setupListElementListener() {
        ListView list = (ListView) getListView();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,int position, long id) {
					//Use the XML list in strings.xml to get the Java class name of the example we want to start and store that value for once we've chosen a radio.
					try {
						BluetoothLEStack.beginSearchingForBluetoothDevices(ListAllExamples.this);
						chosenExample = new Intent(viewClicked.getContext(), Class.forName("com.rfduino.examples." + getListView().getItemAtPosition(position).toString()));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					
					//Runs code that pops up a second list on the UI screen- this one shows all possible bluetooth devices that we can use in our examples. 
					runOnUiThread(new Runnable(){
						@Override
						public void run() {
							BluetoothLEStack.showFoundBluetoothDevices(ListAllExamples.this, rfduinoChosenListener);
						}
						
					});
				}

			
        }
        
        );
    }
	
	@Override 
	public void onDestroy(){
		BluetoothLEStack.stopSearchingForBluetoothDevices(this);
		super.onDestroy();
	}
	
	
	/** 
	 * GUI OnClickListener: after a list of possible RFDuinos is displayed, this handler listens for a click in the list and connects
	 * to the corresponding radio. 
	 */
	DialogInterface.OnClickListener rfduinoChosenListener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			BluetoothLEStack.stopSearchingForBluetoothDevices(ListAllExamples.this);
			
			chosenBluetoothDevice = BluetoothLEStack.discoveredDevices.get(which);
			chosenExample.putExtra("bluetooth_device", chosenBluetoothDevice);
			
			startActivity(chosenExample);
			
			
		}
	};
	
}
