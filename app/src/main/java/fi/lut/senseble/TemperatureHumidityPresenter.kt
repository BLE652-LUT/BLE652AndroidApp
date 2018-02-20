package fi.lut.senseble

import android.os.Handler
import android.util.Log
import fi.lut.senseble.bluetoothleservice.BluetoothLeService

/**
 * Created by jessejuuti on 18.2.2018.
 */
class TemperatureHumidityPresenter constructor(private val temperatureHumidityView: TemperatureHumidityView) {

    private val TAG: String = "TempHumidPresenter"
    private val bluetoothLeService: BluetoothLeService = BluetoothLeService
    private var killSwitch: Boolean = false
    private val updateInterval: Long = 1000

    init {
        bluetoothLeService.connectToTemperatureService()
    }

    fun killUpdater() {
        killSwitch = true
    }

    fun getTemperatureValueData() {
        val handler = Handler()
        val runnable = object: Runnable {
            override fun run() {
                if (killSwitch) {
                    Log.d(TAG, "UI Updater killed")
                } else {
                    Log.d(TAG, "UI Updater running")
                    val temperatureValue = bluetoothLeService.readTemperatureServiceValues()
                    temperatureHumidityView.displayBleModuleData(temperatureValue)
                    handler.postDelayed(this, updateInterval)
                }
            }
        }
        runnable.run()
    }
}