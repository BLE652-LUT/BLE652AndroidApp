package fi.lut.senseble

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView

/**
 * Created by jessejuuti on 18.2.2018.
 */
class TemperatureHumidityActivity: AppCompatActivity(), TemperatureHumidityView {

    private val TAG: String = "TempHumidActivity"
    private lateinit var tempHumidPresenter: TemperatureHumidityPresenter
    private lateinit var temperatureValueTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature_humidity)
        initializePresenter()
        initializeView()
    }

    override fun onDestroy() {
        tempHumidPresenter.killUpdater()
        super.onDestroy()
    }

    fun initializePresenter() {
        Log.d(TAG, "Initializing TemperatureHumidityPresenter")
        tempHumidPresenter = TemperatureHumidityPresenter(this@TemperatureHumidityActivity)
    }

    fun initializeView() {
        Log.d(TAG, "Initializing TemperatureHumidityView")
        temperatureValueTextView = findViewById(R.id.textview_temperature_value_data)
        tempHumidPresenter.getTemperatureValueData()
    }

    override fun displayBleModuleData(temperatureValue: Float?) {
        temperatureValueTextView.text = temperatureValue.toString()
    }
}