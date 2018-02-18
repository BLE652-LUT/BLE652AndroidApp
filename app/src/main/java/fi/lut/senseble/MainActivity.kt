package fi.lut.senseble

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import android.util.Log
import android.widget.TextView
import android.widget.Toast.LENGTH_SHORT

class MainActivity : AppCompatActivity(), MainView {

    private val TAG: String = "MainActivity"
    private lateinit var mainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializePresenter()
        initializeViews()
    }

    override fun onResume() {
        mainPresenter.checkIfBleModuleConnected()
        super.onResume()
    }

    fun initializePresenter() {
        Log.d(TAG, "Initializing MainPresenter")
        mainPresenter = MainPresenter(this@MainActivity, applicationContext)
    }

    fun initializeViews() {
        Log.d(TAG, "Initializing MainView")
        val connectDisconnectButton = findViewById<Button>(R.id.connect_disconnect_button)
        val bleModuleStatusButton = findViewById<Button>(R.id.ble_module_button)
        val magneticFieldButton = findViewById<Button>(R.id.magnetic_field_button)
        val tempHumidButton = findViewById<Button>(R.id.temp_humid_button)
        val noiseLevelButton = findViewById<Button>(R.id.noise_level_button)
        if (mainPresenter.checkIfBleModuleConnected() == 2) {
            connectDisconnectButton.setText(resources.getString(R.string.button_status_disconnect))
            Log.d(TAG, "BLE Module connected!")
        } else {
            connectDisconnectButton.setText(resources.getString(R.string.button_status_connect))
            Log.d(TAG, "BLE Module disconnected!")
        }
        connectDisconnectButton.setOnClickListener {
            mainPresenter.connectDisconnectButtonClicked(connectDisconnectButton)
            Log.d(TAG, "ConnectDisconnectButton clicked")
        }
        bleModuleStatusButton.setOnClickListener {
            mainPresenter.bleModuleStatusButtonClicked()
            Log.d(TAG, "BleModuleButton clicked")
        }
        magneticFieldButton.setOnClickListener {
            mainPresenter.magneticFieldButtonClicked()
            Log.d(TAG, "MagneticFieldButton clicked")
        }
        tempHumidButton.setOnClickListener {
            mainPresenter.menuButtonClicked(tempHumidButton)
            Log.d(TAG, "TempHumidButton clicked")
        }
        noiseLevelButton.setOnClickListener {
            mainPresenter.menuButtonClicked(noiseLevelButton)
            Log.d(TAG, "NoiseLevelButton clicked")
        }
        mainPresenter.checkIfBleModuleConnected()
    }

    override fun showErrMsgModuleNotConnected() {
        Log.d(TAG, "Showing error message")
        Toast.makeText(this@MainActivity, resources.getString(R.string.ble_module_not_connected), LENGTH_SHORT).show()
    }

    override fun openConnectionActivity() {
        val connectionActivityIntent = Intent(this, ConnectionActivity::class.java)
        startActivity(connectionActivityIntent)
    }

    override fun openModuleStatusActivity() {
        val moduleStatusActivityIntent = Intent(this, ModuleStatusActivity::class.java)
        startActivity(moduleStatusActivityIntent)
    }

    override fun openMagneticFieldActivity() {
        val magneticFieldActivity = Intent(this, MagneticFieldActivity::class.java)
        startActivity(magneticFieldActivity)
    }


    override fun setConnectionStatus(connectionStatus: Int) {
        var bleModuleConnectionStatus = findViewById<TextView>(R.id.ble_module_connection_status)
        if (connectionStatus == 2) {
            bleModuleConnectionStatus.setText(R.string.ble_module_status_connected)
        } else if (connectionStatus == 1) {
            bleModuleConnectionStatus.setText(R.string.ble_module_status_connecting)
        } else {
            bleModuleConnectionStatus.setText(R.string.ble_module_status_disconnected)
        }
    }
}
