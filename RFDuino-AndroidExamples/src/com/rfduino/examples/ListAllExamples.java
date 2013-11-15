package com.rfduino.examples;

import com.rfduino.R;
import com.rfduino.core.BluetoothLEStack;
import android.view.View;
import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/** 
 * ListAllExamples.java
 * 
 * This Activity:
 * 1.  loads a list of all possible example Activities for use with an RFDuino board and displays them in a clickable format
 * 2. Allows the user to select an Activity via a listElementListener.
 * 3. Performs a Bluetooth Low Energy device scan and displays available Bluetooth devices that can be used with the Activity
 * 4. Stores the selected BluetoothDevice as an "Extra" to pass to the new Activity's "onCreate" method when it is initialized.
 *   
 * @author adrienne
 * 
 * This library is released under the LGPL. A copy of the license should have been distributed with this library/source code,
 *  if not, you can read it here: (https://github.com/abolger/awesomesauce-rfduino/blob/master/LICENSE)
*/

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
