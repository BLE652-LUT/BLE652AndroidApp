package fi.lut.senseble

/**
 * Created by jessejuuti on 18.2.2018.
 */
interface ModuleStatusView {

    fun displayBleModuleData(deviceAddress: String?, deviceName: String?, signalStrength: Int?, discoveredServices: Int?)

}