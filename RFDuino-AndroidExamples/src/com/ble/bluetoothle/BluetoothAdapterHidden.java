package com.ble.bluetoothle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

public class BluetoothAdapterHidden
{
	static Method startLeDiscoveryMethod = null;
	private static final String TAG = "BluetoothAdapterHidden";

	public static void startLeDiscovery(BluetoothAdapter bluetoothAdapter)
	{
		try
		{
			if(startLeDiscoveryMethod == null) // find the method if necessary
			{
				@SuppressWarnings("rawtypes")
				Class classToInvestigate = Class.forName("android.bluetooth.BluetoothAdapter");
				Method[] aClassMethods = classToInvestigate.getDeclaredMethods();
				for(Method m : aClassMethods)
				{
					if(m.getName().equals("startLeDiscovery"))
					{
						startLeDiscoveryMethod = m;
						break;
					}
				}
			}

			startLeDiscoveryMethod.invoke(bluetoothAdapter, (Object[]) null);
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			Log.e(TAG, "Failure");
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			Log.e(TAG, "Failure");
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			Log.e(TAG, "Failure");
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			Log.e(TAG, "Failure");
			e.printStackTrace();
		}
	}
}
