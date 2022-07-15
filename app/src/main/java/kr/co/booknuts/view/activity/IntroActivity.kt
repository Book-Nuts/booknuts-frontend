package kr.co.booknuts.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
        val savedToken = pref.getString("refreshToken", null)
        val editor = pref.edit()
        var responseData: RefreshToken?

        val intentLogin = Intent(this, LoginActivity::class.java)
        val intentMain = Intent(this, MainActivity::class.java)

        Timer().schedule(timerTask {
            if(savedToken != null) {
                RetrofitBuilder.authApi.getTokenRefresh(savedToken).enqueue(object:
                    Callback<RefreshToken> {
                    override fun onResponse(call: Call<RefreshToken>, response: Response<RefreshToken>) {
                        if(response.isSuccessful) {
                            responseData = response.body()
                            Log.d("Token Valid", "" + responseData)
                            editor.putString("refreshToken", responseData?.refreshToken).apply()
                            editor.putString("accessToken", responseData?.accessToken).apply()
                            startActivity(intentMain)
                        } else {
                            if(response.errorBody() != null) {
                                when(response.code()) {
                                    403 -> {
                                        Log.d("Token  Expired", "Both token expired")
                                        startActivity(intentLogin)
                                    }
                                }
                            }
                        }
                    }
                    override fun onFailure(call: Call<RefreshToken>, t: Throwable) {
                        Log.d("API Falied", "Wrong Request")
                        startActivity(intentLogin)
                    }
                })
            } else {
                Log.d("Logout Status", "Login needed")
                startActivity(intentLogin)
            }
            finish()
        }, 2000)

    }
}