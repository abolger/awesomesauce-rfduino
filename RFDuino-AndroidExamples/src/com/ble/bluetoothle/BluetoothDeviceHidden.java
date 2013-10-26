package com.ble.bluetoothle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothDevice;

public class BluetoothDeviceHidden
{
	static Method getDeviceTypeMethod = null;
	static Method createBondMethod = null;
	static Method isLEDeviceConnectedMethod = null;
	static Method removeBondMethod = null;

	public static int getDeviceType(BluetoothDevice device)
	{
		int result = -1;
		try
		{
			// hunt for the getDeviceType() method
			if(getDeviceTypeMethod == null)
			{
				@SuppressWarnings("rawtypes")
				Class classToInvestigate = Class.forName("android.bluetooth.BluetoothDevice");
				Method[] aClassMethods = classToInvestigate.getDeclaredMethods();
				for(Method m : aClassMethods)
				{
					if(m.getName().equals("getDeviceType"))
					{
						getDeviceTypeMethod = m;
						break;
					}
				}
			}

			result = (Integer) getDeviceTypeMethod.invoke(device, (Object[]) null);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}


	public static boolean isLEDeviceConnected(BluetoothDevice device)
	{
		boolean result = false;
		try
		{
			// hunt for the getDeviceType() method
			if(isLEDeviceConnectedMethod == null)
			{
				@SuppressWarnings("rawtypes")
				Class classToInvestigate = Class.forName("android.bluetooth.BluetoothDevice");
				Method[] aClassMethods = classToInvestigate.getDeclaredMethods();
				for(Method m : aClassMethods)
				{
					if(m.getName().equals("isLEDeviceConnected"))
					{
						isLEDeviceConnectedMethod = m;
						break;
					}
				}
			}

			result = (Boolean) isLEDeviceConnectedMethod.invoke(device, (Object[]) null);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}


	public static boolean createBond(BluetoothDevice device)
	{
		boolean result = false;
		try
		{
			// hunt for the getDeviceType() method
			if(createBondMethod == null)
			{
				@SuppressWarnings("rawtypes")
				Class classToInvestigate = Class.forName("android.bluetooth.BluetoothDevice");
				Method[] aClassMethods = classToInvestigate.getDeclaredMethods();
				for(Method m : aClassMethods)
				{
					if(m.getName().equals("createBond"))
					{
						createBondMethod = m;
						break;
					}
				}
			}

			result = (Boolean) createBondMethod.invoke(device, (Object[]) null);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public static void removeBond(BluetoothDevice device) 
	{
		try 
		{
			if(removeBondMethod == null)
			{
				removeBondMethod = Class.forName("android.bluetooth.BluetoothDevice").getMethod("removeBond", (Class[]) null);
			}
			
			removeBondMethod.invoke(device, (Object[]) null);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
