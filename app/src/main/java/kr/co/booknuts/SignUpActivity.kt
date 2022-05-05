package kr.co.booknuts

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import kr.co.booknuts.data.JoinRequestDTO
import kr.co.booknuts.data.User
import kr.co.booknuts.databinding.ActivitySignUpBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    var isIdCorrect: Boolean = false
    var isPwdCorrect: Boolean = false
    var isPwdConfirmCorrect: Boolean = false
    // var isNicknameCorrect: Boolean = false

    val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // EditText 입력 중 외부 터치 시 키보드 숨기기
        binding.layoutForm.setOnClickListener {
            hideKeyboard()
        }
        binding.toolbar.setOnClickListener {
            hideKeyboard()
        }

        // X 버튼 앞으로 가져오기
        binding.btnExit.bringToFront()

        // X 버튼 클릭 시 액티비티 종료
        binding.btnExit.setOnClickListener {
            finish()
        }

        // 아이디 입력창 변화
        binding.editId.addTextChangedListener {
            // id를 입력하지 않은 경우
            if (binding.editId.text.isEmpty()) {
                isIdCorrect = false
                binding.editId.setBackgroundResource(R.drawable.style_info_form)
                binding.imgIdStatus.visibility = View.INVISIBLE
                binding.textIdStatus.visibility = View.VISIBLE
                binding.textIdStatus.text = "6자 이상의 영문 혹은 영문과 숫자를 조합해 주세요.";
            }
            // id를 6자 이상의 영문과 숫자로 조합하지 않은 경우
            else if (!Pattern.matches("^[a-zA-Z0-9]{6,}$", binding.editId.text)) {
                isIdCorrect = false
                binding.editId.setBackgroundResource(R.drawable.style_info_form_wrong)
                binding.imgIdStatus.visibility = View.VISIBLE
                binding.imgIdStatus.setImageResource(R.drawable.icon_wrong)
                binding.textIdStatus.visibility = View.VISIBLE
                binding.textIdStatus.text = "6자 이상의 영문 혹은 영문과 숫자를 조합해 주세요.";
            }
            // id가 이미 존재하는 경우 ★★★
            // id가 모든 조건을 만족한 경우
            else {
                isIdCorrect = true
                binding.editId.setBackgroundResource(R.drawable.style_info_form_correct)
                binding.imgIdStatus.visibility = View.VISIBLE
                binding.imgIdStatus.setImageResource(R.drawable.icon_correct)
                binding.textIdStatus.visibility = View.GONE
            }

            check()
        }

        // 비밀번호 입력창 변화
        binding.editPwd.addTextChangedListener {
            // 비밀번호를 입력하지 않은 경우
            if (binding.editPwd.text.isEmpty()) {
                isPwdCorrect = false
                binding.editPwd.setBackgroundResource(R.drawable.style_info_form)
                binding.imgPwdStatus.visibility = View.INVISIBLE
                binding.textPwdStatus.setTextColor(ContextCompat.getColor(this, R.color.grey))
            }
            // 비밀번호를 8자 이상의 영문과 숫자, 특수기호로 조합하지 않은 경우
            else if (!Pattern.matches("^(?=.*\\d)(?=.*[!~@\$])(?=.*[a-zA-Z]).{8,}$", binding.editPwd.text)) {
                isPwdCorrect = false
                binding.editPwd.setBackgroundResource(R.drawable.style_info_form_wrong)
                binding.imgPwdStatus.visibility = View.VISIBLE
                binding.imgPwdStatus.setImageResource(R.drawable.icon_wrong)
                binding.textPwdStatus.setTextColor(ContextCompat.getColor(this, R.color.grey))
            }
            // 비밀번호가 모든 조건을 만족한 경우
            else {
                isPwdCorrect = true
                binding.editPwd.setBackgroundResource(R.drawable.style_info_form_correct)
                binding.imgPwdStatus.visibility = View.VISIBLE
                binding.imgPwdStatus.setImageResource(R.drawable.icon_correct)
                binding.textPwdStatus.setTextColor(ContextCompat.getColor(this, R.color.green))
            }

            if (binding.editPwd.text.toString().equals(binding.editPwdConfirm.text.toString())) {
                isPwdConfirmCorrect = true
                binding.editPwdConfirm.setBackgroundResource(R.drawable.style_info_form_correct)
                binding.imgPwdConfirmStatus.visibility = View.VISIBLE
                binding.imgPwdConfirmStatus.setImageResource(R.drawable.icon_correct)
                binding.textPwdConfirmStatus.text = "비밀번호가 일치합니다."
                binding.textPwdConfirmStatus.setTextColor(ContextCompat.getColor(this, R.color.green))
            }

            if (!binding.editPwdConfirm.text.toString().equals(binding.editPwd.text.toString()) && !binding.editPwdConfirm.text.isEmpty()) {
                isPwdConfirmCorrect = false
                binding.editPwdConfirm.setBackgroundResource(R.drawable.style_info_form_wrong)
                binding.imgPwdConfirmStatus.visibility = View.VISIBLE
                binding.imgPwdConfirmStatus.setImageResource(R.drawable.icon_wrong)
                binding.textPwdConfirmStatus.text = "비밀번호가 일치하지 않습니다."
                binding.textPwdConfirmStatus.setTextColor(ContextCompat.getColor(this, R.color.grey))
            }
            else if (!binding.editPwdConfirm.text.isEmpty()){
                isPwdConfirmCorrect = true
                binding.editPwdConfirm.setBackgroundResource(R.drawable.style_info_form_correct)
                binding.imgPwdConfirmStatus.visibility = View.VISIBLE
                binding.imgPwdConfirmStatus.setImageResource(R.drawable.icon_correct)
                binding.textPwdConfirmStatus.text = "비밀번호가 일치합니다."
                binding.textPwdConfirmStatus.setTextColor(ContextCompat.getColor(this, R.color.green))
            }
            check()
        }

        // 비밀번호 확인 입력창 변화
        binding.editPwdConfirm.addTextChangedListener {
            // 입력하지 않은 경우
            if (binding.editPwdConfirm.text.isEmpty()) {
                isPwdConfirmCorrect = false
                binding.editPwdConfirm.setBackgroundResource(R.drawable.style_info_form)
                binding.imgPwdConfirmStatus.visibility = View.INVISIBLE
                binding.textPwdConfirmStatus.text = "비밀번호가 일치하지 않습니다."
                binding.textPwdConfirmStatus.setTextColor(ContextCompat.getColor(this, R.color.grey))
            }
            // 비밀번호가 일치하지 않은 경우
            else if (!binding.editPwdConfirm.text.toString().equals(binding.editPwd.text.toString())) {
                isPwdConfirmCorrect = false
                binding.editPwdConfirm.setBackgroundResource(R.drawable.style_info_form_wrong)
                binding.imgPwdConfirmStatus.visibility = View.VISIBLE
                binding.imgPwdConfirmStatus.setImageResource(R.drawable.icon_wrong)
                binding.textPwdConfirmStatus.text = "비밀번호가 일치하지 않습니다."
                binding.textPwdConfirmStatus.setTextColor(ContextCompat.getColor(this, R.color.grey))
            }
            // 비밀번호가 일치하는 경우
            else {
                isPwdConfirmCorrect = true
                binding.editPwdConfirm.setBackgroundResource(R.drawable.style_info_form_correct)
                binding.imgPwdConfirmStatus.visibility = View.VISIBLE
                binding.imgPwdConfirmStatus.setImageResource(R.drawable.icon_correct)
                binding.textPwdConfirmStatus.text = "비밀번호가 일치합니다."
                binding.textPwdConfirmStatus.setTextColor(ContextCompat.getColor(this, R.color.green))
            }
            check()
        }

        binding.editName.addTextChangedListener {
            check()
        }
        binding.editNickname.addTextChangedListener {
            check()
        }
        // 닉네임 중복 여부 확인 ★★★
        binding.editNickname.addTextChangedListener {
            check()
        }

        // 가입하기 버튼 클릭
        binding.btnSignup.setOnClickListener {
            var userId = binding.editId.text.toString().trim()
            var password = binding.editPwd.text.toString().trim()
            var username = binding.editName.text.toString().trim()
            var nickname = binding.editNickname.text.toString().trim()
            var email = binding.editEmail.text.toString().trim()
            // 필수 입력 정보 중 비어있는 입력창이 있는지 확인
            if (!userId.isEmpty() && !password.isEmpty() && !username.isEmpty() && !nickname.isEmpty() && !email.isEmpty()) {
                // 비밀번호 재입력 확인 ★★★
                // 확인해야하는 정보들 모두 확인 ★★★

                var joinInfo = JoinRequestDTO(userId, password, username, nickname, email)
                var responseUser: User? = null
                RetrofitBuilder.api.createUser(joinInfo).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        responseUser = response.body()
                        Log.d("SIGNUP_D", "response : " + responseUser?.toString())
                        Log.d("SIGNUP_D", "id : " + responseUser?.id.toString())
                        Log.d("SIGNUP_D", "userId : " + responseUser?.userId.toString())
                        Log.d("SIGNUP_D", "username : " + responseUser?.username.toString())
                        Log.d("SIGNUP_D", "nickname : " + responseUser?.nickname.toString())
                        Log.d("SIGNUP_D", "email : " + responseUser?.email.toString())
                        Log.d("SIGNUP_D", "accesToken : " + responseUser?.accessToken.toString())
                        Log.d("SIGNUP_D", "roles : " + responseUser?.roles.toString())

                        Toast.makeText(this@SignUpActivity, "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        t.message?.let {
                            Log.e("SIGNUP_E", it)
                            Toast.makeText(this@SignUpActivity, it, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }

    // 키보드 비활성화 함수
    fun hideKeyboard() {
        val edit1 = binding.editId
        val edit2 = binding.editEmail
        val edit3 = binding.editName
        val edit4 = binding.editNickname
        val edit5 = binding.editPwd
        val edit6 = binding.editPwdConfirm
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edit1.windowToken, 0);
        imm.hideSoftInputFromWindow(edit2.windowToken, 0);
        imm.hideSoftInputFromWindow(edit3.windowToken, 0);
        imm.hideSoftInputFromWindow(edit4.windowToken, 0);
        imm.hideSoftInputFromWindow(edit5.windowToken, 0);
        imm.hideSoftInputFromWindow(edit6.windowToken, 0);
    }

    fun check() {
        // 닉네임 변수 수정 ★★★
        if (isIdCorrect && isPwdCorrect && isPwdConfirmCorrect && !binding.editName.text.isEmpty() && !binding.editEmail.text.isEmpty() && !binding.editNickname.text.isEmpty()) {
            binding.btnSignup.setBackgroundColor(ContextCompat.getColor(this, R.color.main_red))
            binding.btnSignup.isClickable = true
        } else {
            binding.btnSignup.setBackgroundColor(ContextCompat.getColor(this, R.color.grey))
            binding.btnSignup.isClickable = false
        }
    }
}