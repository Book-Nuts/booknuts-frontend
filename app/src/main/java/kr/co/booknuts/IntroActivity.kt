package kr.co.booknuts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import java.util.*
import kotlin.concurrent.timerTask

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val pref = this.getSharedPreferences("authToken", MODE_PRIVATE)

        val savedToken = pref.getString("Token", null)

        Toast.makeText(this@IntroActivity, "authToken: " + savedToken, Toast.LENGTH_SHORT).show()

        val intentLogin = Intent(this, LoginActivity::class.java)

        // 로그인(토큰) 확인
        val isLogin: Boolean = savedToken != null

        Timer().schedule(timerTask {
            //startActivity(if(isLogin) intentMain else intentLogin)
            startActivity(intentLogin)
            finish()
        }, 2000)

    }
}