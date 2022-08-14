package kr.co.booknuts.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.booknuts.R
import kr.co.booknuts.data.remote.BookSearchInfoKakao
import kr.co.booknuts.data.remote.PostRequestDTO
import kr.co.booknuts.data.remote.RefreshToken
import kr.co.booknuts.retrofit.BookRetrofitBuilder
import kr.co.booknuts.retrofit.RetrofitBuilder
import kr.co.booknuts.view.adapter.BookSearchListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.timerTask

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val pref = this.getSharedPreferences("auth", MODE_PRIVATE)
        val refreshToken = pref.getString("refreshToken", null)

        val intentLogin = Intent(this, LoginActivity::class.java)
        val intentMain = Intent(this, MainActivity::class.java)

        Timer().schedule(timerTask {
            if(refreshToken != null) {
                RetrofitBuilder.authApi.getTokenRefresh(refreshToken).enqueue(object :
                    Callback<RefreshToken> {
                    override fun onResponse(
                        call: Call<RefreshToken>,
                        response: Response<RefreshToken>
                    ) {
                        if (response.isSuccessful) {
                            var responseToken = response.body()
                            pref?.edit()?.putString("accessToken", responseToken?.accessToken)?.apply()
                            Log.d("API Success", "New access token is successfully reissued")
                            startActivity(intentMain)
                            finish()
                        } else {
                            if (response.errorBody() != null) {
                                when (response.code()) {
                                    403 -> {
                                        Log.d("INVALID REFRESH TOKEN", "Login needed")
                                        startActivity(intentLogin)
                                        finish()
                                    }
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<RefreshToken>, t: Throwable) {
                        Log.d("Approach Fail", "wrong server approach")
                    }
                })
            } else {
                Log.d("Logout Status", "Login needed")
                startActivity(intentLogin)
                finish()
            }
        }, 1500)
    }
}