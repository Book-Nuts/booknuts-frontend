package kr.co.booknuts.fragment

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
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
import kr.co.booknuts.adapter.BoardListAdapter
import kr.co.booknuts.adapter.MyArchiveListAdapter
import kr.co.booknuts.adapter.MyPostListAdapter
import kr.co.booknuts.adapter.MySeriesListAdapter
import kr.co.booknuts.data.MyArchive
import kr.co.booknuts.data.MySeries
import kr.co.booknuts.data.Post
import kr.co.booknuts.data.UserInfo
import kr.co.booknuts.databinding.FragmentMyBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFragment : Fragment() {

    var tab: Int = 0
    private var savedToken: String? = null
    private var mBinding: FragmentMyBinding? = null
    private val binding get() = mBinding!!

    private var postDataArray: ArrayList<Post>? = null
    private var seriesDataArray: ArrayList<MySeries>? = null
    private var archiveDataArray: ArrayList<MyArchive>? = null

    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentMyBinding.inflate(inflater, container, false)

        binding.myImgBg.setColorFilter(Color.parseColor("#bbbbbb"), PorterDuff.Mode.MULTIPLY);

        // 로컬에 저장된 토큰
        val pref = this.activity?.getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
        savedToken = pref?.getString("Token", null).toString()

        // 서버에서 유저 데이터 받아오기
        RetrofitBuilder.api.getUserInfo(savedToken).enqueue(object:
            Callback<UserInfo> {
            override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                var userInfo = response.body()
                Log.d("UserInfo Get Test", "data : " + userInfo?.accessToken)
                Toast.makeText(activity, "통신 성공", Toast.LENGTH_SHORT).show()
                binding.myTextNickname.text = userInfo?.loginId
            }
            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })

        postTab()

        //seriesDataArray?.add(MySeries(0, "Sample", "Content???", "", 0, 0))
        tabListener()

        // Inflate the layout for this fragment
        return binding.root
    }

    fun tabListener() {
        binding.myLinearPost.setOnClickListener{
            tab = 0
            // 탭 밑줄
            binding.myViewPost.visibility = View.VISIBLE
            binding.myViewSeries.visibility = View.GONE
            binding.myViewArchive.visibility = View.GONE

            // 글씨 굵기
            binding.myTextPost.setTypeface(null, Typeface.BOLD)
            binding.myTextSeries.setTypeface(null, Typeface.NORMAL)
            binding.myTextArchive.setTypeface(null, Typeface.NORMAL)

            postTab()
        }

        binding.myLinearSeries.setOnClickListener{
            tab = 1
            // 탭 밑줄
            binding.myViewPost.visibility = View.GONE
            binding.myViewSeries.visibility = View.VISIBLE
            binding.myViewArchive.visibility = View.GONE

            // 글씨 굵기
            binding.myTextPost.setTypeface(null, Typeface.NORMAL)
            binding.myTextSeries.setTypeface(null, Typeface.BOLD)
            binding.myTextArchive.setTypeface(null, Typeface.NORMAL)

            seriesTab()
        }

        binding.myLinearArchive.setOnClickListener{
            tab = 2
            // 탭 밑줄
            binding.myViewPost.visibility = View.GONE
            binding.myViewSeries.visibility = View.GONE
            binding.myViewArchive.visibility = View.VISIBLE

            // 글씨 굵기
            binding.myTextPost.setTypeface(null, Typeface.NORMAL)
            binding.myTextSeries.setTypeface(null, Typeface.NORMAL)
            binding.myTextArchive.setTypeface(null, Typeface.BOLD)

            archiveTab()
        }
    }

    fun postTab() {
        // 서버에서 내가 쓴 게시글 데이터 받아오기
        RetrofitBuilder.api.getMyPostList(savedToken).enqueue(object:
            Callback<ArrayList<Post>> {
            override fun onResponse(call: Call<ArrayList<Post>>, response: Response<ArrayList<Post>>) {
                postDataArray = response.body()
                Log.d("Post List Get Test", "data : " + postDataArray?.get(0)?.boardId)
                Toast.makeText(activity, "통신 성공", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })

        binding.myTextPost.text = "포스트 " + postDataArray?.size.toString()
        recyclerView = binding.myRv
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        if(postDataArray?.size != 0 )
            Log.d("DataArray size is not 0", "" + postDataArray?.size)
        recyclerView.adapter = MyPostListAdapter(postDataArray);
    }

    fun seriesTab() {
        RetrofitBuilder.api.getMySeriesList(savedToken).enqueue(object:
            Callback<ArrayList<MySeries>> {
            override fun onResponse(call: Call<ArrayList<MySeries>>, response: Response<ArrayList<MySeries>>) {
                seriesDataArray = response.body()
                Log.d("Post List Get Test", "data : " + seriesDataArray?.get(0)?.seriesId)
                Toast.makeText(activity, "통신 성공", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<ArrayList<MySeries>>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })

        //seriesDataArray = arrayListOf<MySeries>(MySeries(0, "Series Sample", "Content???", "", 0, 0))

        binding.myTextSeries.text = "시리즈 " + seriesDataArray?.size.toString()

        recyclerView = binding.myRv
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        if(seriesDataArray?.size != 0 )
            Log.d("DataArray size is not 0", "" + seriesDataArray?.size)
        recyclerView.adapter = MySeriesListAdapter(seriesDataArray);
    }

    fun archiveTab() {
        archiveDataArray = arrayListOf<MyArchive>(MyArchive(0, "Archive Sample", "Content???", ""))

        binding.myTextArchive.text = "아카이브 " + archiveDataArray?.size.toString()

        recyclerView = binding.myRv
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        if(archiveDataArray?.size != 0 )
            Log.d("DataArray size is not 0", "" + archiveDataArray?.size)
        recyclerView.adapter = MyArchiveListAdapter(archiveDataArray);
    }
}