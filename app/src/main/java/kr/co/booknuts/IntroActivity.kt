package kr.co.booknuts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import java.util.*
import kotlin.concurrent.timerTask

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val intent = Intent(this, LoginActivity::class.java)

        Timer().schedule(timerTask {
            startActivity(intent)
            finish()
        }, 2000)

    }
}