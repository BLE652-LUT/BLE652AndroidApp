package fi.lut.senseble

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView

/**
 * Created by jessejuuti on 18.2.2018.
 */
class ModuleStatusActivity: AppCompatActivity(), ModuleStatusView {

    private val TAG: String = "ModuleStatusActivity"
    private lateinit var moduleStatusPresenter: ModuleStatusPresenter
    private lateinit var deviceNameTextView: TextView
    private lateinit var deviceAddressTextView: TextView
    private lateinit var signalStrengthTextView: TextView
    private lateinit var batteryLevelTextView: TextView
    private lateinit var discoveredServicesTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module_status)
        initializePresenter()
        initializeViews()
    }

    override fun onDestroy() {
        moduleStatusPresenter.killUpdater()
        super.onDestroy()
    }

    fun initializePresenter() {
        Log.d(TAG, "Initializing ModuleStatusPresenter")
        moduleStatusPresenter = ModuleStatusPresenter(this@ModuleStatusActivity)
    }

    fun initializeViews() {
        Log.d(TAG,"Initializing ModuleStatusView")
        deviceNameTextView = findViewById(R.id.textview_device_name_data)
        deviceAddressTextView = findViewById(R.id.textview_device_address_data)
        signalStrengthTextView = findViewById(R.id.textview_signal_level_data)
        batteryLevelTextView = findViewById(R.id.textview_battery_level_data)
        discoveredServicesTextView = findViewById(R.id.textview_services_data)
        moduleStatusPresenter.getBleModuleData()

    }

    override fun displayBleModuleData(deviceAddress: String?, deviceName: String?, signalStrength: Int?, discoveredServices: Int?) {
        if (deviceAddress != null) {
            deviceAddressTextView.text = deviceAddress
        }
        if (deviceName != null) {
            deviceNameTextView.text = deviceName
        }
        if (signalStrength != null) {
            signalStrengthTextView.text = signalStrength.toString()
        }
        if (discoveredServices != null) {
            discoveredServicesTextView.text = discoveredServices.toString()
        }
    }

}