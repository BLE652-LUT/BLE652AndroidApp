package fi.lut.senseble

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView

/**
 * Created by jessejuuti on 18.2.2018.
 */
class NoiseActivity: AppCompatActivity(), NoiseView {

    private val TAG: String = "NoiseActivity"
    private lateinit var noisePresenter: NoisePresenter
    private lateinit var noiseLevelTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noise_level)
        initializePresenter()
        initializeView()
    }

    override fun onDestroy() {
        noisePresenter.killUpdater()
        super.onDestroy()
    }

    fun initializePresenter() {
        Log.d(TAG, "Initializing NoisePresenter")
        noisePresenter = NoisePresenter(this@NoiseActivity)
    }

    fun initializeView() {
        Log.d(TAG, "Initializing NoiseView")
        noiseLevelTextView = findViewById(R.id.textview_noise_value_data)
        noisePresenter.getNoiseValueData()
    }

    override fun displayBleModuleData(noiseValue: Float?) {
        noiseLevelTextView.text = noiseValue.toString()
    }
}