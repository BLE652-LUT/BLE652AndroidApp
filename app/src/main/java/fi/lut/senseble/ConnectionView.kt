package fi.lut.senseble

import fi.lut.senseble.bluetoothleservice.BluetoothLeService

/**
 * Created by jessejuuti on 5.12.2017.
 */
interface ConnectionView {

    fun setScanButtonText(bleScannerStatus: Boolean)

    fun populateDeviceList(scanResults: ArrayList<String>)

    fun openMainActivity()

}