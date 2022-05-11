package kr.co.booknuts

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
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
    var isNicknameCorrect: Boolean = false

    val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }

    val checkListener by lazy {
        CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            // 체크박스 선택
            if (isChecked) {
                if (binding.checkTerms1.isChecked && binding.checkTerms2.isChecked && binding.checkTerms3.isChecked && binding.checkTerms4.isChecked) binding.checkEntireTerms.isChecked = true
                when (compoundButton.id) {
                    R.id.check_entireTerms -> {
                        binding.checkTerms1.isChecked = true
                        binding.checkTerms2.isChecked = true
                        binding.checkTerms3.isChecked = true
                        binding.checkTerms4.isChecked = true
                    }
                    R.id.check_terms1 -> binding.textTerms1Type.setTextColor(ContextCompat.getColor(this, R.color.coral_600))
                    R.id.check_terms2 -> binding.textTerms2Type.setTextColor(ContextCompat.getColor(this, R.color.coral_600))
                    R.id.check_terms3 -> binding.textTerms3Type.setTextColor(ContextCompat.getColor(this, R.color.coral_600))
                    R.id.check_terms4 -> binding.textTerms4Type.setTextColor(ContextCompat.getColor(this, R.color.coral_600))
                }
            }
            // 체크박스 해제
            else {
                binding.checkEntireTerms.isChecked = false
                when (compoundButton.id) {
                    R.id.check_entireTerms -> {
                        if (binding.checkTerms1.isChecked && binding.checkTerms2.isChecked && binding.checkTerms3.isChecked && binding.checkTerms4.isChecked) {
                            binding.checkTerms1.isChecked = false
                            binding.checkTerms2.isChecked = false
                            binding.checkTerms3.isChecked = false
                            binding.checkTerms4.isChecked = false
                        }
                    }
                    R.id.check_terms1 -> binding.textTerms1Type.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
                    R.id.check_terms2 -> binding.textTerms2Type.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
                    R.id.check_terms3 -> binding.textTerms3Type.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
                    R.id.check_terms4 -> binding.textTerms4Type.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
                }
            }
            check()
        }
    }

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

        // 이용약관 리스너 연결
        binding.checkEntireTerms.setOnCheckedChangeListener(checkListener)
        binding.checkTerms1.setOnCheckedChangeListener(checkListener)
        binding.checkTerms2.setOnCheckedChangeListener(checkListener)
        binding.checkTerms3.setOnCheckedChangeListener(checkListener)
        binding.checkTerms4.setOnCheckedChangeListener(checkListener)

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
            else {
                // 아이디 중복 확인
                RetrofitBuilder.api.checkId(binding.editId.text.toString()).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        var isIdDup = response.body().toString()
                        Log.d("ID_DUP_D", "response : " + isIdDup)
                        if (isIdDup == "true") {
                            isIdCorrect = false
                            binding.editId.setBackgroundResource(R.drawable.style_info_form_wrong)
                            binding.imgIdStatus.visibility = View.VISIBLE
                            binding.imgIdStatus.setImageResource(R.drawable.icon_wrong)
                            binding.textIdStatus.visibility = View.VISIBLE
                            binding.textIdStatus.text = "이미 사용 중인 아이디입니다.";
                        }
                        // id가 모든 조건을 만족한 경우
                        else {
                            isIdCorrect = true
                            binding.editId.setBackgroundResource(R.drawable.style_info_form_correct)
                            binding.imgIdStatus.visibility = View.VISIBLE
                            binding.imgIdStatus.setImageResource(R.drawable.icon_correct)
                            binding.textIdStatus.visibility = View.GONE
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        t.message?.let {
                            Log.e("NICKNAME_DUP_E", it)
                            Toast.makeText(this@SignUpActivity, "아이디 중복 확인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
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
                binding.textPwdStatus.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
                binding.imgPwdCheck.visibility = View.INVISIBLE
            }
            // 비밀번호를 8자 이상의 영문과 숫자, 특수기호로 조합하지 않은 경우
            else if (!Pattern.matches("^(?=.*\\d)(?=.*[!~@\$])(?=.*[a-zA-Z]).{8,}$", binding.editPwd.text)) {
                isPwdCorrect = false
                binding.editPwd.setBackgroundResource(R.drawable.style_info_form_wrong)
                binding.imgPwdStatus.visibility = View.VISIBLE
                binding.imgPwdStatus.setImageResource(R.drawable.icon_wrong)
                binding.textPwdStatus.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
                binding.imgPwdCheck.visibility = View.INVISIBLE
            }
            // 비밀번호가 모든 조건을 만족한 경우
            else {
                isPwdCorrect = true
                binding.editPwd.setBackgroundResource(R.drawable.style_info_form_correct)
                binding.imgPwdStatus.visibility = View.VISIBLE
                binding.imgPwdStatus.setImageResource(R.drawable.icon_correct)
                binding.textPwdStatus.setTextColor(ContextCompat.getColor(this, R.color.green))
                binding.imgPwdCheck.visibility = View.VISIBLE
            }

            if (!binding.editPwdConfirm.text.toString().equals(binding.editPwd.text.toString()) && !binding.editPwdConfirm.text.isEmpty()) {
                isPwdConfirmCorrect = false
                binding.editPwdConfirm.setBackgroundResource(R.drawable.style_info_form_wrong)
                binding.imgPwdConfirmStatus.visibility = View.VISIBLE
                binding.imgPwdConfirmStatus.setImageResource(R.drawable.icon_wrong)
                binding.textPwdConfirmStatus.text = "비밀번호가 일치하지 않습니다."
                binding.textPwdConfirmStatus.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
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
                binding.textPwdConfirmStatus.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
            }
            // 비밀번호가 일치하지 않은 경우
            else if (!binding.editPwdConfirm.text.toString().equals(binding.editPwd.text.toString())) {
                isPwdConfirmCorrect = false
                binding.editPwdConfirm.setBackgroundResource(R.drawable.style_info_form_wrong)
                binding.imgPwdConfirmStatus.visibility = View.VISIBLE
                binding.imgPwdConfirmStatus.setImageResource(R.drawable.icon_wrong)
                binding.textPwdConfirmStatus.text = "비밀번호가 일치하지 않습니다."
                binding.textPwdConfirmStatus.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
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

        // 닉네임 입력창 변화
        binding.editNickname.addTextChangedListener {
            // 닉네임을 입력하지 않은 경우
            if (binding.editNickname.text.isEmpty()) {
                isNicknameCorrect = false
                binding.editNickname.setBackgroundResource(R.drawable.style_info_form)
                binding.textNicknameStatus.visibility = View.GONE
                binding.imgNicknameStatus.visibility = View.INVISIBLE
            } else {
                // 닉네임 중복 여부 확인
                RetrofitBuilder.api.checkNickname(binding.editNickname.text.toString()).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        var isNicknameDup = response.body().toString()
                        Log.d("NICKNAME_DUP_D", "response : " + isNicknameDup)
                        // 이미 존재하는 닉네임인 경우
                        if(isNicknameDup == "true") {
                            isNicknameCorrect = false
                            binding.editNickname.setBackgroundResource(R.drawable.style_info_form_wrong)
                            binding.textNicknameStatus.visibility = View.VISIBLE
                            binding.imgNicknameStatus.visibility = View.VISIBLE
                            binding.imgNicknameStatus.setImageResource(R.drawable.icon_wrong)
                        }
                        // 사용 가능한 닉네임인 경우
                        else {
                            isNicknameCorrect = true
                            binding.editNickname.setBackgroundResource(R.drawable.style_info_form_correct)
                            binding.textNicknameStatus.visibility = View.GONE
                            binding.imgNicknameStatus.visibility = View.VISIBLE
                            binding.imgNicknameStatus.setImageResource(R.drawable.icon_correct)
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        t.message?.let {
                            Log.e("NICKNAME_DUP_E", it)
                            Toast.makeText(this@SignUpActivity, "닉네임 중복 확인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
            check()
        }
        
        // 가입하기 버튼 클릭
        binding.btnSignup.setOnClickListener {
            var loginId = binding.editId.text.toString().trim()
            var password = binding.editPwd.text.toString().trim()
            var username = binding.editName.text.toString().trim()
            var nickname = binding.editNickname.text.toString().trim()
            var email = binding.editEmail.text.toString().trim()

            var joinInfo = JoinRequestDTO(loginId, password, username, nickname, email)
            var responseUser: User? = null
            RetrofitBuilder.api.createUser(joinInfo).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    responseUser = response.body()
                    Log.d("SIGNUP_D", "response : " + responseUser?.toString())
                    Log.d("SIGNUP_D", "userId : " + responseUser?.userId.toString())
                    Log.d("SIGNUP_D", "loginId : " + responseUser?.loginId.toString())
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
                        Toast.makeText(this@SignUpActivity, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
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

    // 가입 가능 여부 확인 함수
    fun check() {
        if (isIdCorrect && isPwdCorrect && isPwdConfirmCorrect && isNicknameCorrect && !binding.editName.text.isEmpty() && !binding.editEmail.text.isEmpty() && binding.checkTerms1.isChecked && binding.checkTerms2.isChecked) {
            binding.btnSignup.setBackgroundColor(ContextCompat.getColor(this, R.color.coral_500))
            binding.btnSignup.isEnabled = true
        } else {
            binding.btnSignup.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_200))
            binding.btnSignup.isEnabled = false
        }
    }
}