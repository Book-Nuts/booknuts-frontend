package kr.co.booknuts.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.booknuts.DebateCreateActivity
import kr.co.booknuts.adapter.DebateRoomAdapter
import kr.co.booknuts.data.DebateListRequestDTO
import kr.co.booknuts.data.DebateRoom
import kr.co.booknuts.data.DebateSearchInfo
import kr.co.booknuts.databinding.FragmentDebateBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DebateFragment : Fragment() {
    val binding by lazy { FragmentDebateBinding.inflate(layoutInflater) }

    var debateStatus: Int = 1 // debateStatus : 1 = 맞춤 토론, 2 = 진행 중, 3 = 대기 중
    var debateType: Int = 2 // debateType : 0 = 텍스트, 1 = 음성, 2 = 전체
    lateinit var recyclerView: RecyclerView

    // 어댑터 변수
    val personalizedAdapter = DebateRoomAdapter()
    val proceedingAdapter = DebateRoomAdapter()
    val waitAdapter = DebateRoomAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentDebateBinding.inflate(inflater, container, false)


        // 리사이클러뷰 어댑터 초기화
        binding.recyclePersonalized.adapter = personalizedAdapter
        binding.recyclePersonalized.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recycleProceeding.adapter = proceedingAdapter
        binding.recycleProceeding.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recycleWait.adapter = waitAdapter
        binding.recycleWait.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // debateStatus : 1 = 맞춤 토론, 2 = 진행 중, 3 = 대기 중
        // debateType : 0 = 텍스트, 1 = 음성, 2 = 전체
        loadData(debateStatus, debateType) // 전체 맞춤 토론

        // 토론장 개설 버튼 클릭
        binding.btnAddDebate.setOnClickListener {
            var intent = Intent(this.activity, DebateCreateActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    // debateStatus : 1 = 맞춤 토론, 2 = 진행 중, 3 = 대기 중
    // debateType : 0 = 텍스트, 1 = 음성, 2 = 전체
    fun loadData(debateSatatus: Int, debateType: Int) {
        val data: MutableList<DebateSearchInfo> = mutableListOf()
        val pref = this.getActivity()?.getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
        val token = pref?.getString("Token", "")

        RetrofitBuilder.debateApi.roomList(token, debateType).enqueue(object : Callback<DebateListRequestDTO> {
            override fun onResponse(call: Call<DebateListRequestDTO>, response: Response<DebateListRequestDTO>) {
                val debateRoomList = response.body()
                var debateData: List<DebateRoom>
                // 토글 버튼에 따라 타입 선택
                when(debateSatatus) {
                    1 -> debateData = debateRoomList?.personalizedDebate!!
                    2 -> debateData = debateRoomList?.proceedingDebate!!
                    3 -> debateData = debateRoomList?.waitDebate!!
                    else -> debateData = emptyList()
                }

                if (debateData != null) {
                    for (d in debateData) {
                        // ★★★ 시간과 파이어베이스에서 참여인원 가져오기 !!!
                        var debateRoom = DebateSearchInfo(d.roomId, d.topic, d.bookTitle, d.coverImgUrl, d.curYesUser, d.curNoUser, "10분", d.owner, 5)
                        Log.d("ROOMLIST_SUCCESS", "${debateSatatus} 타입 : ${debateRoom}")
                        when(debateSatatus) {
                            1 -> personalizedAdapter.listData.add(debateRoom)
                            2 -> proceedingAdapter.listData.add(debateRoom)
                            3 -> waitAdapter.listData.add(debateRoom)
                            else -> Toast.makeText(this@DebateFragment.activity, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<DebateListRequestDTO>, t: Throwable) {
                t.message?.let {
                    Log.e("ROOMLIST_ERROR", it)
                    Toast.makeText(this@DebateFragment.activity, "토론장 조회에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}