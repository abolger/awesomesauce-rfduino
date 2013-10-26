package com.samsung.bluetoothle;

/*
 * This class is obtained from the decompilation of android OS. 
 * Only the function declarations is needed to compile.
 * At runtime the system class will be loaded instead of this dummy class.
 */

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IBluetoothLEClientCharUpdationCallBack extends IInterface
{
	public abstract void onDiscoverCharacteristics(String paramString, String[] paramArrayOfString)
			throws RemoteException;

	public abstract void onReadCharValue(String paramString)
			throws RemoteException;

	public abstract void onWatcherValueChanged(String paramString1, String paramString2)
			throws RemoteException;

	public abstract void onWriteCharValue(String paramString1, String paramString2)
			throws RemoteException;

	public abstract void onWriteClientConfigDesc(String paramString1, String paramString2)
			throws RemoteException;

	public static abstract class Stub extends Binder
	implements IBluetoothLEClientCharUpdationCallBack
	{
		@SuppressWarnings("unused")
		private static final String DESCRIPTOR = "com.samsung.bluetoothle.IBluetoothLEClientCharUpdationCallBack";
		static final int TRANSACTION_onDiscoverCharacteristics = 4;
		static final int TRANSACTION_onReadCharValue = 1;
		static final int TRANSACTION_onWatcherValueChanged = 5;
		static final int TRANSACTION_onWriteCharValue = 2;
		static final int TRANSACTION_onWriteClientConfigDesc = 3;

		public Stub()
		{
			attachInterface(this, "com.samsung.bluetoothle.IBluetoothLEClientCharUpdationCallBack");
		}

		public static IBluetoothLEClientCharUpdationCallBack asInterface(IBinder paramIBinder)
		{
			if (paramIBinder == null)
				return null;
			IInterface localIInterface = paramIBinder.queryLocalInterface("com.samsung.bluetoothle.IBluetoothLEClientCharUpdationCallBack");
			if ((localIInterface != null) && ((localIInterface instanceof IBluetoothLEClientCharUpdationCallBack)))
				return (IBluetoothLEClientCharUpdationCallBack)localIInterface;
			return new Proxy(paramIBinder);
		}

		public IBinder asBinder()
		{
			return this;
		}

		public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
				throws RemoteException
				{
			switch (paramInt1)
			{
			default:
				return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
			case 1598968902:
				paramParcel2.writeString("com.samsung.bluetoothle.IBluetoothLEClientCharUpdationCallBack");
				return true;
			case 1:
				paramParcel1.enforceInterface("com.samsung.bluetoothle.IBluetoothLEClientCharUpdationCallBack");
				onReadCharValue(paramParcel1.readString());
				paramParcel2.writeNoException();
				return true;
			case 2:
				paramParcel1.enforceInterface("com.samsung.bluetoothle.IBluetoothLEClientCharUpdationCallBack");
				onWriteCharValue(paramParcel1.readString(), paramParcel1.readString());
				paramParcel2.writeNoException();
				return true;
			case 3:
				paramParcel1.enforceInterface("com.samsung.bluetoothle.IBluetoothLEClientCharUpdationCallBack");
				onWriteClientConfigDesc(paramParcel1.readString(), paramParcel1.readString());
				paramParcel2.writeNoException();
				return true;
			case 4:
				paramParcel1.enforceInterface("com.samsung.bluetoothle.IBluetoothLEClientCharUpdationCallBack");
				onDiscoverCharacteristics(paramParcel1.readString(), paramParcel1.createStringArray());
				paramParcel2.writeNoException();
				return true;
			case 5:
			}
			paramParcel1.enforceInterface("com.samsung.bluetoothle.IBluetoothLEClientCharUpdationCallBack");
			onWatcherValueChanged(paramParcel1.readString(), paramParcel1.readString());
			paramParcel2.writeNoException();
			return true;
				}

		private static class Proxy
		implements IBluetoothLEClientCharUpdationCallBack
		{
			private IBinder mRemote;

			Proxy(IBinder paramIBinder)
			{
				this.mRemote = paramIBinder;
			}

			public IBinder asBinder()
			{
				return this.mRemote;
			}

			@SuppressWarnings("unused")
			public String getInterfaceDescriptor()
			{
				return "com.samsung.bluetoothle.IBluetoothLEClientCharUpdationCallBack";
			}

			public void onDiscoverCharacteristics(String paramString, String[] paramArrayOfString)
					throws RemoteException
					{
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try
				{
					localParcel1.writeInterfaceToken("com.samsung.bluetoothle.IBluetoothLEClientCharUpdationCallBack");
					localParcel1.writeString(paramString);
					localParcel1.writeStringArray(paramArrayOfString);
					this.mRemote.transact(4, localParcel1, localParcel2, 0);
					localParcel2.readException();
					return;
				}
				finally
				{
					localParcel2.recycle();
					localParcel1.recycle();
				}
					}

			public void onReadCharValue(String paramString)
					throws RemoteException
					{
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try
				{
					localParcel1.writeInterfaceToken("com.samsung.bluetoothle.IBluetoothLEClientCharUpdationCallBack");
					localParcel1.writeString(paramString);
					this.mRemote.transact(1, localParcel1, localParcel2, 0);
					localParcel2.readException();
					return;
				}
				finally
				{
					localParcel2.recycle();
					localParcel1.recycle();
				}
					}

			public void onWatcherValueChanged(String paramString1, String paramString2)
					throws RemoteException
					{
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try
				{
					localParcel1.writeInterfaceToken("com.samsung.bluetoothle.IBluetoothLEClientCharUpdationCallBack");
					localParcel1.writeString(paramString1);
					localParcel1.writeString(paramString2);
					this.mRemote.transact(5, localParcel1, localParcel2, 0);
					localParcel2.readException();
					return;
				}
				finally
				{
					localParcel2.recycle();
					localParcel1.recycle();
				}
					}

			public void onWriteCharValue(String paramString1, String paramString2)
					throws RemoteException
					{
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try
				{
					localParcel1.writeInterfaceToken("com.samsung.bluetoothle.IBluetoothLEClientCharUpdationCallBack");
					localParcel1.writeString(paramString1);
					localParcel1.writeString(paramString2);
					this.mRemote.transact(2, localParcel1, localParcel2, 0);
					localParcel2.readException();
					return;
				}
				finally
				{
					localParcel2.recycle();
					localParcel1.recycle();
				}
					}

			public void onWriteClientConfigDesc(String paramString1, String paramString2)
					throws RemoteException
					{
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try
				{
					localParcel1.writeInterfaceToken("com.samsung.bluetoothle.IBluetoothLEClientCharUpdationCallBack");
					localParcel1.writeString(paramString1);
					localParcel1.writeString(paramString2);
					this.mRemote.transact(3, localParcel1, localParcel2, 0);
					localParcel2.readException();
					return;
				}
				finally
				{
					localParcel2.recycle();
					localParcel1.recycle();
				}
					}
		}
	}
}

/* Qualified Name:     com.samsung.bluetoothle.IBluetoothLEClientCharUpdationCallBack
 * JD-Core Version:    0.6.2
 */