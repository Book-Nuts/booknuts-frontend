package kr.co.booknuts.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.booknuts.ArchivePopUpActivity
import kr.co.booknuts.PostDetailActivity
import kr.co.booknuts.adapter.BoardListAdapter
import kr.co.booknuts.R
import kr.co.booknuts.data.Post
import kr.co.booknuts.databinding.FragmentHomeBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class HomeFragment : Fragment() {
    //private var swipeRefreshLayout: SwipeRefreshLayout? = null
    var dataArray: ArrayList<Post>? = null
    var tabType: Int = 0    // 0 -> 나의 구독, 1 -> 오늘 추천, 2 -> 독립출판
    var savedToken: String? = null

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

        binding.swipe.setOnRefreshListener{
            getPostData()
            binding.swipe.isRefreshing = false
        }

        // 로컬에 저장된 토큰
        val pref = this.activity?.getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
        savedToken = pref?.getString("Token", null)

        // 서버에서 게시글 데이터 받아오기
        getPostData()

        //binding.my
        return binding.root
    }

    fun getPostData() {
        RetrofitBuilder.api.getBoardList(savedToken, tabType).enqueue(object: Callback<ArrayList<Post>> {
            override fun onResponse(call: Call<ArrayList<Post>>, response: Response<ArrayList<Post>>) {
                dataArray = response.body()
                //Log.d("BoardList Get Test", "data : " + dataArray.toString())
                //Toast.makeText(activity, "통신 성공", Toast.LENGTH_SHORT).show()

                recyclerView = binding.rvBoard
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                val adapter: BoardListAdapter = BoardListAdapter(dataArray)
                if(dataArray?.size != 0 )
                    recyclerView.adapter = adapter
                adapter.setItemClickListener(object: BoardListAdapter.OnItemClickListener{
                    override fun onClick(v: View, position: Int) {
                        var intent = Intent(activity, PostDetailActivity::class.java)
                        intent.putExtra("id", dataArray?.get(position)?.boardId)
                        Log.d("Board ID", "" + dataArray?.get(position)?.boardId)
                        startActivity(intent)
                    }
                })
                adapter.setItemClickListenerArchive(object: BoardListAdapter.OnItemClickListenerArchive{
                    override fun onClick(v: View, position: Int) {
                        var intent = Intent(activity, ArchivePopUpActivity::class.java)
                        intent.putExtra("id", dataArray?.get(position)?.boardId)
                        Log.d("Board ID", "" + dataArray?.get(position)?.boardId)
                        startActivity(intent)
                    }
                })

            }
            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                //Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
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
            binding.homeImgIndieSample.visibility = View.GONE
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
            binding.homeImgIndieSample.visibility = View.GONE
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

            // 독립출판 뷰
            binding.homeImgIndieEvent.visibility = View.VISIBLE
            binding.homeImgIndieSample.visibility = View.VISIBLE

        }
    }
}
