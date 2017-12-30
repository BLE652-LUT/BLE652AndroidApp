package fi.lut.senseble

import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log

/**
 * Created by jessejuuti on 5.12.2017.
 */
class ConnectionPresenter constructor(connectionView: ConnectionView, context: Context){

    private val TAG: String = "ConnectionPresenter"
    private val connectionView = connectionView
    private val context = context
    var bleScannerStatus: Boolean = false
    private lateinit var bluetoothLeScanner: BluetoothLeScanner

    private val bleScanner = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            Log.d("ScanDeviceActivity", "onScanResult(): ${result?.device?.address} - ${result?.device?.name}")
        }
    }

    fun initializeBleAdapter() {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
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