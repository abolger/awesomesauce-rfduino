package com.samsung.bluetoothle;

/*
 * This class is obtained from the decompilation of android OS. 
 * Only the function declarations is needed to compile.
 * At runtime the system class will be loaded instead of this dummy class.
 */

import android.bluetooth.BluetoothDevice;
import android.util.Log;
import java.util.ArrayList;

public abstract class BluetoothLEClientService
{
//	private static final boolean DEBUG = true;
	public static final int GATT_WRITE_CMD = 0;
	public static final int GATT_WRITE_REQ = 1;
//	private static final String TAG = "BluetoothLEClientService";
//	private boolean discoverCharInProgress = false;
//	private boolean isDiscoverCharByUUID = false;
//	private BluetoothLEClientCharUpdationCallBack mCallback;
//	private BluetoothLEClientProfile mProfile;
//	private ArrayList<BluetoothLEClientChar> mServiceChars;
//	private String mServicePath = null;
//	private String mServiceUUID;

	public BluetoothLEClientService(String paramString)
	{
//		LogD("BluetoothLEClientService", "BluetoothLEClientService");
//		this.mServiceUUID = paramString;
//		this.mCallback = new BluetoothLEClientCharUpdationCallBack();
//		this.mProfile = null;
//		this.mServiceChars = new ArrayList();
	}

	private void LogD(String paramString1, String paramString2)
	{
		Log.d(paramString1, paramString2);
	}

	@SuppressWarnings("unused")
	private BluetoothLEClientChar getCharbyPath(String paramString)
	{
//		LogD("BluetoothLEClientService", "getCharbyPath");
//		for (int i = 0; i < this.mServiceChars.size(); i++)
//		{
//			BluetoothLEClientChar localBluetoothLEClientChar = (BluetoothLEClientChar)this.mServiceChars.get(i);
//			if (localBluetoothLEClientChar.getCharPath().equals(paramString))
//				return localBluetoothLEClientChar;
//		}
		return null;
	}

	@SuppressWarnings("unused")
	private void updateServiceChars(String paramString, String[] paramArrayOfString)
	{
//		LogD("BluetoothLEClientService", "updateServiceChars");
//		if (this.mProfile.getLEProfileState() == 2)
//		{
//			this.mServiceChars.clear();
//			for (int i = 0; i < paramArrayOfString.length; i++)
//			{
//				LogD("BluetoothLEClientService", "Charpath :" + paramArrayOfString[i]);
//				String[] arrayOfString = this.mProfile.getGattProxy().gattGetCharProperties(paramString, paramArrayOfString[i]);
//				BluetoothLEClientChar localBluetoothLEClientChar = new BluetoothLEClientChar(paramArrayOfString[i]);
//				if (arrayOfString != null)
//				{
//					for (int j = 0; j < arrayOfString.length; j += 2)
//						localBluetoothLEClientChar.setProperty(arrayOfString[j], arrayOfString[(j + 1)]);
//					this.mServiceChars.add(localBluetoothLEClientChar);
//				}
//			}
//			this.mProfile.updateRefreshState(this.mServicePath);
//		}
	}

	@SuppressWarnings("unused")
	private void updateSingleChar(String paramString1, String paramString2)
	{
//		LogD("BluetoothLEClientService", "updateSingleChar");
//		if (this.mProfile.getLEProfileState() == 2)
//		{
//			BluetoothLEClientChar localBluetoothLEClientChar = getCharbyPath(paramString2);
//			int i = 0;
//			if (localBluetoothLEClientChar == null)
//			{
//				localBluetoothLEClientChar = new BluetoothLEClientChar(paramString2);
//				i = 1;
//			}
//			String[] arrayOfString = this.mProfile.getGattProxy().gattGetCharProperties(paramString1, paramString2);
//			if (arrayOfString != null)
//			{
//				for (int j = 0; j < arrayOfString.length; j += 2)
//					localBluetoothLEClientChar.setProperty(arrayOfString[j], arrayOfString[(j + 1)]);
//				if (i != 0)
//					this.mServiceChars.add(localBluetoothLEClientChar);
//				onDiscoverCharacteristics(localBluetoothLEClientChar);
//			}
//		}
	}

