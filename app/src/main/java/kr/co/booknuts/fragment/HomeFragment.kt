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
import kr.co.booknuts.Adapter.BoardListAdapter
import kr.co.booknuts.R
import kr.co.booknuts.data.BoardList
import kr.co.booknuts.data.Post
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

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        rootView.home_text_my_sub.setOnClickListener{
            tabType = 0
            rootView.home_text_my_sub.setTextColor(resources.getColor(R.color.white))
            rootView.home_text_today.setTextColor(resources.getColor(R.color.black))
            rootView.home_text_indie.setTextColor(resources.getColor(R.color.black))
            rootView.home_text_my_sub.setBackgroundResource(R.drawable.top_tab_view_fill)
            rootView.home_text_today.setBackgroundResource(R.drawable.top_tab_view)
            rootView.home_text_indie.setBackgroundResource(R.drawable.top_tab_view)
            rootView.home_text_title.text = "내가 구독한 게시글"
        }

        rootView.home_text_today.setOnClickListener{
            tabType = 1
            rootView.home_text_my_sub.setTextColor(resources.getColor(R.color.black))
            rootView.home_text_today.setTextColor(resources.getColor(R.color.white))
            rootView.home_text_indie.setTextColor(resources.getColor(R.color.black))
            rootView.home_text_my_sub.setBackgroundResource(R.drawable.top_tab_view)
            rootView.home_text_today.setBackgroundResource(R.drawable.top_tab_view_fill)
            rootView.home_text_indie.setBackgroundResource(R.drawable.top_tab_view)
            rootView.home_text_title.text = "오늘의 추천 게시글"
        }

        rootView.home_text_indie.setOnClickListener{
            tabType = 2
            rootView.home_text_my_sub.setTextColor(resources.getColor(R.color.black))
            rootView.home_text_today.setTextColor(resources.getColor(R.color.black))
            rootView.home_text_indie.setTextColor(resources.getColor(R.color.white))
            rootView.home_text_my_sub.setBackgroundResource(R.drawable.top_tab_view)
            rootView.home_text_today.setBackgroundResource(R.drawable.top_tab_view)
            rootView.home_text_indie.setBackgroundResource(R.drawable.top_tab_view_fill)
            rootView.home_text_title.text = "오늘의 독립출판 서적"
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

                recyclerView = rootView.findViewById(R.id.rv_board!!)as RecyclerView
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                if(dataArray?.size != 0 )
                    recyclerView.adapter = BoardListAdapter(requireContext(), dataArray);
            }
            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })

        return rootView
    }
}
