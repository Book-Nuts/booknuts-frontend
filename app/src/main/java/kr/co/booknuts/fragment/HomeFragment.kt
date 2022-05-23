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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    //lateinit var binding: FragmentHomeBinding
    //var datas = mutableListOf<Post>()
    var dataArray: ArrayList<Post>? = null
    var datas: BoardList? = null
    var tabType: Int = 0    // 0 -> 나의 구독, 1 -> 오늘 추천, 2 -> 독립출판

    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeTextMySub.setOnClickListener{
            tabType = 0
            binding.homeTextMySub.setTextColor(resources.getColor(R.color.white))
            binding.homeTextToday.setTextColor(resources.getColor(R.color.black))
            binding.homeTextIndie.setTextColor(resources.getColor(R.color.black))
            binding.homeTextMySub.setBackgroundResource(R.drawable.top_tab_view_fill)
            binding.homeTextToday.setBackgroundResource(R.drawable.top_tab_view)
            binding.homeTextIndie.setBackgroundResource(R.drawable.top_tab_view)
            binding.homeTextTitle.text = "내가 구독한 게시글"
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
            val img_indie = ImageView(context)
            //val layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            //img_indie.layoutParams = layoutParams
            //img_indie.setImageResource(R.drawable.img_today_indie)
            //rootView.rv_board.addView(img_indie)

        }

        // 로컬에 저장된 토큰
        val pref = this.activity?.getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
        val savedToken = pref?.getString("Token", null)

        // 서버에서 게시글 데이터 받아오기
        RetrofitBuilder.api.getBoardList(savedToken, tabType).enqueue(object: Callback<ArrayList<Post>> {
            override fun onResponse(call: Call<ArrayList<Post>>, response: Response<ArrayList<Post>>) {
                dataArray = response.body()
                Log.d("BoardList Get Test", "data : " + dataArray?.size)
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
}
