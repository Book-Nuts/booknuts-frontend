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
import kr.co.booknuts.adapter.DebateRoomAdapter
import kr.co.booknuts.DebateCreateActivity
import kr.co.booknuts.data.DebateListRequestDTO
import kr.co.booknuts.data.DebateRoom
import kr.co.booknuts.data.DebateSearchInfo
import kr.co.booknuts.databinding.FragmentDebateBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DebateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DebateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val binding by lazy { FragmentDebateBinding.inflate(layoutInflater) }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentDebateBinding.inflate(inflater, container, false)

        // toggleType : 1 = 맞춤 토론, 2 = 진행 중, 3 = 대기 중
        // debateType : 0 = 텍스트, 1 = 음성, 2 = 전체
        // ★★★ debateType 변수로 바꾸기!!!
        val personalizedData: MutableList<DebateSearchInfo> = loadData(1, 2)
        binding.recyclePersonalized.layoutManager = LinearLayoutManager(requireContext())
        var personalizedAdapter = DebateRoomAdapter()
        personalizedAdapter.listData = personalizedData
        binding.recyclePersonalized.adapter = personalizedAdapter
        binding.recyclePersonalized.layoutManager = LinearLayoutManager(requireContext())

//        val proceedingData: MutableList<DebateSearchInfo> = loadData(2, 2)
//        binding.recycleProceeding.layoutManager = LinearLayoutManager(requireContext())
//        var proceedingAdapter = DebateRoomAdapter()
//        proceedingAdapter.listData = proceedingData
//        binding.recycleProceeding.adapter = proceedingAdapter
//        binding.recycleProceeding.layoutManager = LinearLayoutManager(requireContext())
//
//        val waitData: MutableList<DebateSearchInfo> = loadData(3, 2)
//        binding.recycleWait.layoutManager = LinearLayoutManager(requireContext())
//        var waitAdapter = DebateRoomAdapter()
//        waitAdapter.listData = waitData
//        binding.recycleWait.adapter = waitAdapter
//        binding.recycleWait.layoutManager = LinearLayoutManager(requireContext())

        // 토론장 개설 버튼 클릭
        binding.btnAddDebate.setOnClickListener {
            var intent = Intent(this.activity, DebateCreateActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    // toggleType : 1 = 맞춤 토론, 2 = 진행 중, 3 = 대기 중
    // debateType : 0 = 텍스트, 1 = 음성, 2 = 전체
    fun loadData(toggleType: Int, debateType: Int): MutableList<DebateSearchInfo> {
        val data: MutableList<DebateSearchInfo> = mutableListOf()
        val pref = this.getActivity()?.getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
        val token = pref?.getString("Token", "")

        RetrofitBuilder.debateApi.roomList(token, debateType).enqueue(object : Callback<DebateListRequestDTO> {
            override fun onResponse(call: Call<DebateListRequestDTO>, response: Response<DebateListRequestDTO>) {
                val debateRoomList = response.body()
                var debateData: List<DebateRoom>
                // 토글 버튼에 따라 타입 선택
                when(toggleType) {
                    1 -> debateData = debateRoomList?.personalizedDebate!!
                    2 -> debateData = debateRoomList?.proceedingDebate!!
                    3 -> debateData = debateRoomList?.waitDebate!!
                    else -> debateData = emptyList()
                }

                if (debateData != null) {
                    for (d in debateData) {
                        // ★★★ 시간과 파이어베이스에서 참여인원 가져오기 !!!
                        var debateRoom = DebateSearchInfo(d.roomId, d.topic, d.bookTitle, d.coverImgUrl, d.curYesUser, d.curNoUser, "10분", d.owner, 5)
                        Log.d("ROOMLIST_SUCCESS", "${toggleType} 타입 : ${debateRoom}")
                        data.add(debateRoom)
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

        return data
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DebateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DebateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}