	@SuppressWarnings("unused")
	private void waitDiscoveryDone()
	{
//		try
//		{
//			LogD("BluetoothLEClientService", "waitDiscoveryDone");
//			try
//			{
//				wait(70000L);
//				return;
//			}
//			catch (InterruptedException localInterruptedException)
//			{
//				while (true)
//					Log.e("BluetoothLEClientService", "Characteristics discovery takes too long");
//			}
//		}
//		finally
//		{
//		}
	}

	public void discoverCharacteristics(BluetoothDevice paramBluetoothDevice)
	{
//		LogD("BluetoothLEClientService", "discoverCharacteristics");
//		if (this.mServicePath != null)
//		{
//			if (this.discoverCharInProgress)
//				waitDiscoveryDone();
//			if (this.mProfile.getGattProxy().gattDiscoveryCharacteristics(paramBluetoothDevice.getAddress(), this.mServicePath, "0xffff"))
//				setCharDiscoveryProgress(true);
//			return;
//		}
//		Log.d("BluetoothLEClientService", "mServicePath is null");
	}

	public void discoverCharacteristics(BluetoothDevice paramBluetoothDevice, String paramString)
	{
//		LogD("BluetoothLEClientService", "discoverCharacteristics :" + paramString);
//		if (this.mServicePath != null)
//		{
//			if (this.discoverCharInProgress)
//				waitDiscoveryDone();
//			if (this.mProfile.getGattProxy().gattDiscoveryCharacteristics(paramBluetoothDevice.getAddress(), this.mServicePath, paramString))
//			{
//				setCharDiscoveryProgress(true);
//				this.isDiscoverCharByUUID = true;
//			}
//		}
	}

	@SuppressWarnings("rawtypes")
	public ArrayList getAllChars(BluetoothDevice paramBluetoothDevice)
	{
//		LogD("BluetoothLEClientService", "getAllChars");
//		if (paramBluetoothDevice.equals(this.mProfile.getConnectedLEDevice()))
//			return this.mServiceChars;
		return null;
	}

	public BluetoothLEClientChar getCharbyUUID(BluetoothDevice paramBluetoothDevice, String paramString)
	{
//		LogD("BluetoothLEClientService", "getChar");
//		if (paramBluetoothDevice.equals(this.mProfile.getConnectedLEDevice()))
//			for (int i = 0; i < this.mServiceChars.size(); i++)
//			{
//				BluetoothLEClientChar localBluetoothLEClientChar = (BluetoothLEClientChar)this.mServiceChars.get(i);
//				if (localBluetoothLEClientChar.getCharUUID().equals(paramString))
//					return localBluetoothLEClientChar;
//			}
		return null;
	}

	String getServicePath()
	{
//		return this.mServicePath;
		return null;
	}

	String getServiceUUID()
	{
//		LogD("BluetoothLEClientService", "mServiceUUID is " + this.mServiceUUID);
//		return this.mServiceUUID;
		return null;
	}

	void init(BluetoothDevice paramBluetoothDevice, BluetoothLEClientProfile paramBluetoothLEClientProfile)
	{
//		LogD("BluetoothLEClientService", "init");
//		String[] arrayOfString = paramBluetoothDevice.getRemoteServicePaths();
//		this.mProfile = paramBluetoothLEClientProfile;
//		for (int i = 0; ; i++)
//			if (i < arrayOfString.length)
//			{
//				String str = paramBluetoothDevice.getRemoteServiceUUID(arrayOfString[i]);
//				if (str.length() == 4)
//					str = "0000" + str + "-0000-1000-8000-00805f9b34fb";
//				if (this.mServiceUUID.equals(str))
//				{
//					this.mServicePath = arrayOfString[i];
//					registerLEServiceCallback();
//				}
//			}
//			else
//			{
//				return;
//			}
	}

