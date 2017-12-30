package fi.lut.senseble.bluetoothleservice

import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.Log

/**
 * Created by jessejuuti on 10.12.2017.
 */

class BluetoothLeService: AppCompatActivity() {


    private val TAG: String = "BluetoothLeService"
    private lateinit var bluetoothLeScanner: BluetoothLeScanner

    fun initializeBleAdapter() {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    }

    fun scanBleDevices() {
        bluetoothLeScanner.startScan(object: ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                Log.d(TAG, "onScanResult(): ${result?.device?.address} - ${result?.device?.name}")
            }
        })
    }
}


