package fi.lut.senseble

import android.os.Handler
import android.util.Log
import fi.lut.senseble.bluetoothleservice.BluetoothLeService

/**
 * Created by jessejuuti on 18.2.2018.
 */
class ModuleStatusPresenter constructor(private val moduleStatusView: ModuleStatusView) {

    private val TAG: String = "ModuleStatusPresenter"
    private val bluetoothLeService: BluetoothLeService = BluetoothLeService
    private var killSwitch: Boolean = false
    private val updateInterval: Long = 1000

    init {
        bluetoothLeService.connectToRSSIService()
    }

    fun getBleModuleData() {
        val handler = Handler()
        val runnable = object: Runnable {
            override fun run() {
                if (killSwitch) {
                    Log.d(TAG, "UI Updater killed")
                } else {
                    Log.d(TAG, "UI Updater running")
                    val deviceAddress = bluetoothLeService.getBleDeviceAddress()
                    val deviceName = bluetoothLeService.getBleDeviceName()
                    val signalStrength = bluetoothLeService.readRssiServiceValues()
                    val discoveredServices = bluetoothLeService.getDiscoveredServices()
                    moduleStatusView.displayBleModuleData(deviceAddress, deviceName, signalStrength, discoveredServices)
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