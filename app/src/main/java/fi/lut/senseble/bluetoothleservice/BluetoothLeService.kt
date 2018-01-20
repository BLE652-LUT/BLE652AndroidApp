package fi.lut.senseble.bluetoothleservice

import android.bluetooth.*
import android.bluetooth.BluetoothAdapter.STATE_CONNECTED
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import android.util.Log
import java.security.PrivateKey
import kotlin.reflect.KClass

/**
 * Created by jessejuuti on 10.12.2017.
 */

object BluetoothLeService {

    private val TAG = "BluetoothLeService"
    private val STATE_DISCONNECTED = 0
    private val STATE_CONNECTING = 1
    private val STATE_CONNECTED = 2
    lateinit var mBluetoothManager: BluetoothManager
    lateinit var mBluetoothAdapter: BluetoothAdapter
    lateinit var mBluetoothLeScanner: BluetoothLeScanner
    lateinit var mBleDevice: BluetoothDevice
    lateinit var mBluetoothGatt: BluetoothGatt
    var mConnectionStatus: Int = STATE_DISCONNECTED

    fun initializeBleAdapter(context: Context) {
        mBluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = mBluetoothManager.adapter
        mBluetoothLeScanner = mBluetoothAdapter.bluetoothLeScanner
    }

    fun connectBleDevice(mDeviceAddress: String, context: Context) {
        mBleDevice = mBluetoothAdapter.getRemoteDevice(mDeviceAddress)
        mConnectionStatus = STATE_CONNECTING
        mBluetoothGatt = mBleDevice.connectGatt(context, false, mBluetoothGattCallback)
    }

    fun getConnectionStatus(): Int {
        return mConnectionStatus
    }

    object mBluetoothGattCallback : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == STATE_CONNECTED) {
                mConnectionStatus = STATE_CONNECTED
            } else if (newState == 1) {
                mConnectionStatus = STATE_CONNECTING
            } else {
                mConnectionStatus = STATE_DISCONNECTED
            }
            super.onConnectionStateChange(gatt, status, newState)
        }
    }

}


