package kr.co.booknuts.view.adapter.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kr.co.booknuts.view.activity.ChatDetailActivity
import kr.co.booknuts.data.remote.DebateJoinableDTO
import kr.co.booknuts.databinding.FragmentDebateDetail1Binding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DebateDetail1Fragment : Fragment() {

    private val fragmentDetail2 by lazy { DebateDetail2Fragment() }

    val binding by lazy { FragmentDebateDetail1Binding.inflate(layoutInflater) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentDebateDetail1Binding.inflate(inflater, container, false)

        val roomId = arguments?.getInt("roomId")
        val topic = arguments?.getString("topic")
        val title = arguments?.getString("title")
        val author = arguments?.getString("author")
        Log.d("DEBATE_JOINABLE_roomID", roomId.toString())

        binding.textBook.text = "${title} (${author})"
        binding.textTopic.text = topic

        // 반대 버튼 클릭
        binding.btnCons.setOnClickListener {
            if (roomId != null) {
                checkJoinable(roomId.toLong(), false)
            }
        }

        // 찬성 버튼 클릭
        binding.btnPros.setOnClickListener {
            if (roomId != null) {
                checkJoinable(roomId.toLong(), true)
            }
        }

        return binding.root
    }

    fun checkJoinable(roomId: Long, opinion: Boolean){
        var result = false
        val pref = this.getActivity()?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val token = pref?.getString("accessToken", "")
        if (token != null) {
            RetrofitBuilder.debateApi.joinable(token, roomId).enqueue(object: Callback<DebateJoinableDTO> {
                override fun onResponse(call: Call<DebateJoinableDTO>, response: Response<DebateJoinableDTO>) {
                    if (opinion) result = response.body()?.canJoinPros == true
                    else result = response.body()?.canJoinCons == true
                    Log.d("DEBATE_JOINABLE", result.toString())

                    if (result) {
                        // 다음 프래그먼트로 이동
                        // 토론장 id 넘겨주기

                        (activity as ChatDetailActivity).changeFragmentWithData(fragmentDetail2, roomId, opinion);
                    } else {
                        Toast.makeText(this@DebateDetail1Fragment.activity, "인원이 초과되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DebateJoinableDTO>, t: Throwable) {
                    Toast.makeText(this@DebateDetail1Fragment.activity, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        } else Log.d("DEBATE_JOINABLE", "토큰이 존재하지 않습니다.")
    }
}