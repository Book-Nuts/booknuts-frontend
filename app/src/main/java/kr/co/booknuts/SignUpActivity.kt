package kr.co.booknuts

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import kr.co.booknuts.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
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
}