package com.samsung.bluetoothle;

/*
 * This class is obtained from the decompilation of android OS. 
 * Only the function declarations is needed to compile.
 * At runtime the system class will be loaded instead of this dummy class.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class BluetoothLEClientChar implements Parcelable
{
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final Parcelable.Creator<BluetoothLEClientChar> CREATOR = new Parcelable.Creator()
	{
		public BluetoothLEClientChar createFromParcel(Parcel paramAnonymousParcel)
		{
			return new BluetoothLEClientChar(paramAnonymousParcel.readString());
		}

		public BluetoothLEClientChar[] newArray(int paramAnonymousInt)
		{
			return new BluetoothLEClientChar[paramAnonymousInt];
		}
	};
//	private static final boolean DEBUG = true;
//	private static final String TAG = "BluetoothLEClientChar";
//	private String mCharPath;
//	private String mCharUUID;
//	private byte[] mClientDesc;
//	private String mDescription;
//	private String mName;
//	private byte[] mValue;

	public BluetoothLEClientChar(String paramString)
	{
//		this.mCharPath = paramString;
	}

	@SuppressWarnings("unused")
	private void LogD(String paramString)
	{
//		Log.d("BluetoothLEClientChar", paramString);
	}

	public int describeContents()
	{
		return 0;
	}

	public String getCharPath()
	{
//		LogD("getCharPath:" + this.mCharPath);
//		return this.mCharPath;
		
		return null;
	}

	public String getCharUUID()
	{
//		LogD("getCharUUID:" + this.mCharUUID);
//		return this.mCharUUID;
		
		return null;
	}

	public byte[] getCharVaule()
	{
//		LogD("getCharVaule ");
//		if (this.mValue != null)
//		{
//			int i = this.mValue.length;
//			byte[] arrayOfByte = new byte[i];
//			System.arraycopy(this.mValue, 0, arrayOfByte, 0, i);
//			return arrayOfByte;
//		}
//		LogD("the value is not initialized");
		return null;
	}

	public byte[] getClientConfigDesc()
	{
//		LogD("getClientConfigDesc   : ");
//		if (this.mClientDesc != null)
//		{
//			int i = this.mClientDesc.length;
//			byte[] arrayOfByte = new byte[i];
//			System.arraycopy(this.mClientDesc, 0, arrayOfByte, 0, i);
//			return arrayOfByte;
//		}
//		LogD("the value is not initialized");
		return null;
	}

	void setCharPath(String paramString)
	{
//		LogD("setCharPath:" + paramString);
//		this.mCharPath = paramString;
	}

	public void setCharValue(byte[] paramArrayOfByte)
	{
//		LogD("setCharValue of  char : " + this.mCharPath);
//		int i = paramArrayOfByte.length;
//		this.mValue = null;
//		this.mValue = new byte[i];
//		System.arraycopy(paramArrayOfByte, 0, this.mValue, 0, i);
	}

	public void setClientConfigDesc(byte[] paramArrayOfByte)
	{
//		LogD("setClientConfigDes of  char : " + this.mCharPath);
//		int i = paramArrayOfByte.length;
//		this.mClientDesc = null;
//		this.mClientDesc = new byte[i];
//		System.arraycopy(paramArrayOfByte, 0, this.mClientDesc, 0, i);
	}

	void setProperty(String paramString1, String paramString2)
	{
//		LogD("setProperty name : " + paramString1 + " : Value :" + paramString2);
//		if (paramString1.equals("Value"))
//		{
//			if ((paramString2 == null) || (paramString2.length() == 0))
//			{
//				this.mValue = new byte[1];
//				this.mValue[0] = 0;
//			}
//			while (true)
//			{
//				for (int j = 0; j < this.mValue.length; j++)
//					LogD("setProperty Value :" + (char)this.mValue[j]);
//				LogD("Check size of string " + paramString2.length());
//				try
//				{
//					this.mValue = paramString2.getBytes("UTF8");
//				}
//				catch (Exception localException2)
//				{
//					Log.e("BluetoothLEClientChar", "## Exception in conversion");
//				}
//			}
//		}
//		if (paramString1.equals("Name"))
//			this.mName = paramString2;
//		do
//		{
//			return;
//			if (paramString1.equals("UUID"))
//			{
//				this.mCharUUID = paramString2;
//				return;
//			}
//			if (paramString1.equals("ClientDescriptor"))
//			{
//				if ((paramString2 == null) || (paramString2.length() == 0))
//				{
//					this.mClientDesc = new byte[1];
//					this.mClientDesc[0] = 0;
//				}
//				while (true)
//				{
//					for (int i = 0; i < this.mClientDesc.length; i++)
//						LogD("setProperty mClientDesc :" + (char)this.mClientDesc[i]);
//					break;
//					Log.e("BluetoothLEClientChar", "Check size of string " + paramString2.length());
//					try
//					{
//						this.mClientDesc = paramString2.getBytes("UTF8");
//					}
//					catch (Exception localException1)
//					{
//						Log.e("BluetoothLEClientChar", "## Exception in conversion");
//					}
//				}
//			}
//		}
//		while (!paramString1.equals("Description"));
//		this.mDescription = paramString2;
	}

	public void setcharUUID(String paramString)
	{
//		this.mCharUUID = paramString;
	}

	public void writeToParcel(Parcel paramParcel, int paramInt)
	{
	}
}

/* Qualified Name:     com.samsung.bluetoothle.BluetoothLEClientChar
 * JD-Core Version:    0.6.2
 */