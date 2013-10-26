/** An example of how to use the Samsung BLE bluetooth API for devices that have lower than Android 4.3 available. 
 * Source of sample code: http://e2e.ti.com/support/low_power_rf/f/538/p/240097/879328.aspx
 * 
 * 

package com.ble.activity.main;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ble.R;
import com.ble.activity.discovery.DiscoveryActivity;
import com.ble.bluetoothle.ExtendedBluetoothLEClientProfile;
import com.ble.bluetoothle.SimpleBlePeripheralService;
import com.samsung.bluetoothle.BluetoothLEClientChar;

public class PairingFragment extends Fragment
{
	private static final int enableBluetoothActivityResult = 1;
	private static final int discoveryActivityResult = 2;
	private static final String TAG = "PairingFragment";

	private Button discoverButton;
	private TextView status;

	private BluetoothDevice device;

	private SimpleBlePeripheralService bluetoothLEService;
	private boolean isServiceBound = false;

	private UpdaterAsyncTask task;

	private boolean broadcastReceiverRegistered = false;
	private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent)
		{
			String str = intent.getAction();
			if(str.equals(ExtendedBluetoothLEClientProfile.onLEDeviceConnectedBroadcast))
			{
				Log.i(TAG, "Received onLEDeviceConnectedBroadcast");

				bluetoothLEService.discoverCharacteristics(device);
			}

			if(str.equals(ExtendedBluetoothLEClientProfile.onDiscoverCharacteristicsBroadcast))
			{
				Log.i(TAG, "Received onDiscoverCharacteristicsBroadcast");

				ArrayList<BluetoothLEClientChar> chars = bluetoothLEService.getAllChars(device);

				Log.i(TAG, "Chars are: ");
				for(BluetoothLEClientChar c : chars)
					Log.i(TAG, c.getCharUUID());
				
				// unregister this broadcast receiver. No longer needed.
				getActivity().unregisterReceiver(broadcastReceiver);
				broadcastReceiverRegistered = false;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		if (container == null) 
			return null;

		LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_pairing, container, false);

		status = (TextView) layout.findViewById(R.id.fragment_pairing_status);

		discoverButton = (Button) layout.findViewById(R.id.fragment_pairing_discoverdevices);
		discoverButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(getActivity(), DiscoveryActivity.class);
				startActivityForResult(intent, 2);
			}});

		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// check whether bluetooth is on
		// also, request to turn it on if it is not
		if(!bluetoothAdapter.isEnabled())
		{
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, enableBluetoothActivityResult);
		}

		task = new UpdaterAsyncTask();
		task.execute((Void)null);

		bindService(); // bind to the bluetooth LE service that is created

		if(!broadcastReceiverRegistered)
		{
			getActivity().registerReceiver(broadcastReceiver, new IntentFilter(ExtendedBluetoothLEClientProfile.onLEDeviceConnectedBroadcast));
			getActivity().registerReceiver(broadcastReceiver, new IntentFilter(ExtendedBluetoothLEClientProfile.onDiscoverCharacteristicsBroadcast));
			broadcastReceiverRegistered = true;
		}

		return layout;
	}

	private ServiceConnection serviceConnection = new ServiceConnection()
	{
		public void onServiceConnected(ComponentName className, IBinder service) 
		{
			// service connected
			bluetoothLEService = ((SimpleBlePeripheralService.BluetoothLEBinder)service).getService();
		}

		public void onServiceDisconnected(ComponentName className) 
		{
			// Service disconnected. Did it crash?
			bluetoothLEService = null;
		}
	};

	private void bindService()
	{
		getActivity().bindService(new Intent(getActivity(), SimpleBlePeripheralService.class), serviceConnection, Context.BIND_AUTO_CREATE);
		isServiceBound = true;
	}

	private void unbindService()
	{
		if(isServiceBound)
		{
			getActivity().unbindService(serviceConnection);
			isServiceBound = false;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// handle the request to turn bluetooth on
		if(requestCode == enableBluetoothActivityResult)
		{
			if(resultCode == Activity.RESULT_OK)
			{
				Toast.makeText(getActivity(), "Bluetooth On", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(getActivity(), "Bluetooth must be turned on", Toast.LENGTH_LONG).show();
			}
		}
		// handle the connect request
		else if(requestCode == discoveryActivityResult) 
		{
			if(resultCode == Activity.RESULT_OK)
			{
				String address = data.getStringExtra(DiscoveryActivity.addressToConnect); // obtain the address that is returned
				device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address); // get the bluetooth device

				// begin bluetoothLE connection
				//bluetoothLEService.setRemoteDevice(device);	
				boolean result = bluetoothLEService.connectLEDevice(device);
				Log.i(TAG, "ConnectLEDevice result is " + result);

			}
		}	
	}

	@Override
	public void onDestroy() 
	{		
		task.cancel(true);

		unbindService();

		if(broadcastReceiverRegistered)
		{
			getActivity().unregisterReceiver(broadcastReceiver);
			broadcastReceiverRegistered = false;
		}

		super.onDestroy();
	}


	private class UpdaterAsyncTask extends AsyncTask<Void, Integer, Void> 
	{
		protected Void doInBackground(Void... params) 
		{
			while(true)
			{
				int progress = -1;
				if(bluetoothLEService != null)
					progress = bluetoothLEService.getProfileState();

				publishProgress(progress);

				try
				{
					Thread.sleep(500);
				}
				catch (InterruptedException e)
				{
				}

				if(isCancelled())
					break;
			}

			return null;
		}

		protected void onProgressUpdate(Integer... progress) 
		{
			int res = progress[0];
			switch(res)
			{
			case 0:
				status.setText("GATT_STATE_DISCONNECTED");
				break;
			case 1:
				status.setText("GATT_STATE_CONNECTING");
				break;
			case 2:
				status.setText("GATT_STATE_CONNECTED");
				break;
			case 3:
				status.setText("GATT_STATE_DISCONNECTING");
				break;
			case -1:
				status.setText("-1");
				break;
			default:
				status.setText("unknown state");
			}
		}

		protected void onPostExecute(Void result) 
		{
		}
	}

}
**/