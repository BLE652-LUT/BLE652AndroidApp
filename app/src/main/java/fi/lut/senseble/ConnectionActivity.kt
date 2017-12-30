package fi.lut.senseble


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
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
        scanBleDevicesButton.setOnClickListener {
            connectionPresenter.BleDeviceScan()
        }
    }

    override fun setScanButtonText(bleScannerStatus: Boolean) {
        if (!bleScannerStatus) {
            scanBleDevicesButton.setText(resources.getString(R.string.ble_module_scan_off))
        } else {
            scanBleDevicesButton.setText(resources.getString(R.string.ble_module_scan_on))
        }
    }

    override fun populateDeviceList(scanResults: ArrayList<String>) {
        val arrayAdapter = ArrayAdapter<String>(this@ConnectionActivity, android.R.layout.simple_list_item_1, scanResults)
        val bleDeviceList = findViewById<ListView>(R.id.ble_module_scan_results)
        bleDeviceList.adapter = arrayAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        connectionPresenter.stopBleDeviceScan()
    }

    override fun onDestroy() {
        super.onDestroy()
        connectionPresenter.stopBleDeviceScan()
    }

}