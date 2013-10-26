package com.samsung.bluetoothle;

/*
 * This class is obtained from the decompilation of android OS. 
 * Only the function declarations is needed to compile.
 * At runtime the system class will be loaded instead of this dummy class.
 */

import java.util.ArrayList;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

public abstract class BluetoothLEClientProfile
{
//  private static final boolean DEBUG = true;
  public static final int GATT_STATE_CONNECTED = 2;
  public static final int GATT_STATE_CONNECTING = 1;
  public static final int GATT_STATE_DISCONNECTED = 0;
  public static final int GATT_STATE_DISCONNECTING = 3;
//  private static final String TAG = "BluetoothLEClientProfile";
//  private static int mState;
//  private BluetoothLEClientCallback mCallback;
//  private Context mContext;
//  private BluetoothDevice mDevice;
//  private BluetoothLEGattProxy mGattProxy;
//  private final Handler mHandler = new Handler()
//  {
//    public void handleMessage(Message paramAnonymousMessage)
//    {
//      switch (paramAnonymousMessage.what)
//      {
//      case 2:
//      default:
//      case 1:
//      case 3:
//      }
//      String str1;
//      do
//      {
//        String str2;
//        do
//        {
//          return;
//          str2 = (String)paramAnonymousMessage.obj;
//        }
//        while ((str2 == null) || (BluetoothLEClientProfile.this.mCallback == null));
//        BluetoothLEClientProfile.this.mCallback.onConnected(str2);
//        return;
//        str1 = (String)paramAnonymousMessage.obj;
//      }
//      while ((str1 == null) || (BluetoothLEClientProfile.this.mCallback == null));
//      BluetoothLEClientProfile.this.mCallback.onDisconnected(str1);
//    }
//  };
//  private ArrayList<BluetoothLEClientService> mRequiredServices;
//  private ArrayList<BluetoothLEClientService> mServices;

  public BluetoothLEClientProfile(Context paramContext)
  {
//    LogD("BluetoothLEClientProfile", "BluetoothLEClientProfile");
//    this.mContext = paramContext;
//    this.mCallback = new BluetoothLEClientCallback();
//    this.mGattProxy = BluetoothLEGattProxy.getProxy();
//    mState = 0;
  }

  private void LogD(String paramString1, String paramString2)
  {
//    Log.d(paramString1, paramString2);
  }

  public boolean connectLEDevice(BluetoothDevice paramBluetoothDevice)
  {
//    int i = 1;
//    LogD("BluetoothLEClientProfile", "connectLEDevice");
//    this.mDevice = paramBluetoothDevice;
//    mState = i;
//    LogD("BluetoothLEClientProfile", "LE device connection state :" + paramBluetoothDevice.isLEDeviceConnected());
//    if (paramBluetoothDevice.isLEDeviceConnected())
//    {
//      LogD("BluetoothLEClientProfile", "LE device connection state :" + paramBluetoothDevice.isLEDeviceConnected());
//      Message localMessage2 = this.mHandler.obtainMessage(i);
//      localMessage2.obj = paramBluetoothDevice.getAddress();
//      this.mHandler.sendMessage(localMessage2);
//    }
//    boolean bool;
//    do
//    {
//      return i;
//      bool = this.mGattProxy.connect(paramBluetoothDevice);
//      LogD("BluetoothLEClientProfile", "connectCallSuccess::" + bool);
//    }
//    while (bool);
//    Message localMessage1 = this.mHandler.obtainMessage(3);
//    localMessage1.obj = paramBluetoothDevice.getAddress();
//    this.mHandler.sendMessage(localMessage1);
//    return bool;
	  return false;
  }

