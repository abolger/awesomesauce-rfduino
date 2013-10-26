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

public abstract interface IBluetoothLEClientCallBack extends IInterface
{
	public abstract void onConnected(String paramString)
			throws RemoteException;

	public abstract void onDisconnected(String paramString)
			throws RemoteException;

	public abstract void onGetRssiValue(String paramString)
			throws RemoteException;

	public abstract void onLinkLoss()
			throws RemoteException;

	public static abstract class Stub extends Binder
	implements IBluetoothLEClientCallBack
	{
		@SuppressWarnings("unused")
		private static final String DESCRIPTOR = "com.samsung.bluetoothle.IBluetoothLEClientCallBack";
		static final int TRANSACTION_onConnected = 1;
		static final int TRANSACTION_onDisconnected = 2;
		static final int TRANSACTION_onGetRssiValue = 4;
		static final int TRANSACTION_onLinkLoss = 3;

		public Stub()
		{
			attachInterface(this, "com.samsung.bluetoothle.IBluetoothLEClientCallBack");
		}

		public static IBluetoothLEClientCallBack asInterface(IBinder paramIBinder)
		{
			if (paramIBinder == null)
				return null;
			IInterface localIInterface = paramIBinder.queryLocalInterface("com.samsung.bluetoothle.IBluetoothLEClientCallBack");
			if ((localIInterface != null) && ((localIInterface instanceof IBluetoothLEClientCallBack)))
				return (IBluetoothLEClientCallBack)localIInterface;
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
				paramParcel2.writeString("com.samsung.bluetoothle.IBluetoothLEClientCallBack");
				return true;
			case 1:
				paramParcel1.enforceInterface("com.samsung.bluetoothle.IBluetoothLEClientCallBack");
				onConnected(paramParcel1.readString());
				paramParcel2.writeNoException();
				return true;
			case 2:
				paramParcel1.enforceInterface("com.samsung.bluetoothle.IBluetoothLEClientCallBack");
				onDisconnected(paramParcel1.readString());
				paramParcel2.writeNoException();
				return true;
			case 3:
				paramParcel1.enforceInterface("com.samsung.bluetoothle.IBluetoothLEClientCallBack");
				onLinkLoss();
				paramParcel2.writeNoException();
				return true;
			case 4:
			}
			paramParcel1.enforceInterface("com.samsung.bluetoothle.IBluetoothLEClientCallBack");
			onGetRssiValue(paramParcel1.readString());
			paramParcel2.writeNoException();
			return true;
				}

		private static class Proxy
		implements IBluetoothLEClientCallBack
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
				return "com.samsung.bluetoothle.IBluetoothLEClientCallBack";
			}

			public void onConnected(String paramString)
					throws RemoteException
					{
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try
				{
					localParcel1.writeInterfaceToken("com.samsung.bluetoothle.IBluetoothLEClientCallBack");
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

			public void onDisconnected(String paramString)
					throws RemoteException
					{
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try
				{
					localParcel1.writeInterfaceToken("com.samsung.bluetoothle.IBluetoothLEClientCallBack");
					localParcel1.writeString(paramString);
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

			public void onGetRssiValue(String paramString)
					throws RemoteException
					{
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try
				{
					localParcel1.writeInterfaceToken("com.samsung.bluetoothle.IBluetoothLEClientCallBack");
					localParcel1.writeString(paramString);
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

			public void onLinkLoss()
					throws RemoteException
					{
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try
				{
					localParcel1.writeInterfaceToken("com.samsung.bluetoothle.IBluetoothLEClientCallBack");
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

/* Qualified Name:     com.samsung.bluetoothle.IBluetoothLEClientCallBack
 * JD-Core Version:    0.6.2
 */