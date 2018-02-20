package fi.lut.senseble

import android.content.Intent
import android.graphics.Color.*
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
    private lateinit var connectDisconnectButton: Button
    private lateinit var bleModuleStatusButton: Button
    private lateinit var magneticFieldButton: Button
    private lateinit var tempHumidButton: Button
    private lateinit var noiseLevelButton: Button

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
        mainPresenter = MainPresenter(this@MainActivity)
    }

    fun initializeViews() {
        Log.d(TAG, "Initializing MainView")
        noiseLevelButton = findViewById(R.id.noise_level_button)
        tempHumidButton = findViewById(R.id.temp_humid_button)
        magneticFieldButton = findViewById(R.id.magnetic_field_button)
        bleModuleStatusButton = findViewById(R.id.ble_module_button)
        connectDisconnectButton = findViewById(R.id.connect_disconnect_button)

        connectDisconnectButton.setOnClickListener {
            mainPresenter.connectDisconnectButtonClicked()
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
            mainPresenter.tempHumidButtonClicked()
            Log.d(TAG, "TempHumidButton clicked")
        }
        noiseLevelButton.setOnClickListener {
            mainPresenter.noiseButtonClicked()
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
        val magneticFieldActivityIntent = Intent(this, MagneticFieldActivity::class.java)
        startActivity(magneticFieldActivityIntent)
    }

    override fun openNoiseActivity() {
        val noiseActivityIntent = Intent(this, NoiseActivity::class.java)
        startActivity(noiseActivityIntent)
    }

    override fun openTempHumidActivity() {
        val tempHumidActivityIntent = Intent(this, TemperatureHumidityActivity::class.java)
        startActivity(tempHumidActivityIntent)
    }

    override fun setConnectionStatus(connectionStatus: Int) {
        val bleModuleConnectionStatus = findViewById<TextView>(R.id.ble_module_connection_status)
        if (connectionStatus == 2) {
            bleModuleConnectionStatus.setText(R.string.ble_module_status_connected)
            bleModuleConnectionStatus.setTextColor(GREEN)
            connectDisconnectButton.setText(resources.getString(R.string.button_status_disconnect))
        } else if (connectionStatus == 1) {
            bleModuleConnectionStatus.setText(R.string.ble_module_status_connecting)
            bleModuleConnectionStatus.setTextColor(YELLOW)
        } else {
            bleModuleConnectionStatus.setText(R.string.ble_module_status_disconnected)
            bleModuleConnectionStatus.setTextColor(RED)
            connectDisconnectButton.setText(resources.getString(R.string.button_status_connect))
        }
    }
}
