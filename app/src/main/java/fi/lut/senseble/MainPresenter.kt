package fi.lut.senseble

import android.util.Log
import android.widget.Button

/**
 * Created by jessejuuti on 4.12.2017.
 */
class MainPresenter constructor(mainView: MainView){

    private val TAG: String = "MainPresenter"
    private val mainView = mainView
    private var bleModuleConnected = false

    fun connectDisconnectButtonClicked(buttonClicked: Button) {
        if (checkIfBleModuleConnected()) {
            bleModuleConnected = false
            Log.d(TAG,"Disconnecting BLE Module")
        } else {
            mainView.openConnectionActivity()
            Log.d(TAG,"Opening connection activity!")
        }
    }

    fun menuButtonClicked(buttonClicked: Button) {
        if (!checkIfBleModuleConnected()) {
            mainView.showErrMsgModuleNotConnected()
            return
        } else {
            Log.d(TAG,"New intent should opened! ${buttonClicked}")
        }
    }

    fun checkIfBleModuleConnected(): Boolean {
        return bleModuleConnected
    }
}