  public boolean disconnectLEDevice(BluetoothDevice paramBluetoothDevice)
  {
//    LogD("BluetoothLEClientProfile", "disconnectLEDevice");
//    mState = 3;
//    LogD("BluetoothLEClientProfile", "LE device connection state :" + paramBluetoothDevice.isLEDeviceConnected());
//    if (!paramBluetoothDevice.isLEDeviceConnected())
//    {
//      LogD("BluetoothLEClientProfile", "LE device connection state :" + paramBluetoothDevice.isLEDeviceConnected());
//      Message localMessage = this.mHandler.obtainMessage(3);
//      localMessage.obj = paramBluetoothDevice.getAddress();
//      this.mHandler.sendMessage(localMessage);
//      return true;
//    }
//    boolean bool = this.mGattProxy.disconnect(paramBluetoothDevice);
//    LogD("BluetoothLEClientProfile", "disconnectCallSuccess::" + bool);
//    return bool;
	  return false;
  }

  public void discoverCharacteristics(BluetoothDevice paramBluetoothDevice)
  {
//    LogD("BluetoothLEClientProfile", "discoverCharacteristics");
//    if (mState == 2)
//      for (int i = 0; i < this.mRequiredServices.size(); i++)
//        ((BluetoothLEClientService)this.mRequiredServices.get(i)).discoverCharacteristics(paramBluetoothDevice);
  }

  public void finish()
  {
//    LogD("BluetoothLEClientProfile", "finish");
//    mState = 0;
//    this.mServices.clear();
//    this.mRequiredServices.clear();
//    this.mGattProxy = null;
//    this.mCallback = null;
  }

  public BluetoothDevice getConnectedLEDevice()
  {
//    return this.mDevice;
	  return null;
  }

  BluetoothLEGattProxy getGattProxy()
  {
//    return this.mGattProxy;
	  return null;
  }

  public int getLEProfileState()
  {
//    LogD("BluetoothLEClientProfile", "getLEProfileState: " + mState);
//    return mState;
	  return 0;
  }

  public void getRssiValue(BluetoothDevice paramBluetoothDevice)
  {
//    LogD("BluetoothLEClientProfile", "getRssiValue");
//    if (mState == 2)
//      this.mGattProxy.getRssiValue(paramBluetoothDevice);
  }

  public void onDiscoverCharacteristics(BluetoothDevice paramBluetoothDevice)
  {
    LogD("BluetoothLEClientProfile", "onDiscoverCharacteristics");
  }

  public void onGetRssiValue(BluetoothDevice paramBluetoothDevice, String paramString)
  {
    LogD("BluetoothLEClientProfile", "onGetRssiValue");
  }

  public void onLEDeviceConnected(BluetoothDevice paramBluetoothDevice)
  {
    LogD("BluetoothLEClientProfile", "onLEDeviceConnected");
  }

  public void onLEDeviceDisconnected(BluetoothDevice paramBluetoothDevice)
  {
    LogD("BluetoothLEClientProfile", "onLEDeviceDisconnected");
  }

  public void onLELinkLoss(BluetoothDevice paramBluetoothDevice)
  {
    LogD("BluetoothLEClientProfile", "onLELinkLoss");
  }

  public void registerLEProfile(@SuppressWarnings("rawtypes") ArrayList paramArrayList)
  {
//    LogD("BluetoothLEClientProfile", "registerLEProfile");
//    this.mServices = paramArrayList;
//    this.mRequiredServices = new ArrayList();
//    this.mGattProxy.registerLEProfile(this.mCallback);
  }

  public void setRemoteDevice(BluetoothDevice paramBluetoothDevice)
  {
//    LogD("BluetoothLEClientProfile", "setRemoteDevice");
//    this.mDevice = paramBluetoothDevice;
//    this.mGattProxy.setRemoteDeviceAddress(paramBluetoothDevice);
  }

  public void unregisterLEProfile()
  {
//    LogD("BluetoothLEClientProfile", "unregisterLEProfile");
//    if ((mState == 1) || (mState == 2))
//      disconnectLEDevice(this.mDevice);
//    this.mGattProxy.unregisterLEProfile();
  }

