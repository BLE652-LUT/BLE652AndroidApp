package fi.lut.senseble

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_connection.*

/**
 * Created by jessejuuti on 5.12.2017.
 */

class ConnectionActivity : AppCompatActivity(), ConnectionView {

    private val TAG: String = "ConnectionActivity"
    private lateinit var connectionPresenter: ConnectionPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection)
        initializePresenter()
        initializeViews()
    }

    fun initializePresenter() {
        Log.d(TAG, "Initializing presenter")
        connectionPresenter = ConnectionPresenter(this@ConnectionActivity, applicationContext)
        connectionPresenter.initializeBleAdapter()
    }

    fun initializeViews() {
        Log.d(TAG, "Initializing view")
        val scanBleDevicesButton = findViewById<Button>(R.id.scanBleDevicesButton)
        val enableBluetoothButton = findViewById<Button>(R.id.enableBluetoothButton)
        scanBleDevicesButton.setOnClickListener {
            connectionPresenter.bleDeviceScan()
        }
        enableBluetoothButton.setOnClickListener {
            connectionPresenter.enableBluetooth()
        }
    }

    override fun setScanButtonText(bleScannerStatus: Boolean) {
        if (!bleScannerStatus) {
            scanBleDevicesButton.setText(R.string.ble_module_scan_off)
        } else {
            scanBleDevicesButton.setText(R.string.ble_module_scan_on)
        }
    }

    override fun populateDeviceList(scanResults: ArrayList<String>) {
        val arrayAdapter = ArrayAdapter<String>(this@ConnectionActivity, R.layout.ble_devices_list_item_1, scanResults)
        val bleDeviceList = findViewById<ListView>(R.id.ble_module_scan_results)
        bleDeviceList.adapter = arrayAdapter
        bleDeviceList.setOnItemClickListener{ adapterView: AdapterView<*>, view: View, itemPosition: Int, itemId: Long ->
            var module = arrayAdapter.getItem(itemPosition)
            Log.d(TAG, "ModuleClicked: ${module}")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        connectionPresenter.stopBleDeviceScan()
    }

    override fun onDestroy() {
        super.onDestroy()
        connectionPresenter.stopBleDeviceScan()
        connectionPresenter.disableBluetooth()
    }

}