package fi.lut.senseble

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.util.Log
import android.widget.Toast.LENGTH_SHORT

class MainActivity : Activity(), MainView {

    private val TAG: String = "MainActivity"
    private lateinit var mainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializePresenter()
        initializeViews()
    }

    fun initializePresenter() {
        Log.d(TAG, "Initializing presenter")
        mainPresenter = MainPresenter(this@MainActivity)
    }

    fun initializeViews() {
        Log.d(TAG, "Initializing view")
        val connectDisconnectButton = findViewById<Button>(R.id.connect_disconnect_button)
        val bleModuleStatusButton = findViewById<Button>(R.id.ble_module_button)
        val magneticFieldButton = findViewById<Button>(R.id.magnetic_field_button)
        val tempHumidButton = findViewById<Button>(R.id.temp_humid_button)
        val noiseLevelButton = findViewById<Button>(R.id.noise_level_button)
        if (mainPresenter.checkIfBleModuleConnected()) {
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
            mainPresenter.menuButtonClicked(bleModuleStatusButton)
            Log.d(TAG, "BleModuleButton clicked")
        }
        magneticFieldButton.setOnClickListener {
            mainPresenter.menuButtonClicked(magneticFieldButton)
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
    }

    override fun showErrMsgModuleNotConnected() {
        Log.d(TAG, "Showing error message")
        Toast.makeText(this@MainActivity, resources.getString(R.string.ble_module_not_connected), LENGTH_SHORT).show()
    }

    override fun openConnectionActivity() {
        val connectionActivityIntent = Intent(this, ConnectionActivity::class.java)
        startActivity(connectionActivityIntent)
    }
}
