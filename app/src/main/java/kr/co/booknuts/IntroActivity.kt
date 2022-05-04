package kr.co.booknuts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import java.util.*
import kotlin.concurrent.timerTask

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val pref = this.getSharedPreferences("authToken", MODE_PRIVATE)

        Toast.makeText(this@IntroActivity, "authToken: " + pref.getString("Token", "Token 없음"), Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java)

        Timer().schedule(timerTask {
            startActivity(intent)
            finish()
        }, 2000)

    }
}