	boolean isCharDiscoveryInProgress()
	{
//		return this.discoverCharInProgress;
		return false;
	}

	public void onDiscoverCharacteristics(BluetoothLEClientChar paramBluetoothLEClientChar)
	{
		LogD("BluetoothLEClientService", "onDiscoverCharacteristics");
	}

	public void onWatcherValueChanged(BluetoothLEClientChar paramBluetoothLEClientChar)
	{
		LogD("BluetoothLEClientService", "onWatcherValueChanged");
	}

	public void onWriteCharValue(BluetoothLEClientChar paramBluetoothLEClientChar, String paramString)
	{
		LogD("BluetoothLEClientService", "onWriteCharValue");
	}

	public void onWriteClientConfigDesc(BluetoothLEClientChar paramBluetoothLEClientChar, String paramString)
	{
		LogD("BluetoothLEClientService", "onWriteClientConfigDesc");
	}

	public int readCharValue(BluetoothDevice paramBluetoothDevice, String paramString)
	{
		LogD("BluetoothLEClientService", "readCharValue");
		return 0;
	}

	public void registerLEServiceCallback()
	{
//		LogD("BluetoothLEClientService", "registerLEServiceCallback");
//		this.mProfile.getGattProxy().registerLEServiceCallback(this.mServicePath, this.mCallback);
	}

	public boolean registerWatcher()
	{
//		LogD("BluetoothLEClientService", "registerWatcher");
//		return this.mProfile.getGattProxy().registerWatcher(this.mServicePath);
		return false;
	}

	void setCharDiscoveryProgress(boolean paramBoolean)
	{
//		LogD("BluetoothLEClientService", "setDiscoveryProgress::" + paramBoolean);
//		this.discoverCharInProgress = paramBoolean;
	}

	public boolean unregisterWatcher()
	{
//		LogD("BluetoothLEClientService", "unregisterWatcher");
//		return this.mProfile.getGattProxy().unregisterWatcher(this.mServicePath);
		return false;
	}

	public int writeCharValue(BluetoothLEClientChar paramBluetoothLEClientChar, int paramInt)
	{
//		LogD("BluetoothLEClientService", "writeCharValue");
//		String str1 = this.mProfile.getConnectedLEDevice().getAddress();
//		String str2 = paramBluetoothLEClientChar.getCharPath();
//		byte[] arrayOfByte = paramBluetoothLEClientChar.getCharVaule();
//		String str3;
//		if (paramInt == 1)
//			str3 = "WRITE_REQ";
//		while (true)
//		{
//			if ((str1 != null) && (str2 != null) && (arrayOfByte != null))
//				this.mProfile.getGattProxy().gattSetCharProperty(str1, str3, str2, "Value", arrayOfByte);
//			return 0;
//			str3 = null;
//			if (paramInt == 0)
//				str3 = "WRITE_CMD";
//		}
		return 0;
	}

	public int writeClientConfigDesc(BluetoothLEClientChar paramBluetoothLEClientChar)
	{
//		LogD("BluetoothLEClientService", "writeClientConfigDesc");
//		String str1 = this.mProfile.getConnectedLEDevice().getAddress();
//		String str2 = paramBluetoothLEClientChar.getCharPath();
//		byte[] arrayOfByte = paramBluetoothLEClientChar.getClientConfigDesc();
//		if ((str1 != null) && (str2 != null) && (arrayOfByte != null))
//			this.mProfile.getGattProxy().gattSetCharProperty(str1, "WRITE_REQ", str2, "ClientConfiguration", arrayOfByte);
//		return 0;
		
		return 0;
	}

	@SuppressWarnings("unused")
	private class BluetoothLEClientCharUpdationCallBack extends IBluetoothLEClientCharUpdationCallBack.Stub
	{
		private static final String TAG = "BluetoothLEClientCharUpdationCallBack";

