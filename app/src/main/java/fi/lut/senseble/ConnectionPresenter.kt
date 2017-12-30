package fi.lut.senseble

import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import android.widget.ListView

/**
 * Created by jessejuuti on 5.12.2017.
 */
class ConnectionPresenter constructor(connectionView: ConnectionView, context: Context){

    private val TAG: String = "ConnectionPresenter"
    private val connectionView = connectionView
    private val context = context
    private var bleScannerStatus: Boolean = false
    private var scanResults: ArrayList<String> = ArrayList()
    private lateinit var bluetoothLeScanner: BluetoothLeScanner

    fun initializeBleAdapter() {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    }

    private val bleScanner = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            var deviceName = result?.device?.name
            var deviceUuids = result?.device?.uuids
            var deviceAddress = result?.device?.address
            Log.d(TAG, "onScanResult(): ${deviceAddress} - ${deviceName} - ${deviceUuids}")
            if (!scanResults.contains("${deviceAddress} - ${deviceName} - ${deviceUuids}")) {
                scanResults.add("${deviceAddress} - ${deviceName} - ${deviceUuids}")
                connectionView.populateDeviceList(scanResults)
            } else {
                Log.d(TAG, "Device ${deviceAddress} - ${deviceName} - ${deviceUuids} already listed!")
            }
        }
    }

    fun BleDeviceScan() {
        if (bleScannerStatus) {
            stopBleDeviceScan()
        } else {
            scanResults.clear()
            startBleDeviceScan()
        }
    }

    fun startBleDeviceScan() {
        if (bleScannerStatus == false) {
            bluetoothLeScanner.startScan(bleScanner)
            bleScannerStatus = true
            connectionView.setScanButtonText(bleScannerStatus)
        }
    }

    fun stopBleDeviceScan() {
        if (bleScannerStatus == true) {
            bluetoothLeScanner.stopScan(bleScanner)
            bleScannerStatus = false
            connectionView.setScanButtonText(bleScannerStatus)
        }
    }
}