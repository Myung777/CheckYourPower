package deepcoding.study.checkyourpower

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    val power by lazy {
        intent.getDoubleExtra("power", 0.0) * 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        title = "파워 확인"

        scoreLabel.text = "당신의 파워는 ${String.format("%.0f", power)}점 입니다."
        button.setOnClickListener { finish() }
    }
}