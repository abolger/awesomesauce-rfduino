package com.samsung.bluetoothle;

/*
 * This class is obtained from the decompilation of android OS. 
 * Only the function declarations is needed to compile.
 * At runtime the system class will be loaded instead of this dummy class.
 */

import android.bluetooth.BluetoothDevice;

public class BluetoothLEGattProxy
{
//	private static final boolean DBG = true;
//	private static final String TAG = "BluetoothLEGattProxy.LE";
//	private static IBluetooth mBluetoothService;
//	public IBluetoothLEGattCallback mCallback = new IBluetoothLEGattCallback.Stub()
//	{
//		public void onDiscoverCharResult(String paramAnonymousString, String[] paramAnonymousArrayOfString)
//		{
//			String str1 = paramAnonymousString.substring(0, paramAnonymousString.indexOf("/service"));
//			String str2 = BluetoothLEGattProxy.this.getAddressFromObjectPath(str1);
//			IBluetoothLEClientCharUpdationCallBack localIBluetoothLEClientCharUpdationCallBack;
//			if ((str2 != null) && (str2.equals(BluetoothLEGattProxy.this.mRemoteDeviceAddress)))
//			{
//				localIBluetoothLEClientCharUpdationCallBack = (IBluetoothLEClientCharUpdationCallBack)BluetoothLEGattProxy.this.mLEClientServiceCBTracker.get(paramAnonymousString);
//				if (localIBluetoothLEClientCharUpdationCallBack == null);
//			}
//			try
//			{
//				localIBluetoothLEClientCharUpdationCallBack.onDiscoverCharacteristics(str2, paramAnonymousArrayOfString);
//				return;
//			}
//			catch (RemoteException localRemoteException)
//			{
//				Log.e("BluetoothLEGattProxy.LE", "", localRemoteException);
//				Log.e("BluetoothLEGattProxy.LE", "Failed to call onRefresh.", localRemoteException);
//			}
//		}

		public void onGattCharPropertyChanged(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3)
		{
//			BluetoothLEGattProxy.this.LogD("IBluetoothLEGattCallback", "onGattCharPropertyChanged charPath" + paramAnonymousString1);
//			String str1 = paramAnonymousString1.substring(0, paramAnonymousString1.indexOf("/characteristic"));
//			String str2 = paramAnonymousString1.substring(0, paramAnonymousString1.indexOf("/service"));
//			String str3 = BluetoothLEGattProxy.this.getAddressFromObjectPath(str2);
//			if ((str3 != null) && (str3.equals(BluetoothLEGattProxy.this.mRemoteDeviceAddress)))
//			{
//				IBluetoothLEClientCharUpdationCallBack localIBluetoothLEClientCharUpdationCallBack = (IBluetoothLEClientCharUpdationCallBack)BluetoothLEGattProxy.this.mLEClientServiceCBTracker.get(str1);
//				if (localIBluetoothLEClientCharUpdationCallBack != null)
//					try
//				{
//						if (paramAnonymousString2.equals("Value"))
//						{
//							localIBluetoothLEClientCharUpdationCallBack.onWriteCharValue(paramAnonymousString1, paramAnonymousString3);
//							return;
//						}
//						if (paramAnonymousString2.equals("ClientConfiguration"))
//						{
//							localIBluetoothLEClientCharUpdationCallBack.onWriteClientConfigDesc(paramAnonymousString1, paramAnonymousString3);
//							return;
//						}
//				}
//				catch (RemoteException localRemoteException)
//				{
//					Log.e("BluetoothLEGattProxy.LE", "Failed to call onWrite CallBack.", localRemoteException);
//				}
//			}
		}

		public void onGattConnect(String paramAnonymousString)
		{
//			BluetoothLEGattProxy.this.LogD("IBluetoothLEGattCallback", "onConnectGatt");
//			if ((BluetoothLEGattProxy.this.mLEClientProfileCB != null) && (BluetoothLEGattProxy.this.mRemoteDeviceAddress != null) && (BluetoothLEGattProxy.this.mRemoteDeviceAddress.equals(paramAnonymousString)));
//			try
//			{
//				BluetoothLEGattProxy.this.mLEClientProfileCB.onConnected(paramAnonymousString);
//				return;
//			}
//			catch (RemoteException localRemoteException)
//			{
//				Log.e("BluetoothLEGattProxy.LE", "Call to onConnected of LEClientProfile failed", localRemoteException);
//			}
		}

