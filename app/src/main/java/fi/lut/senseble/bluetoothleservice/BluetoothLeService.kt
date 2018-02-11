package fi.lut.senseble.bluetoothleservice

import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import android.util.Log
import java.util.*
import android.bluetooth.BluetoothGattService


/**
 * Created by jessejuuti on 10.12.2017.
 */

object BluetoothLeService {

    private val TAG = "BluetoothLeService"
    private val STATE_DISCONNECTED = 0
    private val STATE_CONNECTING = 1
    private val STATE_CONNECTED = 2
    private val TEMP_SERVICE_UUID_STRING = "3347AAC0-FB94-11E2-A8E4-F23C91AEC05E"
    private val MAGN_SERVICE_UUID_STRING = "3347BAA0-FB94-11E2-A8E4-F23C91AEC05E"
    private val DESCRIPTOR_CONFIG_UUID = "00002902-0000-1000-8000-00805f9b34fb"
    private lateinit var mBluetoothManager: BluetoothManager
    private lateinit var mBluetoothAdapter: BluetoothAdapter
    private lateinit var mBluetoothLeScanner: BluetoothLeScanner
    private lateinit var mBleDevice: BluetoothDevice
    private lateinit var mBluetoothGatt: BluetoothGatt
    private lateinit var mMagnetoMeterService: BluetoothGattService
    private lateinit var mTemperatureHumidityService: BluetoothGattService
    private lateinit var mMagnetoMeterCharacteristicX: BluetoothGattCharacteristic
    private lateinit var mMagnetoMeterCharacteristicY: BluetoothGattCharacteristic
    private lateinit var mMagnetoMeterCharacteristicZ: BluetoothGattCharacteristic
    private lateinit var mMagnetoMeterCharacteristicTotal: BluetoothGattCharacteristic
    private lateinit var mTemperatureHumidityCharacteristic: BluetoothGattCharacteristic
    private lateinit var mTemperatureHumidityDescriptor: BluetoothGattDescriptor
    private lateinit var mMagnetoMeterDescriptorX: BluetoothGattDescriptor
    private lateinit var mMagnetoMeterDescriptorY: BluetoothGattDescriptor
    private lateinit var mMagnetoMeterDescriptorZ: BluetoothGattDescriptor
    private lateinit var mMagnetoMeterDescriptorTotal: BluetoothGattDescriptor
    private var mMagnetoMeterXConnected: Boolean = false
    private var mMagnetoMeterYConnected: Boolean = false
    private var mMagnetoMeterZConnected: Boolean = false
    private var mMagnetoMeterTotalConnected: Boolean = false
    private var mTemperatureHumidityServiceConnected: Boolean = false

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

