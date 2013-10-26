package com.ble.bluetoothle;

import android.util.Log;

public class BluetoothLeUtils
{
	private static final String TAG = "BluetoothLeUtils";
	
	// This function is necessary because the output from getCharVaule() is usually in a strange format
	// it is a character string of hexadecimals.
	public static int[] fromBluetoothLeFormat(byte[] charArray)
	{
		if(charArray.length % 2 != 0)
		{
			// strange unexpected data
			Log.i(TAG, "not hex string!" + toHexString(charArray));
			int result[] = new int[charArray.length];
			for(int i=0; i<result.length; i++)
				result[i] = charArray[i];
			
			return result;
		}
		
		int result[] = new int[charArray.length/2];

		for(int i=0, j=0; i<charArray.length; i+=2, j++)
		{
			int upper = parseByte(charArray[i]);
			int lower = parseByte(charArray[i+1]);

			result[j] = (upper << 4) | lower;
		}

		return result;
	}

	private static int parseByte(byte b)
	{
		if(b <= '9' && b >= '0')
			return b - '0';

		if(b <= 'f' && b >= 'a')
			return b - 'a' + 10;

		return 0;
	}
	
	public static String toHexString(int[] data)
	{
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<data.length; i++)
			sb.append(String.format("%02X", data[i])).append(" ");
		return sb.toString();
	}
	
	public static String toHexString(byte[] data)
	{
		StringBuilder sb = new StringBuilder();
		
		for(int i=0; i<data.length; i++)
			sb.append(String.format("%02X", (int)data[i])).append(" ");
		return sb.toString();
	}
}
