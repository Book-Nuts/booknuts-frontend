package kr.co.booknuts.view

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kr.co.booknuts.data.remote.RefreshToken
import kr.co.booknuts.retrofit.RetrofitBuilder
import kr.co.booknuts.view.activity.LoginActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object CommonMethod {
    var genres = arrayOf("자기계발", "매거진", "현대문학", "경제경영", "고전문학", "세계문학", "라이프스타일", "자녀교육", "어린이/청소년", "인문사회", "과학기술", "판타지/무협", "로맨스/BL", "독립서적")
    // 키보드 내리기
    fun hideKeyboard(editText: EditText, context: Context) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    // 2개 키보드 숨길 때
    fun hideKeyboards(editText1: EditText, editText2: EditText, context: Context) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText1.windowToken, 0)
        imm.hideSoftInputFromWindow(editText2.windowToken, 0)
    }

    // 토큰 에러 처리
    fun controlTokenError(
        responseErrorBody: ResponseBody?,
        responseCode: Int,
        context: Context,
        activity: FragmentActivity?,
        requireActivity: FragmentActivity
    ): Boolean {
        var isReissued = false
        val pref = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val refreshToken = pref?.getString("refreshToken", null)
        if (responseErrorBody != null) {
            when (responseCode) {
                401 -> {
                    RetrofitBuilder.authApi.getTokenRefresh(refreshToken).enqueue(object :
                        Callback<RefreshToken> {
                        override fun onResponse(
                            call: Call<RefreshToken>,
                            response: Response<RefreshToken>
                        ) {
                            if (response.isSuccessful) {
                                val editor = pref?.edit()
                                var responseToken = response.body()
                                editor?.putString("accessToken", responseToken?.accessToken)
                                    ?.apply()
                                Log.d("API Success", "New access token is successfully reissued")
                                isReissued = true
                            } else {
                                if (response.errorBody() != null) {
                                    when (response.code()) {
                                        403 -> {
                                            Log.d("Refresh Token Error", "Wrong refresh token")
                                            val intent = Intent(activity, LoginActivity::class.java)
                                            activity?.startActivity(intent)
                                            ActivityCompat.finishAffinity(requireActivity)
                                        }
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<RefreshToken>, t: Throwable) {
                            Log.d("Approach Fail", "wrong server approach")
                        }
                    })
                }
                403 -> {
                    Log.d("Token Error", "Wrong token")
                    val intent = Intent(activity, LoginActivity::class.java)
                    activity?.startActivity(intent)
                    ActivityCompat.finishAffinity(requireActivity)
                }
            }
        }

        return isReissued
    }

    // 프래그먼트 종료
    fun closeFragment(activity: FragmentActivity?, fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()?.remove(fragment)?.commit()
    }
}