    private object mBluetoothGattCallback : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == STATE_CONNECTED) {
                mConnectionStatus = STATE_CONNECTED
                mBluetoothGatt.discoverServices()
            } else if (newState == 1) {
                mConnectionStatus = STATE_CONNECTING
            } else {
                mConnectionStatus = STATE_DISCONNECTED
                mMagnetoMeterXConnected = false
                mMagnetoMeterYConnected = false
                mMagnetoMeterZConnected = false
                mMagnetoMeterTotalConnected = false
            }
            super.onConnectionStateChange(gatt, status, newState)
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onServicesDiscovered status: ${status} and services discovered: ${mBluetoothGatt.services.size}")
                connectToMagnetoMeterService()
                connectToTemperatureService()
            } else {
                Log.d(TAG, "onServicesDiscovered status: ${status}")
            }
            super.onServicesDiscovered(gatt, status)
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            connectToMagnetoMeterService()
            connectToTemperatureService()
            readTemperatureServiceValues()
            readMagnetoMeterServiceValues()
            super.onCharacteristicChanged(gatt, characteristic)
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            Log.d(TAG, "onDescriptorWrite")
            super.onDescriptorWrite(gatt, descriptor, status)
        }

        override fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            Log.d(TAG, "onDescriptorRead")
            super.onDescriptorRead(gatt, descriptor, status)
        }
    }

    private fun connectToMagnetoMeterService() {
        mMagnetoMeterService = mBluetoothGatt.getService(UUID.fromString(MAGN_SERVICE_UUID_STRING))
        if (!mMagnetoMeterXConnected) {
            mMagnetoMeterCharacteristicX = mMagnetoMeterService.characteristics[0]
            Log.d(TAG, "Magnetometer Service Characteristics X: ${mMagnetoMeterCharacteristicX}")
            mBluetoothGatt.setCharacteristicNotification(mMagnetoMeterCharacteristicX, true)
            mMagnetoMeterDescriptorX = mMagnetoMeterCharacteristicX.getDescriptor(UUID.fromString(DESCRIPTOR_CONFIG_UUID))
            mMagnetoMeterDescriptorX.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
            mMagnetoMeterXConnected = mBluetoothGatt.writeDescriptor(mMagnetoMeterDescriptorX)
            Log.d(TAG, "mMagnetoMeterXConnected: ${mMagnetoMeterXConnected}")
        }

        if (!mMagnetoMeterYConnected) {
            mMagnetoMeterCharacteristicY = mMagnetoMeterService.characteristics[1]
            Log.d(TAG, "Magnetometer Service Characteristics Y: ${mMagnetoMeterCharacteristicY}")
            mBluetoothGatt.setCharacteristicNotification(mMagnetoMeterCharacteristicY, true)
            mMagnetoMeterDescriptorY = mMagnetoMeterCharacteristicY.getDescriptor(UUID.fromString(DESCRIPTOR_CONFIG_UUID))
            mMagnetoMeterDescriptorY.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
            mMagnetoMeterYConnected = mBluetoothGatt.writeDescriptor(mMagnetoMeterDescriptorY)
            Log.d(TAG, "mMagnetoMeterYConnected: ${mMagnetoMeterYConnected}")
        }

        if (!mMagnetoMeterZConnected) {
            mMagnetoMeterCharacteristicZ = mMagnetoMeterService.characteristics[2]
            Log.d(TAG, "Magnetometer Service Characteristics Z: ${mMagnetoMeterCharacteristicZ}")
            mBluetoothGatt.setCharacteristicNotification(mMagnetoMeterCharacteristicZ, true)
            mMagnetoMeterDescriptorZ = mMagnetoMeterCharacteristicZ.getDescriptor(UUID.fromString(DESCRIPTOR_CONFIG_UUID))
            mMagnetoMeterDescriptorZ.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
            mMagnetoMeterZConnected = mBluetoothGatt.writeDescriptor(mMagnetoMeterDescriptorZ)
            Log.d(TAG, "mMagnetoMeterZConnected: ${mMagnetoMeterZConnected}")
        }

        if (!mMagnetoMeterTotalConnected) {
            mMagnetoMeterCharacteristicTotal = mMagnetoMeterService.characteristics[3]
            Log.d(TAG, "Magnetometer Service Characteristics Total: ${mMagnetoMeterCharacteristicTotal}")
            mBluetoothGatt.setCharacteristicNotification(mMagnetoMeterCharacteristicTotal, true)
            mMagnetoMeterDescriptorTotal = mMagnetoMeterCharacteristicTotal.getDescriptor(UUID.fromString(DESCRIPTOR_CONFIG_UUID))
            mMagnetoMeterDescriptorTotal.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
            mMagnetoMeterTotalConnected = mBluetoothGatt.writeDescriptor(mMagnetoMeterDescriptorTotal)
            Log.d(TAG, "mMagnetoMeterTotalConnected: ${mMagnetoMeterTotalConnected}")
        }
    }

    private fun connectToTemperatureService() {
        mTemperatureHumidityService = mBluetoothGatt.getService(UUID.fromString(TEMP_SERVICE_UUID_STRING))
        if (!mTemperatureHumidityServiceConnected) {
            mTemperatureHumidityCharacteristic = mTemperatureHumidityService.characteristics[0]
            Log.d(TAG, "TemperatureHumidity Service Charasteristics: ${mTemperatureHumidityCharacteristic}")
            mBluetoothGatt.setCharacteristicNotification(mTemperatureHumidityCharacteristic, true)
            mTemperatureHumidityDescriptor = mTemperatureHumidityCharacteristic.getDescriptor(UUID.fromString(DESCRIPTOR_CONFIG_UUID))
            mTemperatureHumidityDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
            mTemperatureHumidityServiceConnected = mBluetoothGatt.writeDescriptor(mTemperatureHumidityDescriptor)
        }
    }

    private fun readMagnetoMeterServiceValues() {
        if (mMagnetoMeterXConnected) {
            val mMagnXValue = byteArrayToValueConversion(mMagnetoMeterCharacteristicX.value)
            Log.d(TAG,"mMagnXValue: ${mMagnXValue}")
        }
        if (mMagnetoMeterYConnected) {
            val mMagnYValue = byteArrayToValueConversion(mMagnetoMeterCharacteristicY.value)
            Log.d(TAG,"mMagnYValue: ${mMagnYValue}")
        }
        if (mMagnetoMeterZConnected) {
            val mMagnZValue = byteArrayToValueConversion(mMagnetoMeterCharacteristicZ.value)
            Log.d(TAG,"mMagnZValue: ${mMagnZValue}")
        }
        if (mMagnetoMeterTotalConnected) {
            val mMagnTotalValue = byteArrayToValueConversion(mMagnetoMeterCharacteristicTotal.value)
            Log.d(TAG,"mMagnTotalValue: ${mMagnTotalValue}")
        }
    }

    private fun readTemperatureServiceValues() {
        if (mTemperatureHumidityServiceConnected) {
            val mTempValue = byteArrayToValueConversion(mTemperatureHumidityCharacteristic.value)
            val mTempCelsius = (0.1 * mTempValue).toFloat()
            Log.d(TAG, "mTempValue: ${mTempCelsius}")
        }
    }

    private fun byteArrayToValueConversion(mByteArray: ByteArray): Int {
        var mValue = 0
        val mByteHigh = (mByteArray[1].toInt() shl 8) and 0xFF00
        val mByteLow = mByteArray[0].toInt() and 0x00FF
        if (mByteArray[1].toInt() < 0) {
            mValue = (mByteHigh or mByteLow) xor 0xFFFF0000.toInt()
        } else {
            mValue = (mByteHigh or mByteLow)
        }
        return mValue
    }

}


