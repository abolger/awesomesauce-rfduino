/** An example of how to use the Samsung BLE bluetooth API for devices that have lower than Android 4.3 available. 
 * Source of sample code: http://e2e.ti.com/support/low_power_rf/f/538/p/240097/879328.aspx
 * 
 * 

package com.ble.activity.discovery;

import java.util.ArrayList;
import java.util.ListIterator;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.ble.bluetoothle.BluetoothAdapterHidden;
import com.ble.bluetoothle.BluetoothDeviceHidden;

public class DiscoveryActivity extends Activity
{
	public static final String addressToConnect = "DiscoveryActivityAddressToConnect";
	
	private TextView scanning, no_le_devices_found;
	private Button button;
	private ListView listView;

	private BluetoothAdapter bluetoothAdapter;

	private class BluetoothInfo
	{
		public BluetoothDevice bluetoothDevice;
		public int rssi;
	}
	private ArrayList<BluetoothInfo> deviceList;
	private DeviceDiscoveryAdapter listAdapter;

	// broadcast receiver to receive the broadcasts while scanning for bt le devices
	private final BroadcastReceiver mReceiver = new BroadcastReceiver()
	{
		public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
		{
			String str = paramAnonymousIntent.getAction();
			if ("android.bluetooth.device.action.FOUND".equals(str))
			{
				BluetoothDevice localBluetoothDevice = (BluetoothDevice)paramAnonymousIntent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
				if(BluetoothDeviceHidden.getDeviceType(localBluetoothDevice) == 1)
				{
					short s = paramAnonymousIntent.getShortExtra("android.bluetooth.device.extra.RSSI", (short)0);
					addDevice(localBluetoothDevice, s);
				}
			}
			if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(str))
			{
				button.setText("Scan");

				// scan done, unregister receiver
				if ((bluetoothAdapter != null) && (bluetoothAdapter.isDiscovering()))
					bluetoothAdapter.cancelDiscovery();
				unregisterReceiver(mReceiver);

				if(deviceList.size() == 0)
				{
					no_le_devices_found.setVisibility(View.VISIBLE);
					scanning.setVisibility(View.INVISIBLE);
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.discovery_activity);

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		deviceList = new ArrayList<BluetoothInfo>();

		scanning = (TextView) findViewById(R.id.discovery_activity_scanning);
		no_le_devices_found = (TextView) findViewById(R.id.discovery_activity_no_le_found);
		button = (Button) findViewById(R.id.discovery_activity_button);
		button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0)
			{
				if(button.getText().equals("Scan"))
				{
					button.setText("Cancel");

					scanForDevices();
				}
				else
				{
					// cancel scanning
					button.setText("Scan");

					if ((bluetoothAdapter != null) && (bluetoothAdapter.isDiscovering()))
						bluetoothAdapter.cancelDiscovery();
					unregisterReceiver(mReceiver);

					if(deviceList.size() == 0)
					{
						no_le_devices_found.setVisibility(View.VISIBLE);
						scanning.setVisibility(View.INVISIBLE);
					}
				}
			}});

		listView = (ListView) findViewById(R.id.discovery_activity_listView);
		listAdapter = new DeviceDiscoveryAdapter(this);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{			
				BluetoothDevice device = deviceList.get(position).bluetoothDevice;	
				
				 Intent returnIntent = new Intent();
				 returnIntent.putExtra(addressToConnect, device.getAddress());
				 setResult(RESULT_OK, returnIntent);     
				 finish();
			}});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_discovery, menu);
		return true;
	}

	protected void onDestroy()
	{
		super.onDestroy();
		if ((bluetoothAdapter != null) && (bluetoothAdapter.isDiscovering()))
			bluetoothAdapter.cancelDiscovery();
	}

	public void onStart()
	{
		super.onStart();

		scanForDevices();
	}

	public void onStop()
	{
		super.onStop();
		if ((bluetoothAdapter != null) && (bluetoothAdapter.isDiscovering()))
			bluetoothAdapter.cancelDiscovery();

		try
		{
			unregisterReceiver(mReceiver);
		}
		catch(IllegalArgumentException e) // try catch in case the receiver is already unregistered
		{}
	}

	public void scanForDevices()
	{
		// clear the list view
		deviceList.clear();
		listAdapter.notifyDataSetChanged();

		// set up the text views
		no_le_devices_found.setVisibility(View.INVISIBLE);
		scanning.setVisibility(View.VISIBLE);

		// register broadcast receiver to receive scan results
		IntentFilter localIntentFilter = new IntentFilter("android.bluetooth.device.action.FOUND");
		localIntentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
		registerReceiver(mReceiver, localIntentFilter);

		// begin scan
		BluetoothAdapterHidden.startLeDiscovery(bluetoothAdapter);
	}

	public void addDevice(BluetoothDevice device, short rssi)
	{
		ListIterator<BluetoothInfo> itr = deviceList.listIterator();
		boolean exists = false;

		while(itr.hasNext())
		{
			BluetoothInfo btInfo = itr.next();
			if(btInfo.bluetoothDevice.getAddress().equals(device.getAddress()))
			{
				exists = true;
				btInfo.rssi = rssi;
				break;
			}
		}

		if(exists == false)
		{
			BluetoothInfo btInfo = new BluetoothInfo();
			btInfo.bluetoothDevice = device;
			btInfo.rssi = rssi;
			deviceList.add(btInfo);
		}

		listAdapter.notifyDataSetChanged();

		// enter this function => definitely have at least 1 item found
		scanning.setVisibility(View.INVISIBLE);
	}


	private class DeviceDiscoveryAdapter extends BaseAdapter
	{
		Context context;

		public DeviceDiscoveryAdapter(Context context)
		{
			this.context = context;
		}

		@Override
		public int getCount()
		{
			return deviceList.size();
		}

		@Override
		public Object getItem(int arg0)
		{
			return deviceList.get(arg0);
		}

		@Override
		public long getItemId(int arg0)
		{
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View rowView = convertView;

			if (rowView == null) 
			{
				LayoutInflater inflater = ((Activity)context).getLayoutInflater();
				rowView = inflater.inflate(R.layout.discovery_activity_list_item, null);
			}

			TextView name = (TextView) rowView.findViewById(R.id.discovery_activity_list_item_name);
			TextView address = (TextView) rowView.findViewById(R.id.discovery_activity_list_item_address);
			TextView rssi = (TextView) rowView.findViewById(R.id.discovery_activity_list_item_rssi);

			BluetoothInfo btInfo = deviceList.get(position);

			name.setText(btInfo.bluetoothDevice.getName());
			address.setText(btInfo.bluetoothDevice.getAddress());
			rssi.setText(""+btInfo.rssi);

			return rowView;
		}

	}
}
**/
