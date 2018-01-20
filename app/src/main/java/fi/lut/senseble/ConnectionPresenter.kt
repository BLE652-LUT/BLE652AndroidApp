package fi.lut.senseble

import fi.lut.senseble.bluetoothleservice.BluetoothLeService
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import android.os.Handler

/**
 * Created by jessejuuti on 5.12.2017.
 */
class ConnectionPresenter constructor(private var connectionView: ConnectionView, private var context: Context){

    private val TAG: String = "ConnectionPresenter"
    private var bleScannerStatus: Boolean = false
    private var scanResults: ArrayList<String> = ArrayList()
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothLeScanner: BluetoothLeScanner
    private lateinit var bluetoothLeService: BluetoothLeService

    private val scanPeriod: Long = 10000

    fun initializeBleAdapter() {
        bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
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

    fun clearScanResults() {
        scanResults.clear()
        connectionView.populateDeviceList(scanResults)
    }

    fun enableBluetooth() {
        if (!bluetoothAdapter.isEnabled) {
            bluetoothAdapter.enable()
        }
    }

    fun disableBluetooth() {
        if (bluetoothAdapter.isEnabled) {
            bluetoothAdapter.disable()
        }
    }

    fun bleDeviceScan() {
        if (bleScannerStatus) {
            stopBleDeviceScan()
        } else {
            clearScanResults()
            startBleDeviceScan()
        }
    }

    fun startBleDeviceScan() {
        enableBluetooth()
        val handler = Handler()
        val runnable = Runnable {
            stopBleDeviceScan()
        }
        handler.postDelayed(runnable, scanPeriod)
        bleScannerStatus = true
        bluetoothLeScanner.startScan(bleScanner)
        connectionView.setScanButtonText(bleScannerStatus)
    }

    fun stopBleDeviceScan() {
        if (bleScannerStatus == true) {
            bluetoothLeScanner.stopScan(bleScanner)
            bleScannerStatus = false
            connectionView.setScanButtonText(bleScannerStatus)
        }
    }

    fun initializeConnection(deviceMacAddress:String) {
        bluetoothLeService = BluetoothLeService
        bluetoothLeService.initializeBleAdapter(context)
        bluetoothLeService.connectBleDevice(deviceMacAddress, context)
        stopBleDeviceScan()
        connectionView.openMainActivity()
    }

}
