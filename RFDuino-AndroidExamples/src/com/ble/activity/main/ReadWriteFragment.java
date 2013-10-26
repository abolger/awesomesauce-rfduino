/**
package com.ble.activity.main;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ble.R;
import com.ble.bluetoothle.BluetoothLeUtils;
import com.ble.bluetoothle.ExtendedBluetoothLEClientService;
import com.ble.bluetoothle.SimpleBlePeripheralService;
import com.samsung.bluetoothle.BluetoothLEClientChar;

public class ReadWriteFragment extends Fragment implements OnClickListener
{
	private static final String TAG= "ReadWriteFragment";
	private static final String NULL_CHAR_MESSAGE = "Error: null for chacteristic. Please restart app/phone.";
	// permissions
	// 0000 0nrw -- last 3 bits stand for allow notifications, allow read, allow write
	private int permissions[] = {3, 2, 1, 4, 0}; 

	private EditText values[] = new EditText[5];
	private Button readButtons[] = new Button[5];
	private Button writeButtons[] = new Button[5];
	private ToggleButton notificationButtons[] = new ToggleButton[5];

	private SimpleBlePeripheralService bluetoothLEService;
	private boolean isServiceBound = false;

	private boolean isBroadCastReceiverRegistered = false;
	private final BroadcastReceiver notificationBroadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String str = intent.getAction();

			if(str.equals(ExtendedBluetoothLEClientService.onWatcherValueChangedBroadcast))
			{
				Log.i(TAG, "Received onWatcherValueChangedBroadcast");

				// received notifications from characteristic 4
				BluetoothLEClientChar c = bluetoothLEService.getCharbyUUID(bluetoothLEService.connectedDevice, SimpleBlePeripheralService.SIMPLEPROFILE_CHAR4_UUID);

				if(c == null)
				{
					Toast.makeText(getActivity(), NULL_CHAR_MESSAGE, Toast.LENGTH_LONG).show();
					return;

				}
				int[] data = BluetoothLeUtils.fromBluetoothLeFormat(c.getCharVaule());
				if(data != null)
					values[3].setText(""+data[0]);
			}
		}
	};

	private int indexToRead = 0;
	
	private final BroadcastReceiver readBroadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String uuid128 = indexToUuid128(indexToRead);
			BluetoothLEClientChar c = bluetoothLEService.getCharbyUUID(bluetoothLEService.connectedDevice, uuid128);

			byte data[] = c.getCharVaule();

			if(data != null)
			{
				values[indexToRead].setText(""+BluetoothLeUtils.fromBluetoothLeFormat(data)[0]);
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		if (container == null) 
			return null;

		LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_readwrite, container, false);

		values[0] = (EditText) layout.findViewById(R.id.fragment_readwrite_value1);
		values[1] = (EditText) layout.findViewById(R.id.fragment_readwrite_value2);
		values[2] = (EditText) layout.findViewById(R.id.fragment_readwrite_value3);
		values[3] = (EditText) layout.findViewById(R.id.fragment_readwrite_value4);
		values[4] = (EditText) layout.findViewById(R.id.fragment_readwrite_value5);

		readButtons[0] = (Button) layout.findViewById(R.id.fragment_readwrite_read1);
		readButtons[1] = (Button) layout.findViewById(R.id.fragment_readwrite_read2);
		readButtons[2] = (Button) layout.findViewById(R.id.fragment_readwrite_read3);
		readButtons[3] = (Button) layout.findViewById(R.id.fragment_readwrite_read4);
		readButtons[4] = (Button) layout.findViewById(R.id.fragment_readwrite_read5);

		writeButtons[0] = (Button) layout.findViewById(R.id.fragment_readwrite_write1);
		writeButtons[1] = (Button) layout.findViewById(R.id.fragment_readwrite_write2);
		writeButtons[2] = (Button) layout.findViewById(R.id.fragment_readwrite_write3);
		writeButtons[3] = (Button) layout.findViewById(R.id.fragment_readwrite_write4);
		writeButtons[4] = (Button) layout.findViewById(R.id.fragment_readwrite_write5);

		notificationButtons[0] = (ToggleButton) layout.findViewById(R.id.fragment_readwrite_notify1);
		notificationButtons[1] = (ToggleButton) layout.findViewById(R.id.fragment_readwrite_notify2);
		notificationButtons[2] = (ToggleButton) layout.findViewById(R.id.fragment_readwrite_notify3);
		notificationButtons[3] = (ToggleButton) layout.findViewById(R.id.fragment_readwrite_notify4);
		notificationButtons[4] = (ToggleButton) layout.findViewById(R.id.fragment_readwrite_notify5);

		// enable/disable buttons according to permissions
		for(int i=0; i<5; i++)
		{
			if((permissions[i] & (1<<2)) != 0)
				notificationButtons[i].setEnabled(true);
			else
				notificationButtons[i].setEnabled(false);

			if((permissions[i] & (1<<1)) != 0)
				readButtons[i].setEnabled(true);
			else
				readButtons[i].setEnabled(false);

			if((permissions[i] & (1<<0)) != 0)
				writeButtons[i].setEnabled(true);
			else
				writeButtons[i].setEnabled(false);

			readButtons[i].setOnClickListener(this);
			writeButtons[i].setOnClickListener(this);
			notificationButtons[i].setOnClickListener(this);
		}

		bindService(); // bind to the bluetooth LE service that is created

		getActivity().registerReceiver(readBroadcastReceiver, new IntentFilter(ExtendedBluetoothLEClientService.onDiscoverCharacteristicsBroadcast));

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
	public void onDestroy() 
	{		
		unbindService();

		getActivity().unregisterReceiver(readBroadcastReceiver);

		if(isBroadCastReceiverRegistered)
		{
			bluetoothLEService.unregisterWatcher();
			getActivity().unregisterReceiver(notificationBroadcastReceiver);

			isBroadCastReceiverRegistered = false;
		}

		super.onDestroy();
	}


	@Override
	public void onClick(View arg0)
	{
		switch(arg0.getId())
		{
		case R.id.fragment_readwrite_read1:
			read(0);
			break;
		case R.id.fragment_readwrite_read2:
			read(1);
			break;
		case R.id.fragment_readwrite_read3:
			read(2);
			break;
		case R.id.fragment_readwrite_read4:
			read(3);
			break;
		case R.id.fragment_readwrite_read5:
			read(4);
			break;

		case R.id.fragment_readwrite_write1:
			write(0);
			break;
		case R.id.fragment_readwrite_write2:
			write(1);
			break;
		case R.id.fragment_readwrite_write3:
			write(2);
			break;
		case R.id.fragment_readwrite_write4:
			write(3);
			break;
		case R.id.fragment_readwrite_write5:
			write(4);
			break;

		case R.id.fragment_readwrite_notify4:
			notification();
			break;
		}
	}

	private String indexToUuid128(int index)
	{
		String uuid128 = null;

		switch(index)
		{
		case 0:
			uuid128 = SimpleBlePeripheralService.SIMPLEPROFILE_CHAR1_UUID;
			break;
		case 1:
			uuid128 = SimpleBlePeripheralService.SIMPLEPROFILE_CHAR2_UUID;
			break;
		case 2:
			uuid128 = SimpleBlePeripheralService.SIMPLEPROFILE_CHAR3_UUID;
			break;
		case 3:
			uuid128 = SimpleBlePeripheralService.SIMPLEPROFILE_CHAR4_UUID;
			break;
		case 4:
			uuid128 = SimpleBlePeripheralService.SIMPLEPROFILE_CHAR5_UUID;
			break;
		default:
			uuid128 = SimpleBlePeripheralService.SIMPLEPROFILE_CHAR1_UUID;
			break;
		}

		return uuid128;
	}

	private void read(int index)
	{
		String uuid128 = indexToUuid128(index);

		bluetoothLEService.bluetoothLEClientProfile.discoverCharacteristics(bluetoothLEService.connectedDevice, uuid128);
		BluetoothLEClientChar c = bluetoothLEService.getCharbyUUID(bluetoothLEService.connectedDevice, uuid128);

		if(c == null)
		{
			Toast.makeText(getActivity(), NULL_CHAR_MESSAGE, Toast.LENGTH_LONG).show();
			return;
		}
		
		indexToRead = index;
	}



	private void write(int index)
	{
		String uuid128 = indexToUuid128(index);

		try
		{
			BluetoothLEClientChar c = bluetoothLEService.getCharbyUUID(bluetoothLEService.connectedDevice, uuid128);

			if(c == null)
			{
				Toast.makeText(getActivity(), NULL_CHAR_MESSAGE, Toast.LENGTH_LONG).show();
				return;
			}

			int value = Integer.parseInt(values[index].getText().toString());
			byte data[] = new byte[1];
			data[0] = (byte) value;
			c.setCharValue(data);
			bluetoothLEService.writeCharValue(c, 1);
		}
		catch(NumberFormatException e)
		{
			// just ignore it.
		}
	}

	private void notification()
	{
		if(!isBroadCastReceiverRegistered)
		{
			// register watcher for the bluetooth service
			bluetoothLEService.registerWatcher();

			// register the broadcast receiver
			getActivity().registerReceiver(notificationBroadcastReceiver, new IntentFilter(ExtendedBluetoothLEClientService.onWatcherValueChangedBroadcast));

			isBroadCastReceiverRegistered = true;

			// now write the 1 to the workaround characteristic to begin notifications
			String uuid128 = SimpleBlePeripheralService.SIMPLEPROFILE_NOTIFY_WORKAROUND_UUID;
			BluetoothLEClientChar c = bluetoothLEService.getCharbyUUID(bluetoothLEService.connectedDevice, uuid128);

			if(c == null)
			{
				Toast.makeText(getActivity(), NULL_CHAR_MESSAGE, Toast.LENGTH_LONG).show();
				return;
			}

			byte data[] = {1};
			c.setCharValue(data);
			bluetoothLEService.writeCharValue(c, 1);
		}
		else
		{
			// disable notification (workaround)
			String uuid128 = SimpleBlePeripheralService.SIMPLEPROFILE_NOTIFY_WORKAROUND_UUID;
			BluetoothLEClientChar c = bluetoothLEService.getCharbyUUID(bluetoothLEService.connectedDevice, uuid128);

			if(c == null)
			{
				Toast.makeText(getActivity(), NULL_CHAR_MESSAGE, Toast.LENGTH_LONG).show();
				return;
			}

			byte data[] = {0};
			c.setCharValue(data);

			bluetoothLEService.unregisterWatcher();

			getActivity().unregisterReceiver(notificationBroadcastReceiver);

			isBroadCastReceiverRegistered = false;
		}
	}
}**/