		public void onGattDisconnect(String paramAnonymousString)
		{
//			BluetoothLEGattProxy.this.LogD("IBluetoothLEGattCallback", "onGattDisconnect");
//			if ((BluetoothLEGattProxy.this.mLEClientProfileCB != null) && (paramAnonymousString.equals(BluetoothLEGattProxy.this.mRemoteDeviceAddress)));
//			try
//			{
//				BluetoothLEGattProxy.this.mLEClientProfileCB.onDisconnected(paramAnonymousString);
//				return;
//			}
//			catch (RemoteException localRemoteException)
//			{
//				Log.e("BluetoothLEGattProxy.LE", "Call onDisconnected failed", localRemoteException);
//			}
		}

		public void onGattLinkLoss(String paramAnonymousString)
		{
//			BluetoothLEGattProxy.this.LogD("BluetoothLEGattProxy.LE", "onGattLinkLoss");
//			if ((BluetoothLEGattProxy.this.mLEClientProfileCB != null) && (paramAnonymousString.equals(BluetoothLEGattProxy.this.mRemoteDeviceAddress)));
//			try
//			{
//				BluetoothLEGattProxy.this.mLEClientProfileCB.onLinkLoss();
//				return;
//			}
//			catch (RemoteException localRemoteException)
//			{
//				Log.e("BluetoothLEGattProxy.LE", "Call onGattLinkLoss failed");
//			}
		}

		public void onGattReadCharValue(String paramAnonymousString)
		{
//			BluetoothLEGattProxy.this.LogD("IBluetoothLEGattCallback", "onGattReadCharValue");
		}

		public void onGetRssiValue(String paramAnonymousString1, String paramAnonymousString2)
		{
//			BluetoothLEGattProxy.this.LogD("IBluetoothLEGattCallback", "onGetRssiValue");
//			if ((BluetoothLEGattProxy.this.mLEClientProfileCB != null) && (paramAnonymousString2.equals(BluetoothLEGattProxy.this.mRemoteDeviceAddress)));
//			try
//			{
//				BluetoothLEGattProxy.this.mLEClientProfileCB.onGetRssiValue(paramAnonymousString1);
//				return;
//			}
//			catch (RemoteException localRemoteException)
//			{
//				Log.e("BluetoothLEGattProxy.LE", "Call onGattLinkLoss failed");
//			}
		}

