package fi.lut.senseble

import android.os.Handler
import android.util.Log
import fi.lut.senseble.bluetoothleservice.BluetoothLeService

/**
 * Created by jessejuuti on 18.2.2018.
 */
class NoisePresenter constructor(private val noiseView: NoiseView) {

    private val TAG: String = "NoisePresenter"
    private val bluetoothLeService: BluetoothLeService = BluetoothLeService
    private var killSwitch: Boolean = false
    private val updateInterval: Long = 1000

    init {
        bluetoothLeService.connectToNoiseService()
    }

    fun killUpdater() {
        killSwitch = true
    }

    fun getNoiseValueData() {
        val handler = Handler()
        val runnable = object: Runnable {
            override fun run() {
                if (killSwitch) {
                    Log.d(TAG, "UI Updater killed")
                } else {
                    Log.d(TAG, "UI Updater running")
                    val noiseValue = bluetoothLeService.readNoiseServiceValues()
                    noiseView.displayBleModuleData(noiseValue)
                    handler.postDelayed(this, updateInterval)
                }
            }
        }
        runnable.run()
    }
}