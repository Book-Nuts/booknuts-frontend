package kr.co.booknuts.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kr.co.booknuts.DebateChatActivity
import kr.co.booknuts.R
import kr.co.booknuts.data.DebateJoinDTO
import kr.co.booknuts.data.UserInfo
import kr.co.booknuts.databinding.FragmentDebateDetail2Binding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DebateDetail2Fragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentDebateDetail2Binding.inflate(inflater, container, false)

        // 파이어베이스 데이터베이스 인스턴스 연결
        val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference: DatabaseReference = firebaseDatabase.getReference()

        val roomId = arguments?.getLong("roomId", -1)
        val opinion = arguments?.getBoolean("opinion", false)

        binding.checkBox2.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) {
                val pref = this.getActivity()?.getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
                val token = pref?.getString("Token", "")
                Log.d("DEBATE_JOIN", "Bundle : ${token}, ${roomId}, ${opinion}")

                // 토론장 참여하기
                if (token != null && roomId != null && opinion != null) {
                    RetrofitBuilder.debateApi.join(token, roomId, opinion).enqueue(object : Callback<DebateJoinDTO> {
                        override fun onResponse(call: Call<DebateJoinDTO>, response: Response<DebateJoinDTO>) {
                            Log.d("DEBATE_JOIN", "입장한 토론장 : ${response.body().toString()}")
                            // 파이어베이스에 입장한 사용자 정보 저장 후 토론장으로 이동
                            var userOpinion = ""
                            if (opinion) userOpinion = "pros"
                            else userOpinion = "cons"
                            var nickname = ""
                            // 유저 정보 조회해서 닉네임 가져오기
                            RetrofitBuilder.api.getUserInfo(token).enqueue(object : Callback<UserInfo> {
                                override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                                    nickname = response.body()?.nickname.toString()
                                    Log.d("DEBATE_JOIN", "닉네임 : ${nickname}")
                                }

                                override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                                    Toast.makeText(this@DebateDetail2Fragment.activity, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                                }
                            })
                            var participants = response.body()?.curNoUser
                            participants = participants?.plus(response.body()?.curYesUser!!)
                            if (participants != null) {
                                participants = participants + 1
                            }

                            databaseReference.child(roomId.toString()).child("user").child(userOpinion).setValue(nickname)
                            databaseReference.child(roomId.toString()).child("participants").setValue(participants)

                            var intent = Intent(this@DebateDetail2Fragment.activity, DebateChatActivity::class.java)
                            intent.putExtra("roomId", roomId)
                            startActivity(intent)
                            this@DebateDetail2Fragment.activity?.finish()
                        }

                        override fun onFailure(call: Call<DebateJoinDTO>, t: Throwable) {
                            Toast.makeText(this@DebateDetail2Fragment.activity, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }

        return binding.root
    }


}