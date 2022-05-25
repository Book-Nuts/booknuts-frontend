package kr.co.booknuts

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kr.co.booknuts.data.Chat
import kr.co.booknuts.data.DebateCreateDTO
import kr.co.booknuts.data.DebateRoom
import kr.co.booknuts.databinding.ActivityDebateCreateBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DebateCreateActivity : AppCompatActivity() {

    var toggleRatio = -1
    var isOpinion = false
    var userOpinion = false
    val binding by lazy { ActivityDebateCreateBinding.inflate(layoutInflater) }

    // 파이어베이스 데이터베이스 인스턴스 연결
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.getReference()
    // databaseReference.setValue("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // EditText 입력 중 외부 터치 시 키보드 숨기기
        binding.toolbar.setOnClickListener { hideKeyboard() }
        binding.linearDebateCreate.setOnClickListener { hideKeyboard() }
        binding.linearImg.setOnClickListener { hideKeyboard() }

        // X 버튼 클릭 시 액티비티 종료
        binding.btnExit.setOnClickListener { finish() }

        // 음성 채팅 비활성화
        binding.btnToggleVoicechat.setOnClickListener {
            Toast.makeText(this, "데모 버전에서는 텍스트 채팅만 가능합니다.", Toast.LENGTH_SHORT).show()
        }

        // 토론 인원 선택
        binding.btnOneVersesOne.setOnClickListener {
            toggleRatio = 2
            binding.btnOneVersesOne.setTextColor(ContextCompat.getColor(this, R.color.coral_600))
            binding.btnTwoVersesTwo.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
            binding.btnThreeVersesThree.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
            binding.btnFourVersesFour.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
        }
        binding.btnTwoVersesTwo.setOnClickListener {
            toggleRatio = 4
            binding.btnOneVersesOne.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
            binding.btnTwoVersesTwo.setTextColor(ContextCompat.getColor(this, R.color.coral_600))
            binding.btnThreeVersesThree.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
            binding.btnFourVersesFour.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
        }
        binding.btnThreeVersesThree.setOnClickListener {
            binding.btnOneVersesOne.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
            binding.btnTwoVersesTwo.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
            binding.btnThreeVersesThree.setTextColor(ContextCompat.getColor(this, R.color.coral_600))
            binding.btnFourVersesFour.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
            toggleRatio = 6
        }
        binding.btnFourVersesFour.setOnClickListener {
            binding.btnOneVersesOne.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
            binding.btnTwoVersesTwo.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
            binding.btnThreeVersesThree.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
            binding.btnFourVersesFour.setTextColor(ContextCompat.getColor(this, R.color.coral_600))
            toggleRatio = 8
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
            isOpinion = true
            userOpinion = true
            binding.btnTogglePros.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.btnTogglePros.setBackgroundColor(ContextCompat.getColor(this, R.color.coral_600))
            binding.btnToggleCons.setTextColor(ContextCompat.getColor(this, R.color.grey_200))
            binding.btnToggleCons.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        }

        // 만들기 버튼 클릭
        binding.btnCreate.setOnClickListener {
            if (binding.editDebateName.text.isEmpty()) {
                Toast.makeText(this, "토론 이름을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else if (toggleRatio == -1) {
                Toast.makeText(this, "토론 인원을 선택하세요.", Toast.LENGTH_SHORT).show()
            } else if (!isOpinion) {
                Toast.makeText(this, "찬반을 선택하세요.", Toast.LENGTH_SHORT).show()
            } else {
                val bookTitle = "test" // 책 선택
                val author = "test" // 책 선택
                val bookImgUrl = "www.imglink.test" // 책 선택
                val bookGenre = "test" // 책 선택
                val topic = binding.editDebateName.text.toString()
                val coverImgUrl = "www.testurl" // 갤러리
                val type = 0 // 텍스트 채팅
                val maxUser = toggleRatio
                val opinion = userOpinion

                val pref = this.getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
                val token = pref?.getString("Token", "")

                var debateInfo = DebateCreateDTO(bookTitle, author, bookImgUrl, bookGenre, topic, coverImgUrl, type, maxUser, opinion)

                if (token != null) {
                    // 토론장 개설
                    RetrofitBuilder.debateApi.debateCreate(token, debateInfo).enqueue(object : Callback<DebateRoom> {
                        override fun onResponse(call: Call<DebateRoom>, response: Response<DebateRoom>) {
                            Log.d("CREATE_DEBATE", response.body().toString())

                            // 유저 정보 조회해서 닉네임 가져오기
                            val ownerNickname = "test"
                            val roomId = response.body()?.roomId.toString()
                            var ownerOpinion: String = ""
                            if (userOpinion) ownerOpinion = "pros"
                            else ownerOpinion = "cons"

                            // 파이어베이스에도 토론장 정보 추가 후 토론장 액티비티로 이동
                            databaseReference.child(roomId).child("state").setValue(false)
                            databaseReference.child(roomId).child("speaker").setValue(ownerNickname)
                            databaseReference.child(roomId).child("participants").setValue(1)
                            databaseReference.child(roomId).child("user").child(ownerOpinion).setValue(ownerNickname)

                            var intent = Intent(this@DebateCreateActivity, DebateChatActivity::class.java)
                            intent.putExtra("roomId", roomId)
                            startActivity(intent)
                            finish()
                        }

                        override fun onFailure(call: Call<DebateRoom>, t: Throwable) {
                            t.message?.let {
                                Log.e("SIGNUP_E", it)
                                Toast.makeText(this@DebateCreateActivity, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
            }
        }
    }

    // 키보드 비활성화 함수
    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editDebateName.windowToken, 0);
    }
}