		public void onWatcherValueChanged(String paramAnonymousString1, String paramAnonymousString2)
		{
//			BluetoothLEGattProxy.this.LogD("IBluetoothLEGattCallback", "onWatcherValueChanged");
//			String str1 = paramAnonymousString1.substring(0, paramAnonymousString1.indexOf("/characteristic"));
//			String str2 = paramAnonymousString1.substring(0, paramAnonymousString1.indexOf("/service"));
//			String str3 = BluetoothLEGattProxy.this.getAddressFromObjectPath(str2);
//			IBluetoothLEClientCharUpdationCallBack localIBluetoothLEClientCharUpdationCallBack;
//			if ((str3 != null) && (str3.equals(BluetoothLEGattProxy.this.mRemoteDeviceAddress)))
//			{
//				localIBluetoothLEClientCharUpdationCallBack = (IBluetoothLEClientCharUpdationCallBack)BluetoothLEGattProxy.this.mLEClientServiceCBTracker.get(str1);
//				if (localIBluetoothLEClientCharUpdationCallBack == null);
//			}
//			try
//			{
//				localIBluetoothLEClientCharUpdationCallBack.onWatcherValueChanged(paramAnonymousString1, paramAnonymousString2);
//				return;
//			}
//			catch (RemoteException localRemoteException)
//			{
//				Log.e("BluetoothLEGattProxy.LE", "", localRemoteException);
//				Log.e("BluetoothLEGattProxy.LE", "Failed to call onWatcherValueChanged.", localRemoteException);
//			}
//		}
	};
//	private IBluetoothLEClientCallBack mLEClientProfileCB = null;
//	private final HashMap<String, IBluetoothLEClientCharUpdationCallBack> mLEClientServiceCBTracker;
//	private String mRemoteDeviceAddress = null;

//	private BluetoothLEGattProxy(IBluetooth paramIBluetooth)
//	{
//		LogD("BluetoothLEGattProxy.LE", "BluetoothLEGattProxy Constructor");
//		mBluetoothService = paramIBluetooth;
//		this.mLEClientServiceCBTracker = new HashMap();
//		try
//		{
//			mBluetoothService.registerGattCallback(this.mCallback);
//			return;
//		}
//		catch (RemoteException localRemoteException)
//		{
//			Log.e("BluetoothLEGattProxy.LE", "Failed to initialize/register callbacks with Obex server.", localRemoteException);
//		}
//	}
//
//	private void LogD(String paramString1, String paramString2)
//	{
//		Log.d(paramString1, paramString2);
//	}
//
//	private String getAddressFromObjectPath(String paramString)
//	{
//		LogD("BluetoothLEGattProxy.LE", "getAddressFromObjectPath");
//		try
//		{
//			String str = mBluetoothService.getAddressFromObjectPathForLE(paramString);
//			return str;
//		}
//		catch (RemoteException localRemoteException)
//		{
//			Log.e("BluetoothLEGattProxy.LE", "Failed to call getAddressFromObjectPath on BluetothService.", localRemoteException);
//		}
//		return null;
//	}
//
//	public static BluetoothLEGattProxy getProxy()
//	{
//		IBinder localIBinder;
//		try
//		{
//			if (mBluetoothService != null)
//				break label42;
//			localIBinder = ServiceManager.getService("bluetooth");
//			if (localIBinder == null)
//				throw new RuntimeException("Bluetooth service not available");
//		}
//		finally
//		{
//		}
//		mBluetoothService = IBluetooth.Stub.asInterface(localIBinder);
//		label42: return new BluetoothLEGattProxy(mBluetoothService);
//	}

	public boolean connect(BluetoothDevice paramBluetoothDevice)
	{
//		LogD("BluetoothLEGattProxy.LE", "connect()");
//		this.mRemoteDeviceAddress = paramBluetoothDevice.getAddress();
//		try
//		{
//			boolean bool = mBluetoothService.gattDeviceConnect(this.mRemoteDeviceAddress);
//			return bool;
//		}
//		catch (RemoteException localRemoteException)
//		{
//			Log.e("BluetoothLEGattProxy.LE", "Call connect failed", localRemoteException);
//		}
//		return false;
		
		return false;
	}

	public boolean disconnect(BluetoothDevice paramBluetoothDevice)
	{
//		LogD("BluetoothLEGattProxy.LE", "disconnect()");
//		this.mRemoteDeviceAddress = paramBluetoothDevice.getAddress();
//		try
//		{
//			boolean bool = mBluetoothService.gattDeviceDisconnect(this.mRemoteDeviceAddress);
//			return bool;
//		}
//		catch (RemoteException localRemoteException)
//		{
//			Log.e("BluetoothLEGattProxy.LE", "Call disconnect failed", localRemoteException);
//		}
//		return false;
		
		return false;
	}

	// paramString1 is paramBluetoothDevice.getAddress()
	// paramString2 servicePath
	public boolean gattDiscoveryCharacteristics(String paramString1, String paramString2, String paramString3)
	{
//		LogD("BluetoothLEGattProxy.LE", "gattCharacteristicsDiscovery()");
//		this.mRemoteDeviceAddress = paramString1;
//		try
//		{
//			boolean bool = mBluetoothService.gattDiscoveryCharacteristics(this.mRemoteDeviceAddress, paramString2, paramString3);
//			return bool;
//		}
//		catch (RemoteException localRemoteException)
//		{
//			Log.e("BluetoothLEGattProxy.LE", "Call gattCharacteristicsDiscovery failed", localRemoteException);
//		}
//		return false;
		
		return false;
	}

