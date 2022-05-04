package kr.co.booknuts

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kr.co.booknuts.data.LoginRequestDTO
import kr.co.booknuts.data.Token
import kr.co.booknuts.databinding.ActivityLoginBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val pref = this.getSharedPreferences("authToken", MODE_PRIVATE)
        val editor = pref.edit()

        // 회원가입 textView에 밑줄 추가
        binding.textJoin.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        binding.linearLayout.setOnClickListener{
            hideKeyboard()
        }

        binding.btnLogin.setOnClickListener {
            hideKeyboard()

            var id: String = binding.editId.text.toString()
            var pw: String = binding.editPw.text.toString()
            id = id.trim()
            pw = pw.trim()

            // 아이디 비밀번호가 공백이 아닐 경우 로그인 수행
            if(!id.isEmpty() && !pw.isEmpty()) {
                var loginInfo = LoginRequestDTO(id, pw)

                var responseToken: Token? = null

                RetrofitBuilder.api.doLogin(loginInfo).enqueue(object: Callback<Token> {
                    override fun onResponse(call: Call<Token>, response: Response<Token>) {
                        //Toast.makeText(this@LoginActivity, "통신 성공", Toast.LENGTH_SHORT).show()
                        responseToken = response.body()
                        if(responseToken != null) {
                            Log.d("Login Success", "Token : " + responseToken?.token)
                            editor.putString("Token", responseToken?.token).apply()
                            //var authToken = pref.getString("Token", "Token 없음")?.chunked(15)
                            //(authToken?.get(authToken.size-1) ?: null)
                            Toast.makeText(this@LoginActivity, "Token: " + pref.getString("Token", "Token 없음"), Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("Login", "no user defined")
                            Toast.makeText(this@LoginActivity, "아이디 또는 비밀번호를 잘못 입력했습니다.", Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onFailure(call: Call<Token>, t: Throwable) {
                        Log.d("Approach Fail", "wrong server approach")
                        //Toast.makeText(this@LoginActivity, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this@LoginActivity, "아이디와 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textJoin.setOnClickListener {
            var intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            //Toast.makeText(this@LoginActivity, "회원가입", Toast.LENGTH_SHORT).show()
        }
    }

    // 키보드 내리기
    fun hideKeyboard() {
        val editId = binding.editId
        val editPw = binding.editPw
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editId.windowToken, 0)
        imm.hideSoftInputFromWindow(editPw.windowToken, 0)
    }
}