package fi.lut.senseble

/**
 * Created by jessejuuti on 4.12.2017.
 */
interface MainView {

    fun showErrMsgModuleNotConnected()

    fun openConnectionActivity()

    fun setConnectionStatus(connectionStatus: Int)

}