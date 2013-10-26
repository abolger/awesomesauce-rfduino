package com.samsung.bluetoothle;

/*
 * This class is obtained from the decompilation of android OS. 
 * Only the function declarations is needed to compile.
 * At runtime the system class will be loaded instead of this dummy class.
 */

public final class BluetoothLENamespace
{
  public static String toUuid128StringFormat(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder("0000");
    localStringBuilder.append(Integer.toHexString(paramInt));
    localStringBuilder.append("-0000-1000-8000-00805f9b34fb");
    return localStringBuilder.toString();
  }

  public static String toUuid16StringFormat(int paramInt)
  {
    return Integer.toHexString(paramInt);
  }

  public static final class Characteristics
  {
    public static final int AlertLevel = 10758;
    public static final int BodySensorLocation = 10808;
    public static final int FirmwareRevision = 10790;
    public static final int HardwareRevision = 10791;
    public static final int HeartRateControlPoint = 10809;
    public static final int HeartRateMeasurement = 10807;
    public static final int IEEE_11073_20601_RegulatoryCertificationDataList = 10794;
    public static final int ManufactureName = 10793;
    public static final int ModelNumber = 10788;
    public static final int SerialNumber = 10789;
    public static final int SoftwareRevision = 10792;
    public static final int SystemId = 10787;
    public static final int TxPowerLevel = 10759;
  }

  public static final class Services
  {
    public static final int AlertNotificationService = 6161;
    public static final int BatteryService = 6159;
    public static final int BloodPressure = 6160;
    public static final int CurrentTimeService = 6149;
    public static final int DeviceInformationService = 6154;
    public static final int GenericAccess = 6144;
    public static final int GenericAttribute = 6145;
    public static final int HealthThermometer = 6153;
    public static final int HeartRate = 6157;
    public static final int HumanInterfaceDevice = 6162;
    public static final int ImmediateAlert = 6146;
    public static final int LinkLoss = 6147;
    public static final int NextDSTChangeService = 6151;
    public static final int PhoneAlertStatusService = 6158;
    public static final int ReferenceTimeUpdateService = 6150;
    public static final int ScanParameters = 6163;
    public static final int TxPower = 6148;
  }
}

/* Qualified Name:     com.samsung.bluetoothle.BluetoothLENamespace
 * JD-Core Version:    0.6.2
 */