		BluetoothLEClientCharUpdationCallBack()
		{
		}

		public void onDiscoverCharacteristics(String paramString, String[] paramArrayOfString)
		{
//			BluetoothLEClientService.this.LogD("BluetoothLEClientCharUpdationCallBack", "onDiscoverCharacteristics length : " + paramArrayOfString.length);
//			if (BluetoothLEClientService.this.isDiscoverCharByUUID)
//			{
//				BluetoothLEClientService.this.updateSingleChar(paramString, paramArrayOfString[0]);
//				BluetoothLEClientService.access$202(BluetoothLEClientService.this, false);
//			}
//			while (true)
//			{
//				BluetoothLEClientService.this.setCharDiscoveryProgress(false);
//				try
//				{
//					notify();
//					return;
//					BluetoothLEClientService.this.updateServiceChars(paramString, paramArrayOfString);
//				}
//				catch (IllegalMonitorStateException localIllegalMonitorStateException)
//				{
//				}
//			}
		}

		public void onReadCharValue(String paramString)
		{
//			BluetoothLEClientService.this.LogD("BluetoothLEClientCharUpdationCallBack", "onReadCharValue");
		}

		public void onWatcherValueChanged(String paramString1, String paramString2)
		{
//			BluetoothLEClientService.this.LogD("BluetoothLEClientCharUpdationCallBack", "onWatcherValueChanged char Path :" + paramString1 + " value :" + paramString2);
//			for (int i = 0; ; i++)
//				if (i < BluetoothLEClientService.this.mServiceChars.size())
//				{
//					if (paramString1.equals(((BluetoothLEClientChar)BluetoothLEClientService.this.mServiceChars.get(i)).getCharPath()))
//					{
//						BluetoothLEClientChar localBluetoothLEClientChar = (BluetoothLEClientChar)BluetoothLEClientService.this.mServiceChars.get(i);
//						localBluetoothLEClientChar.setProperty("Value", paramString2);
//						BluetoothLEClientService.this.onWatcherValueChanged(localBluetoothLEClientChar);
//					}
//				}
//				else
//					return;
		}

		public void onWriteCharValue(String paramString1, String paramString2)
		{
//			BluetoothLEClientService.this.LogD("BluetoothLEClientCharUpdationCallBack", "onWriteCharValue char Path :" + paramString1 + " status :" + paramString2);
//			BluetoothLEClientChar localBluetoothLEClientChar = null;
//			for (int i = 0; ; i++)
//				if (i < BluetoothLEClientService.this.mServiceChars.size())
//				{
//					localBluetoothLEClientChar = (BluetoothLEClientChar)BluetoothLEClientService.this.mServiceChars.get(i);
//					if (!paramString1.equals(localBluetoothLEClientChar.getCharPath()));
//				}
//				else
//				{
//					BluetoothLEClientService.this.onWriteCharValue(localBluetoothLEClientChar, paramString2);
//					return;
//				}
		}

		public void onWriteClientConfigDesc(String paramString1, String paramString2)
		{
//			BluetoothLEClientService.this.LogD("BluetoothLEClientCharUpdationCallBack", "onWriteClientConfigDesc char Path :" + paramString1 + " status :" + paramString2);
//			BluetoothLEClientChar localBluetoothLEClientChar = null;
//			for (int i = 0; ; i++)
//				if (i < BluetoothLEClientService.this.mServiceChars.size())
//				{
//					localBluetoothLEClientChar = (BluetoothLEClientChar)BluetoothLEClientService.this.mServiceChars.get(i);
//					if (!paramString1.equals(localBluetoothLEClientChar.getCharPath()));
//				}
//				else
//				{
//					BluetoothLEClientService.this.onWriteClientConfigDesc(localBluetoothLEClientChar, paramString2);
//					return;
//				}
		}
	}
}

/* Qualified Name:     com.samsung.bluetoothle.BluetoothLEClientService
 * JD-Core Version:    0.6.2
 */