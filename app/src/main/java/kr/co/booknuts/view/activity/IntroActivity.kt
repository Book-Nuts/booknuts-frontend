package kr.co.booknuts.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.booknuts.R
import java.util.*
import kotlin.concurrent.timerTask

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val pref = this.getSharedPreferences("authToken", MODE_PRIVATE)
        val savedToken = pref.getString("Token", null)

        // 로그인(토큰) 확인
        val isLogin: Boolean = savedToken != null

        //Toast.makeText(this@IntroActivity, "authToken: " + savedToken, Toast.LENGTH_SHORT).show()

        val intentLogin = Intent(this, LoginActivity::class.java)
        val intentMain = Intent(this, MainActivity::class.java)

        Timer().schedule(timerTask {
            startActivity(if(isLogin) intentMain else intentLogin)
            //startActivity(intentLogin)
            finish()
        }, 2000)

    }
}