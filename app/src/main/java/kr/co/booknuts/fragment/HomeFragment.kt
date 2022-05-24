package kr.co.booknuts.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_intro.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kr.co.booknuts.adapter.BoardListAdapter
import kr.co.booknuts.R
import kr.co.booknuts.data.BoardList
import kr.co.booknuts.data.Post
import kr.co.booknuts.databinding.FragmentHomeBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class HomeFragment : Fragment() {

    var dataArray: ArrayList<Post>? = null
    var tabType: Int = 0    // 0 -> 나의 구독, 1 -> 오늘 추천, 2 -> 독립출판

    lateinit var recyclerView: RecyclerView
    //val rootView:
    private var mBinding: FragmentHomeBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        //val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        tabListener()

        // 로컬에 저장된 토큰
        val pref = this.activity?.getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
        val savedToken = pref?.getString("Token", null)

        // 서버에서 게시글 데이터 받아오기
        RetrofitBuilder.api.getBoardList(savedToken, tabType).enqueue(object: Callback<ArrayList<Post>> {
            override fun onResponse(call: Call<ArrayList<Post>>, response: Response<ArrayList<Post>>) {
                dataArray = response.body()
                Log.d("BoardList Get Test", "data : " + dataArray.toString())
                Toast.makeText(activity, "통신 성공", Toast.LENGTH_SHORT).show()

                recyclerView = binding.rvBoard
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                if(dataArray?.size != 0 )
                    recyclerView.adapter = BoardListAdapter(dataArray);
            }
            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    fun tabListener() {
        binding.homeTextMySub.setOnClickListener{
            tabType = 0
            binding.homeTextMySub.setTextColor(resources.getColor(R.color.white))
            binding.homeTextToday.setTextColor(resources.getColor(R.color.black))
            binding.homeTextIndie.setTextColor(resources.getColor(R.color.black))
            binding.homeTextMySub.setBackgroundResource(R.drawable.top_tab_view_fill)
            binding.homeTextToday.setBackgroundResource(R.drawable.top_tab_view)
            binding.homeTextIndie.setBackgroundResource(R.drawable.top_tab_view)
            binding.homeTextTitle.text = "내가 구독한 게시글"

            // 독립출판 뷰
            binding.homeImgIndieEvent.visibility = View.GONE
            binding.homeLinearIndieList.visibility = View.GONE
        }

        binding.homeTextToday.setOnClickListener{
            tabType = 1
            binding.homeTextMySub.setTextColor(resources.getColor(R.color.black))
            binding.homeTextToday.setTextColor(resources.getColor(R.color.white))
            binding.homeTextIndie.setTextColor(resources.getColor(R.color.black))
            binding.homeTextMySub.setBackgroundResource(R.drawable.top_tab_view)
            binding.homeTextToday.setBackgroundResource(R.drawable.top_tab_view_fill)
            binding.homeTextIndie.setBackgroundResource(R.drawable.top_tab_view)
            binding.homeTextTitle.text = "오늘의 추천 게시글"

            // 독립출판 뷰
            binding.homeImgIndieEvent.visibility = View.GONE
            binding.homeLinearIndieList.visibility = View.GONE
        }

        binding.homeTextIndie.setOnClickListener{
            tabType = 2
            binding.homeTextMySub.setTextColor(resources.getColor(R.color.black))
            binding.homeTextToday.setTextColor(resources.getColor(R.color.black))
            binding.homeTextIndie.setTextColor(resources.getColor(R.color.white))
            binding.homeTextMySub.setBackgroundResource(R.drawable.top_tab_view)
            binding.homeTextToday.setBackgroundResource(R.drawable.top_tab_view)
            binding.homeTextIndie.setBackgroundResource(R.drawable.top_tab_view_fill)
            binding.homeTextTitle.text = "오늘의 독립출판 서적"

            // 독립출판 이벤트 이미지 동적 추가
            /*val img_indie = ImageView(context)
            val layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            img_indie.layoutParams = layoutParams
            img_indie.setImageResource(R.drawable.img_today_indie)
            rootView.home_linear_board.addView(img_indie, 0)*/

            // 독립출판 뷰
            binding.homeImgIndieEvent.visibility = View.VISIBLE
            binding.homeLinearIndieList.visibility = View.VISIBLE

        }
    }
}
