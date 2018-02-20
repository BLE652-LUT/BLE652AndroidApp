package fi.lut.senseble

import fi.lut.senseble.bluetoothleservice.BluetoothLeService
import android.util.Log
import android.widget.Button

/**
 * Created by jessejuuti on 4.12.2017.
 */
class MainPresenter constructor(private val mainView: MainView){

    private val TAG: String = "MainPresenter"
    private val bluetoothLeService: BluetoothLeService = BluetoothLeService

    fun connectDisconnectButtonClicked() {
        if (checkIfBleModuleConnected() == 2) {
            Log.d(TAG,"Disconnecting BLE Module")
            bluetoothLeService.disconnectBleDevice()
        } else {
            mainView.openConnectionActivity()
            Log.d(TAG,"Opening connection activity!")
        }
    }

    fun bleModuleStatusButtonClicked() {
        if (checkIfBleModuleConnected() == 2) {
            mainView.openModuleStatusActivity()
        } else {
            Log.d(TAG, "BLE Module not connected!")
            mainView.showErrMsgModuleNotConnected()
        }
    }

    fun magneticFieldButtonClicked() {
        if (checkIfBleModuleConnected() == 2) {
            mainView.openMagneticFieldActivity()
        } else {
            Log.d(TAG, "BLE Module not connected!")
            mainView.showErrMsgModuleNotConnected()
        }
    }

    fun tempHumidButtonClicked() {
        if (checkIfBleModuleConnected() == 2) {
            mainView.openTempHumidActivity()
        } else {
            Log.d(TAG, "BLE Module not connected!")
            mainView.showErrMsgModuleNotConnected()
        }
    }

    fun noiseButtonClicked() {
        if (checkIfBleModuleConnected() == 2) {
            mainView.openNoiseActivity()
        } else {
            Log.d(TAG, "BLE Module not connected!")
            mainView.showErrMsgModuleNotConnected()
        }
    }

    fun checkIfBleModuleConnected(): Int {
        Log.d(TAG, "Connection Status: ${bluetoothLeService.getConnectionStatus()}")
        mainView.setConnectionStatus(bluetoothLeService.getConnectionStatus())
        return bluetoothLeService.getConnectionStatus()
    }
}