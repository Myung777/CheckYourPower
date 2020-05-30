package deepcoding.study.checkyourpower

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private var mMaxPower = 0.0
    private var mIsStart = false
    private var mStartTime = 0L
    private val sensorManager: SensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private val eventListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                if (event.sensor.type != Sensor.TYPE_LINEAR_ACCELERATION) return@let
                val power = event.values[0].toDouble().pow(2.0) + event.values[1].toDouble()
                    .pow(2.0) + event.values[2].toDouble().pow(2.0)

                if (power > 20 && !mIsStart) {
                    mStartTime = System.currentTimeMillis()
                    mIsStart = true
                }

                if (mIsStart) {
                    if (mMaxPower < power) mMaxPower = power

                    stateLabel.text = "파워를 측정하고 있습니다."
                    stateLabel.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.colorRed
                        )
                    )

                    if (System.currentTimeMillis() - mStartTime > 3000) {
                        mIsStart = false
                        checkPowerTestComplete(mMaxPower)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        initGame()
    }

    private fun initGame() {
        mMaxPower = 0.0
        mIsStart = false
        mStartTime = 0L
        stateLabel.text = "핸드폰을 던져요"
        stateLabel.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorBlack))

        sensorManager.registerListener(
            eventListener,
            sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    private fun checkPowerTestComplete(power: Double) {
        sensorManager.unregisterListener(eventListener)
        val intent = Intent(this@MainActivity, ResultActivity::class.java)
        intent.putExtra("power", power)
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        try {
            sensorManager.unregisterListener(eventListener)
        } catch (e: Exception) {
        }
    }
}
