package kr.co.booknuts.view.adapter.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.booknuts.view.activity.MainActivity
import kr.co.booknuts.view.activity.SeriesPopUpActivity
import kr.co.booknuts.view.adapter.MyArchiveListAdapter
import kr.co.booknuts.view.adapter.MyPostListAdapter
import kr.co.booknuts.view.adapter.MySeriesListAdapter
import kr.co.booknuts.data.remote.MyArchive
import kr.co.booknuts.data.remote.MySeries
import kr.co.booknuts.data.remote.Post
import kr.co.booknuts.databinding.FragmentMyBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kr.co.booknuts.view.activity.PostDetailActivity
import kr.co.booknuts.view.fragment.ArchiveDetailFragment


class MyFragment : Fragment() {

    var tab: Int = 0
    var postCnt: Int = 0
    var seriesCnt: Int = 0
    var archiveCnt: Int = 0

    private var accessToken: String? = null
    private var nickname: String? = null
    private var userId: Int? = null
    private var mBinding: FragmentMyBinding? = null
    private val binding get() = mBinding!!

    private var postDataArray: ArrayList<Post>? = null
    private var seriesDataArray: ArrayList<MySeries>? = null
    private var archiveDataArray: ArrayList<MyArchive>? = null

    private val fragmentMySeriesDetail by lazy { MySeriesDetailFragment() }
    private val fragmentArchiveDetail by lazy { ArchiveDetailFragment() }

    lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentMyBinding.inflate(inflater, container, false)

        binding.myImgBg.setColorFilter(Color.parseColor("#aaaaaa"), PorterDuff.Mode.MULTIPLY);

        // 로컬에 저장된 토큰
        val pref = this.activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        accessToken = pref?.getString("accessToken", null).toString()
        nickname = pref?.getString("nickname", null).toString()
        userId = pref?.getInt("userId", -1)
        binding.myTextNickname.text = nickname

        Log.d("UserId", "" + userId)

        val idx = nickname!!.length.rem(5)
        when (idx) {
            0 -> binding.myImgProfile.setImageResource(kr.co.booknuts.R.drawable.img_user1)
            1 -> binding.myImgProfile.setImageResource(kr.co.booknuts.R.drawable.img_user2)
            2 -> binding.myImgProfile.setImageResource(kr.co.booknuts.R.drawable.img_user3)
            3 -> binding.myImgProfile.setImageResource(kr.co.booknuts.R.drawable.img_user4)
            4 -> binding.myImgProfile.setImageResource(kr.co.booknuts.R.drawable.img_user5)
        }

