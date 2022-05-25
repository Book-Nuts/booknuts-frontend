package kr.co.booknuts

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import kr.co.booknuts.databinding.ActivityDebateChatBinding
import kr.co.booknuts.databinding.ActivityDebateCreateBinding

class DebateChatActivity : AppCompatActivity() {

    val binding by lazy { ActivityDebateChatBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // background 적용 및 컴포넌트 앞으로 가져오기
        binding.imgDebateCover.clipToOutline = true
        binding.textTopic.bringToFront()

        // EditText 입력 중 외부 터치 시 키보드 숨기기
        binding.layout.setOnClickListener { hideKeyboard() }
        binding.toolbar.setOnClickListener { hideKeyboard() }

        // X 버튼 클릭 시 액티비티 종료
        binding.btnExit.setOnClickListener { finish() }
    }

    // 키보드 비활성화 함수
    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editChat.windowToken, 0);
    }
}