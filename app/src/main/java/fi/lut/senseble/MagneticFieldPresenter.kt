package fi.lut.senseble

import android.os.Handler
import android.util.Log
import fi.lut.senseble.bluetoothleservice.BluetoothLeService

/**
 * Created by jessejuuti on 18.2.2018.
 */
class MagneticFieldPresenter constructor(private val magneticFieldView: MagneticFieldView) {

    private val TAG: String = "MagneticFieldPresenter"
    private val bluetoothLeService: BluetoothLeService = BluetoothLeService
    private var killSwitch: Boolean = false
    private val updateInterval: Long = 1000

    init {
        bluetoothLeService.connectToMagnetoMeterServiceX()
        bluetoothLeService.connectToMagnetoMeterServiceY()
        bluetoothLeService.connectToMagnetoMeterServiceZ()
        bluetoothLeService.connectToMagnetoMeterServiceTotal()
    }

    fun getMagneticFieldData() {
        val handler = Handler()
        val runnable = object: Runnable {
            override fun run() {
                if (killSwitch) {
                    Log.d(TAG, "UI Updater killed")
                } else {
                    Log.d(TAG, "UI Updater running")
                    bluetoothLeService.connectToMagnetoMeterServiceX()
                    bluetoothLeService.connectToMagnetoMeterServiceY()
                    bluetoothLeService.connectToMagnetoMeterServiceZ()
                    bluetoothLeService.connectToMagnetoMeterServiceTotal()
                    val values = bluetoothLeService.readMagnetoMeterServiceValues()
                    val MagnXValue = values?.get(0)
                    val MagnYValue = values?.get(1)
                    val MagnZValue = values?.get(2)
                    val MagnTotalValue = values?.get(3)
                    magneticFieldView.displayBleModuleData(MagnXValue, MagnYValue, MagnZValue, MagnTotalValue)
                    handler.postDelayed(this, updateInterval)
                }
            }
        }
        runnable.run()
    }

    fun killUpdater() {
        killSwitch = true
    }
}