        RetrofitBuilder.myApi.getMyPostList(accessToken, userId).enqueue(object:
            Callback<ArrayList<Post>> {
            override fun onResponse(call: Call<ArrayList<Post>>, response: Response<ArrayList<Post>>) {
                postDataArray = response.body()
                postCnt = postDataArray?.size!!
                postTab()
            }
            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
            }
        })

        getSeriesData()
        getArchiveData()

        binding.linearMenu.setOnClickListener{
            if(tab == 1){
                val intent = Intent(activity, SeriesPopUpActivity::class.java)
                startActivity(intent)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabListener()
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

    fun getPostData() {
        // 서버에서 내가 쓴 게시글 데이터 받아오기
        RetrofitBuilder.myApi.getMyPostList(accessToken, userId).enqueue(object:
            Callback<ArrayList<Post>> {
            override fun onResponse(call: Call<ArrayList<Post>>, response: Response<ArrayList<Post>>) {
                postDataArray = response.body()
                Log.d("Post List Get Test", "" + postDataArray)
                //Toast.makeText(activity, "통신 성공", Toast.LENGTH_SHORT).show()
                postCnt = postDataArray?.size!!
            }
            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                //Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getSeriesData() {
        // 서버에서 나의 시리즈 데이터 받아오기
        RetrofitBuilder.myApi.getMySeriesList(accessToken, userId).enqueue(object:
            Callback<ArrayList<MySeries>> {
            override fun onResponse(call: Call<ArrayList<MySeries>>, response: Response<ArrayList<MySeries>>) {
                seriesDataArray = response.body()
                //Log.d("Series List Get Test", "data : " + seriesDataArray?.get(0)?.seriesId)
                //Toast.makeText(activity, "통신 성공", Toast.LENGTH_SHORT).show()
                seriesCnt = seriesDataArray?.size!!
            }
            override fun onFailure(call: Call<ArrayList<MySeries>>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                //Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getArchiveData() {
        // 서버에서 나의 아카이브 데이터 받아오기
        RetrofitBuilder.myApi.getMyArchiveList(accessToken, userId).enqueue(object:
            Callback<ArrayList<MyArchive>> {
            override fun onResponse(call: Call<ArrayList<MyArchive>>, response: Response<ArrayList<MyArchive>>) {
                archiveDataArray = response.body()
                //Log.d("Archive List Get Test", "data : " + archiveDataArray?.get(0)?.archiveId)
                //Toast.makeText(activity, "통신 성공", Toast.LENGTH_SHORT).show()
                archiveCnt = archiveDataArray?.size!!
            }
            override fun onFailure(call: Call<ArrayList<MyArchive>>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                //
            // Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 포스트 탭 선택
    fun postTab() {
        binding.myTextPost.text = "포스트 " + postCnt
        binding.myTextSeries.text = "시리즈"
        binding.myTextArchive.text = "아카이브"

        recyclerView = binding.myRv
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter: MyPostListAdapter = MyPostListAdapter(postDataArray)
        if(postCnt != 0 )
            Log.d("DataArray size is not 0", "" + postCnt)
        recyclerView.adapter = adapter
        adapter.setItemClickListener(object: MyPostListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                var intent = Intent(activity, PostDetailActivity::class.java)
                intent.putExtra("id", postDataArray?.get(position)?.boardId)
                intent.putExtra("writer", postDataArray?.get(position)?.writer)
                Log.d("Board ID", "" + postDataArray?.get(position)?.boardId)
                startActivity(intent)
            }
        })
    }

    fun seriesTab() {
        //seriesDataArray = arrayListOf<MySeries>(MySeries(0, "Series Sample", "Content???", "", 0, 0))
        binding.myTextPost.text = "포스트" + postCnt
        binding.myTextSeries.text = "시리즈 " + seriesCnt
        binding.myTextArchive.text = "아카이브" + archiveCnt

        recyclerView = binding.myRv
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter: MySeriesListAdapter = MySeriesListAdapter(seriesDataArray)
        if(seriesCnt != 0 )
            Log.d("DataArray size is not 0", "" + seriesCnt)
        recyclerView.adapter = adapter
        adapter.setItemClickListener(object: MySeriesListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                Log.d("SeriesDataArrayposition", position.toString())
                var series: MySeries? = seriesDataArray?.get(position)
                Log.d("SeriesDataArray", seriesDataArray.toString())
                var seriesInfo: Array<String?> = arrayOf(series?.seriesId?.toString(), series?.title, series?.content, series?.imgUrl)
                Log.d("SeriesDataArray", seriesInfo.toString())
                (activity as MainActivity).changeFragmentWithArrayData(fragmentMySeriesDetail, seriesInfo)
            }
        })
    }

    fun archiveTab() {
        //archiveDataArray = arrayListOf<MyArchive>(MyArchive(0, "Archive Sample", "Content???", ""))

        binding.myTextPost.text = "포스트" + postCnt
        binding.myTextSeries.text = "시리즈"
        binding.myTextArchive.text = "아카이브 " + archiveCnt

        recyclerView = binding.myRv
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter: MyArchiveListAdapter = MyArchiveListAdapter(archiveDataArray)
        if(archiveCnt != 0 )
            Log.d("DataArray size is not 0", "" + archiveCnt)
        recyclerView.adapter = adapter
        adapter.setItemClickListener(object: MyArchiveListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                Log.d("DataArray position", position.toString())
                var archive: MyArchive? = archiveDataArray?.get(position)
                Log.d("DataArray", archiveDataArray.toString())
                var archiveInfo: Array<String?> = arrayOf(archive?.archiveId?.toString(), archive?.title, archive?.content, archive?.imgUrl)
                Log.d("DataArray", archiveInfo.toString())
                (activity as MainActivity).changeFragmentWithArrayData(fragmentArchiveDetail, archiveInfo);
            }
        })
    }
}