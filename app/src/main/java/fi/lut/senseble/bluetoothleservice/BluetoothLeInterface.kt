package fi.lut.senseble.bluetoothleservice

/**
 * Created by jessejuuti on 10.12.2017.
 */

interface BluetoothLeInterface {

    fun initializeBtAdapter()

    fun enableBluetooth()

    fun disableBluetooth()

    fun scanBleDevices()

    fun connectBleDevice()

}