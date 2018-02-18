package fi.lut.senseble

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView

/**
 * Created by jessejuuti on 18.2.2018.
 */
class MagneticFieldActivity: AppCompatActivity(), MagneticFieldView {

    private val TAG: String = "MagneticFieldActivity"
    private lateinit var magneticFieldPresenter: MagneticFieldPresenter
    private lateinit var magnXValueTextView: TextView
    private lateinit var magnYValueTextView: TextView
    private lateinit var magnZValueTextView: TextView
    private lateinit var magnTotalValueTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_magnetic_field)
        initializePresenter()
        initializeView()
    }

    override fun onDestroy() {
        magneticFieldPresenter.killUpdater()
        super.onDestroy()
    }

    fun initializePresenter() {
        Log.d(TAG, "Initializing MagneticFieldPresenter")
        magneticFieldPresenter = MagneticFieldPresenter(this@MagneticFieldActivity)
    }

    fun initializeView() {
        Log.d(TAG, "Initializing MagneticFieldView")
        magnXValueTextView = findViewById(R.id.textview_magnetic_X_axis_data)
        magnYValueTextView = findViewById(R.id.textview_magnetic_Y_axis_data)
        magnZValueTextView = findViewById(R.id.textview_magnetic_Z_axis_data)
        magnTotalValueTextView = findViewById(R.id.textview_magnetic_total_data)
        magneticFieldPresenter.getMagneticFieldData()
    }

    override fun displayBleModuleData(MagnXValue: Int?, MagnYValue: Int?, MagnZValue: Int?, MagnTotalValue: Int?) {
        magnXValueTextView.text = MagnXValue.toString()
        magnYValueTextView.text = MagnYValue.toString()
        magnZValueTextView.text = MagnZValue.toString()
        magnTotalValueTextView.text = MagnTotalValue.toString()
    }
}