package kr.co.booknuts

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kr.co.booknuts.adapter.ChatAdapter
import kr.co.booknuts.data.Chat
import kr.co.booknuts.databinding.ActivityDebateChatBinding

class DebateChatActivity : AppCompatActivity() {

    val binding by lazy { ActivityDebateChatBinding.inflate(layoutInflater) }

    // 어댑터 변수
    val adapter = ChatAdapter()

    // 파이어베이스 데이터베이스 인스턴스 연결
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 토론장 ID 받아오기
        val roomId = intent.getStringExtra("roomId")

        // background 적용 및 컴포넌트 앞으로 가져오기
        binding.imgDebateCover.clipToOutline = true
        binding.textTopic.bringToFront()

        // EditText 입력 중 외부 터치 시 키보드 숨기기
        binding.layout.setOnClickListener { hideKeyboard() }
        binding.toolbar.setOnClickListener { hideKeyboard() }
        binding.linearChat.setOnClickListener { hideKeyboard() }

        // X 버튼 클릭 시 액티비티 종료
        binding.btnExit.setOnClickListener { finish() }

        // 토론장 입장
        if (roomId != null) {
            openChat(roomId)
        }

        // 어댑터 연결
        binding.recyclerMsg.adapter = adapter
        binding.recyclerMsg.layoutManager = LinearLayoutManager(this)

        // 전송 버튼 클릭 시 메시지 저장
        binding.btnSend.setOnClickListener {
            if (binding.editChat.text.isEmpty()) {
                Toast.makeText(this, "메시지를 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                // API로 유저 닉네임 가져오기
                val username = "test"
                // 파이어베이스에서 토론장 상태 가져오기
                val state = true
                val message = binding.editChat.text.toString()
                val chat = Chat(username, message, state)

                // 파이어베이스에 메시지 저장
                if (roomId != null) {
                    databaseReference.child(roomId).child("message").push().setValue(chat)
                }

                binding.editChat.setText("")
            }
        }
    }

    fun openChat(roomId: String) {
        // 리사이클러뷰 어댑터 세팅

        // 리스너 관리 : 메시지 받아오기
        databaseReference.child(roomId).child("message").addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chat = snapshot.getValue(Chat::class.java)
                if (chat != null) {
                    adapter.listData.add(chat)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) { }

            override fun onChildRemoved(snapshot: DataSnapshot) { }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }

            override fun onCancelled(error: DatabaseError) { }
        })
    }

    // 키보드 비활성화 함수
    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editChat.windowToken, 0);
    }
}