	public String[] gattGetCharProperties(String paramString1, String paramString2)
	{
//		LogD("BluetoothLEGattProxy.LE", "gattGetCharacteristicsValue() Charpath : " + paramString2);
//		this.mRemoteDeviceAddress = paramString1;
//		try
//		{
//			String[] arrayOfString = mBluetoothService.gattGetCharProperties(paramString1, paramString2);
//			return arrayOfString;
//		}
//		catch (RemoteException localRemoteException)
//		{
//			Log.e("BluetoothLEGattProxy.LE", "Call gattGetCharacteristicsValue failed", localRemoteException);
//		}
//		return null;
		
		return null;
	}

	public void gattSetCharProperty(String paramString1, String paramString2, String paramString3, String paramString4, byte[] paramArrayOfByte)
	{
//		LogD("BluetoothLEGattProxy.LE", "gattCharSetProperty() Charpath : " + paramString3);
//		this.mRemoteDeviceAddress = paramString1;
//		try
//		{
//			mBluetoothService.gattSetCharProperty(paramString1, paramString2, paramString3, paramString4, paramArrayOfByte);
//			return;
//		}
//		catch (RemoteException localRemoteException)
//		{
//			Log.e("BluetoothLEGattProxy.LE", "Call gattCharSetProperty failed", localRemoteException);
//		}
	}

	public void getRssiValue(BluetoothDevice paramBluetoothDevice)
	{
//		LogD("BluetoothLEGattProxy.LE", "getRssiValue()");
//		this.mRemoteDeviceAddress = paramBluetoothDevice.getAddress();
//		try
//		{
//			mBluetoothService.getRssiValue(this.mRemoteDeviceAddress);
//			return;
//		}
//		catch (RemoteException localRemoteException)
//		{
//			Log.e("BluetoothLEGattProxy.LE", "Call getRssiValue failed", localRemoteException);
//		}
	}

	public void registerLEProfile(IBluetoothLEClientCallBack paramIBluetoothLEClientCallBack)
	{
//		LogD("BluetoothLEGattProxy.LE", "registerLEProfile()");
//		this.mLEClientProfileCB = paramIBluetoothLEClientCallBack;
	}

	public void registerLEServiceCallback(String paramString, IBluetoothLEClientCharUpdationCallBack paramIBluetoothLEClientCharUpdationCallBack)
	{
//		Log.e("BluetoothLEGattProxy.LE", "Praveen registerLEServiceCallback");
//		this.mLEClientServiceCBTracker.put(paramString, paramIBluetoothLEClientCharUpdationCallBack);
	}

	public boolean registerWatcher(String paramString)
	{
//		LogD("BluetoothLEGattProxy.LE", "registerWatcher");
//		try
//		{
//			boolean bool = mBluetoothService.gattRegisterCharWatcher(paramString);
//			return bool;
//		}
//		catch (RemoteException localRemoteException)
//		{
//			Log.e("BluetoothLEGattProxy.LE", "Call registerCharacteristicsWatcher failed", localRemoteException);
//		}
//		return false;
		
		return false;
	}

	public void setRemoteDeviceAddress(BluetoothDevice paramBluetoothDevice)
	{
//		LogD("BluetoothLEGattProxy.LE", "setRemoteDeviceAddress()");
//		this.mRemoteDeviceAddress = paramBluetoothDevice.getAddress();
	}

	public void unregisterLEProfile()
	{
//		LogD("BluetoothLEGattProxy.LE", "unregisterLEProfile()");
//		this.mLEClientProfileCB = null;
//		try
//		{
//			mBluetoothService.unregisterGattCallback(this.mCallback);
//			return;
//		}
//		catch (RemoteException localRemoteException)
//		{
//			Log.e("BluetoothLEGattProxy.LE", "Failed to unregister callbacks with Obex server.", localRemoteException);
//		}
	}

	public boolean unregisterWatcher(String paramString)
	{
//		LogD("BluetoothLEGattProxy.LE", "unregisterNotification");
//		try
//		{
//			boolean bool = mBluetoothService.gattUnregisterCharWatcher(paramString);
//			return bool;
//		}
//		catch (RemoteException localRemoteException)
//		{
//			Log.e("BluetoothLEGattProxy.LE", "Call unregisterCharacteristicsWatcher failed", localRemoteException);
//		}
//		return false;
		
		return false;
	}
}

/* Qualified Name:     com.samsung.bluetoothle.BluetoothLEGattProxy
 * JD-Core Version:    0.6.2
 */