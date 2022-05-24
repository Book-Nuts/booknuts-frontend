package kr.co.booknuts

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import kr.co.booknuts.databinding.ActivityDebateCreateBinding

class DebateCreateActivity : AppCompatActivity() {

    var toggleRatio = -1
    var isOpinion = false
    var userOpinion = false
    val binding by lazy { ActivityDebateCreateBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // EditText 입력 중 외부 터치 시 키보드 숨기기
        binding.layout.setOnClickListener { hideKeyboard() }
        binding.scrollDebateCreate.setOnClickListener { hideKeyboard() }

        // X 버튼 클릭 시 액티비티 종료
        binding.btnExit.setOnClickListener { finish() }

        // 음성 채팅 비활성화
        binding.btnToggleVoicechat.setOnClickListener {
            Toast.makeText(this, "데모 버전에서는 텍스트 채팅만 가능합니다.", Toast.LENGTH_SHORT).show()
        }

        // 유저 의견 선택
        binding.btnToggleCons.setOnClickListener {
            isOpinion = true
            userOpinion = false
            binding.btnToggleCons.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.btnToggleCons.setBackgroundColor(ContextCompat.getColor(this, R.color.coral_600))
            binding.btnTogglePros.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
            binding.btnTogglePros.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        }
        binding.btnTogglePros.setOnClickListener {
            isOpinion = false
            userOpinion = true
            binding.btnTogglePros.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.btnTogglePros.setBackgroundColor(ContextCompat.getColor(this, R.color.coral_600))
            binding.btnToggleCons.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
            binding.btnToggleCons.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        }

        binding.btnCreate.setOnClickListener {
            if (binding.editDebateName.text.isEmpty()) {
                Toast.makeText(this, "토론 이름을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if (toggleRatio == -1) {
                Toast.makeText(this, "토론 인원을 선택하세요.", Toast.LENGTH_SHORT).show()
            } else if (!isOpinion) {
                Toast.makeText(this, "찬반을 선택하세요.", Toast.LENGTH_SHORT).show()
            } else {
                val bookTitle = ""
                val author = ""
                val bookImgUrl = ""
                val bookGenre = ""
                val topic = binding.editDebateName.text
                val coverImgUrl = ""
                val type = 0 // 텍스트 채팅
                val maxUser = toggleRatio
                val opinion = userOpinion

                var intent = Intent(this, DebateChatActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    // 키보드 비활성화 함수
    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editDebateName.windowToken, 0);
    }
}