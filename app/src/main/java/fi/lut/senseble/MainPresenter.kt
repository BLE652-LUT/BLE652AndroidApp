package fi.lut.senseble

import android.content.Context
import fi.lut.senseble.bluetoothleservice.BluetoothLeService
import android.util.Log
import android.widget.Button

/**
 * Created by jessejuuti on 4.12.2017.
 */
class MainPresenter constructor(private var mainView: MainView, private var context: Context){

    private val TAG: String = "MainPresenter"
    private lateinit var bluetoothLeService: BluetoothLeService

    fun connectDisconnectButtonClicked(buttonClicked: Button) {
        if (checkIfBleModuleConnected() == 1) {
            Log.d(TAG,"Disconnecting BLE Module")
        } else {
            mainView.openConnectionActivity()
            Log.d(TAG,"Opening connection activity!")
        }
    }

    fun menuButtonClicked(buttonClicked: Button) {
        if (checkIfBleModuleConnected() != 2) {
            Log.d(TAG,"${buttonClicked.id}")
            mainView.showErrMsgModuleNotConnected()
            return
        } else {
            Log.d(TAG,"New intent should opened! ${buttonClicked}")
        }
    }

    fun bleModuleStatusButtonClicked() {
        if (checkIfBleModuleConnected() == 2) {
            mainView.openModuleStatusActivity()
        } else {
            Log.d(TAG, "BLE Module not connected!")
        }
    }

    fun magneticFieldButtonClicked() {
        if (checkIfBleModuleConnected() == 2) {
            mainView.openMagneticFieldActivity()
        } else {
            Log.d(TAG, "BLE Module not connected!")
        }
    }

    fun checkIfBleModuleConnected(): Int {
        bluetoothLeService = BluetoothLeService
        Log.d(TAG, "Connection Status: ${bluetoothLeService.getConnectionStatus()}")
        mainView.setConnectionStatus(bluetoothLeService.getConnectionStatus())
        return bluetoothLeService.getConnectionStatus()
    }
}