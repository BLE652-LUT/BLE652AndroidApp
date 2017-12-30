package fi.lut.senseble

/**
 * Created by jessejuuti on 5.12.2017.
 */
interface ConnectionView {

    fun setScanButtonText(bleScannerStatus: Boolean)

    fun populateDeviceList(scanResults: ArrayList<String>)

}