  void updateRefreshState(String paramString)
  {
//    LogD("BluetoothLEClientProfile", "updateRefreshState");
//    int i = this.mRequiredServices.size();
//    if (((BluetoothLEClientService)this.mRequiredServices.get(i - 1)).getServicePath().equals(paramString))
//      onDiscoverCharacteristics(this.mDevice);
  }

  @SuppressWarnings("unused")
private class BluetoothLEClientCallback extends IBluetoothLEClientCallBack.Stub
  {
    private static final String TAG = "BluetoothLEClientCallback";

    BluetoothLEClientCallback()
    {
    }

    public void onConnected(String paramString)
    {
//      BluetoothLEClientProfile.this.LogD("BluetoothLEClientCallback", "----- onConnected");
//      BluetoothLEClientProfile.access$202(2);
//      String[] arrayOfString = BluetoothLEClientProfile.this.mDevice.getRemoteServicePaths();
//      BluetoothLEClientProfile.this.mRequiredServices.clear();
//      for (int i = 0; i < arrayOfString.length; i++)
//      {
//        String str = BluetoothLEClientProfile.this.mDevice.getRemoteServiceUUID(arrayOfString[i]);
//        if (str.length() == 4)
//          str = "0000" + str + "-0000-1000-8000-00805f9b34fb";
//        Iterator localIterator = BluetoothLEClientProfile.this.mServices.iterator();
//        while (localIterator.hasNext())
//        {
//          BluetoothLEClientService localBluetoothLEClientService = (BluetoothLEClientService)localIterator.next();
//          if (localBluetoothLEClientService.getServiceUUID().equals(str))
//          {
//            BluetoothLEClientProfile.this.mRequiredServices.add(localBluetoothLEClientService);
//            BluetoothLEClientProfile.this.LogD("BluetoothLEClientCallback", "added to mRequiredServices");
//          }
//        }
//      }
//      for (int j = 0; j < BluetoothLEClientProfile.this.mRequiredServices.size(); j++)
//        ((BluetoothLEClientService)BluetoothLEClientProfile.this.mRequiredServices.get(j)).init(BluetoothLEClientProfile.this.mDevice, BluetoothLEClientProfile.this);
//      BluetoothLEClientProfile.this.onLEDeviceConnected(BluetoothLEClientProfile.this.mDevice);
    }

    public void onDisconnected(String paramString)
    {
//      BluetoothLEClientProfile.this.LogD("BluetoothLEClientCallback", "----- onDisconnected");
//      if (BluetoothLEClientProfile.mState == 0)
//      {
//        BluetoothLEClientProfile.this.LogD("BluetoothLEClientCallback", "already disconnected state, so ignore the signal");
//        return;
//      }
//      BluetoothLEClientProfile.access$202(0);
//      for (int i = 0; i < BluetoothLEClientProfile.this.mRequiredServices.size(); i++)
//        if (((BluetoothLEClientService)BluetoothLEClientProfile.this.mRequiredServices.get(i)).isCharDiscoveryInProgress())
//          ((BluetoothLEClientService)BluetoothLEClientProfile.this.mRequiredServices.get(i)).setCharDiscoveryProgress(false);
//      BluetoothLEClientProfile.this.onLEDeviceDisconnected(BluetoothLEClientProfile.this.mDevice);
    }

    public void onGetRssiValue(String paramString)
    {
//      BluetoothLEClientProfile.this.LogD("BluetoothLEClientCallback", "onGetRssiValue");
//      BluetoothLEClientProfile.this.onGetRssiValue(BluetoothLEClientProfile.this.mDevice, paramString);
    }

    public void onLinkLoss()
    {
//      BluetoothLEClientProfile.this.LogD("BluetoothLEClientCallback", "onLinkLoss");
//      BluetoothLEClientProfile.this.onLELinkLoss(BluetoothLEClientProfile.this.mDevice);
    }
  }
}

/* Qualified Name:     com.samsung.bluetoothle.BluetoothLEClientProfile
 * JD-Core Version:    0.6.2
 */