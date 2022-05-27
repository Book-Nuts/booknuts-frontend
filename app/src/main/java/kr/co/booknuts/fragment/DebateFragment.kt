package kr.co.booknuts.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kr.co.booknuts.DebateCreateActivity
import kr.co.booknuts.R
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

    var debateType: Int = 2 // debateType : 0 = 텍스트, 1 = 음성, 2 = 전체
    lateinit var recyclerView: RecyclerView

    // 파이어베이스 데이터베이스 인스턴스 연결
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.getReference()

    // 어댑터 변수
    val personalizedAdapter = DebateRoomAdapter()
    val proceedingAdapter = DebateRoomAdapter()
    val waitAdapter = DebateRoomAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = FragmentDebateBinding.inflate(inflater, container, false)

        // 리사이클러뷰 어댑터, 레이아웃 매니저 초기화
        binding.recyclePersonalized.adapter = personalizedAdapter
        binding.recyclePersonalized.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recycleProceeding.adapter = proceedingAdapter
        binding.recycleProceeding.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recycleWait.adapter = waitAdapter
        binding.recycleWait.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // debateType : 0 = 텍스트, 1 = 음성, 2 = 전체
        loadData(debateType) // 전체 맞춤 토론

        // refresh 실행
        binding.swipe.setOnRefreshListener{
            // 리사이클러뷰 어댑터, 레이아웃 매니저 초기화
            binding.recyclePersonalized.adapter = personalizedAdapter
            binding.recyclePersonalized.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.recycleProceeding.adapter = proceedingAdapter
            binding.recycleProceeding.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.recycleWait.adapter = waitAdapter
            binding.recycleWait.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            loadData(debateType)
            binding.swipe.isRefreshing = false
        }

        // 토론장 개설 버튼 클릭
        binding.btnAddDebate.setOnClickListener {
            var intent = Intent(this.activity, DebateCreateActivity::class.java)
            startActivity(intent)
        }

        // 토글 버튼 선택
        binding.btnToggleAll.setOnClickListener {
            debateType = 2
            binding.btnToggleAll.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.white))
            binding.btnToggleAll.setBackgroundResource(R.drawable.top_tab_view_fill)
            binding.btnToggleText.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.grey_700))
            binding.btnToggleText.setBackgroundResource(R.drawable.top_tab_view)
            loadData(debateType)
        }
        binding.btnToggleText.setOnClickListener {
            debateType = 0
            binding.btnToggleText.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.white))
            binding.btnToggleText.setBackgroundResource(R.drawable.top_tab_view_fill)
            binding.btnToggleAll.setTextColor(ContextCompat.getColor(this.requireContext(), R.color.grey_700))
            binding.btnToggleAll.setBackgroundResource(R.drawable.top_tab_view)
            loadData(debateType)
        }
        binding.btnToggleVoice.setOnClickListener {
            Toast.makeText(this@DebateFragment.activity, "데모 버전에서는 텍스트 채팅만 가능합니다.", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    // debateStatus : 1 = 맞춤 토론, 2 = 진행 중, 3 = 대기 중
    // debateType : 0 = 텍스트, 1 = 음성, 2 = 전체
    fun loadData(debateType: Int) {
        val data: MutableList<DebateSearchInfo> = mutableListOf()
        val pref = this.getActivity()?.getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
        val token = pref?.getString("Token", "")

        RetrofitBuilder.debateApi.roomList(token, debateType).enqueue(object : Callback<DebateListRequestDTO> {
            override fun onResponse(call: Call<DebateListRequestDTO>, response: Response<DebateListRequestDTO>) {
                val debateRoomList = response.body()
                var debateData: List<DebateRoom>
                // 토론장 상태마다 처리 (맞춤 토론)
                debateData = debateRoomList?.personalizedDebate!!
                personalizedAdapter.listData = mutableListOf<DebateSearchInfo>()
                if (debateData != null && personalizedAdapter.listData.isEmpty()) {
                    for (d in debateData) {
                        // ★★★ 파이어베이스에서 참여인원 가져오기 !!! 일단 2로 초기화
                        var debateRoom = DebateSearchInfo(d.roomId, d.topic, d.bookTitle, d.bookAuthor, d.coverImgUrl, d.curYesUser, d.curNoUser, d.time, d.owner, 2)
                        personalizedAdapter.listData.add(debateRoom)
                    }
                }

                // 토론장 상태마다 처리 (진행 중인 토론)
                debateData = debateRoomList?.proceedingDebate!!
                proceedingAdapter.listData = mutableListOf<DebateSearchInfo>()
                if (debateData != null) {
                    for (d in debateData) {
                        // ★★★ 파이어베이스에서 참여인원 가져오기 !!! 일단 2로 초기화
                        var debateRoom = DebateSearchInfo(d.roomId, d.topic, d.bookTitle, d.bookAuthor, d.coverImgUrl, d.curYesUser, d.curNoUser, d.time, d.owner, 2)
                        proceedingAdapter.listData.add(debateRoom)
                    }
                }

                // 토론장 상태마다 처리 (대기 중인 토론)
                debateData = debateRoomList?.waitDebate!!
                waitAdapter.listData = mutableListOf<DebateSearchInfo>()
                if (debateData != null) {
                    for (d in debateData) {
                        // ★★★ 파이어베이스에서 참여인원 가져오기 !!! 일단 2로 초기화
                        var debateRoom = DebateSearchInfo(d.roomId, d.topic, d.bookTitle, d.bookAuthor, d.coverImgUrl, d.curYesUser, d.curNoUser, d.time, d.owner, d.maxUser)
                        waitAdapter.listData.add(debateRoom)
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