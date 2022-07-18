package kr.co.booknuts.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.booknuts.view.adapter.MakeSeriesListAdapter
import kr.co.booknuts.data.remote.Post
import kr.co.booknuts.databinding.ActivityMakeSeriesFirstBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakeSeriesFirstActivity : AppCompatActivity() {
    companion object {
        var seriesCount: Int = 0
        var parentBinding: ActivityMakeSeriesFirstBinding? = null
        var postClickedList = ArrayList<Int?>()
        var postDataArray = ArrayList<Post>()
    }

    val binding by lazy {ActivityMakeSeriesFirstBinding.inflate(layoutInflater) }
    lateinit var recyclerView: RecyclerView

    private var accessToken: String? = null
    private var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        parentBinding = binding

        // 로컬에 저장된 토큰
        val pref = getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        accessToken = pref?.getString("accessToken", null).toString()
        userId = pref?.getInt("userId", -1)

        // 다음 버튼 클릭
        binding.textGoNext.setOnClickListener {
            if(seriesCount < 2)
                Toast.makeText(this@MakeSeriesFirstActivity, "게시글을 두 개 이상 선택해주세요.", Toast.LENGTH_SHORT).show()
            else {
                val intent = Intent(this@MakeSeriesFirstActivity, MakeSeriesSecondActivity::class.java)
                intent.putIntegerArrayListExtra("postClickedList", postClickedList)
                startActivity(intent)
                finish()
            }
        }

        // X 버튼 클릭
        binding.imgClose.setOnClickListener {
            finish()
        }

        // 서버에서 내가 쓴 게시글 데이터 받아오기
        RetrofitBuilder.myApi.getMyPostList(accessToken, userId).enqueue(object:
            Callback<ArrayList<Post>> {
            override fun onResponse(call: Call<ArrayList<Post>>, response: Response<ArrayList<Post>>) {
                postDataArray = response.body()!!
                Log.d("Post List Get Test", "data : " + postDataArray?.get(0)?.boardId)

                recyclerView = binding.rvPost
                recyclerView.layoutManager = LinearLayoutManager(this@MakeSeriesFirstActivity)
                val adapter: MakeSeriesListAdapter = MakeSeriesListAdapter(postDataArray)
                if(postDataArray?.size != 0 ) {
                    Log.d("DataArray size is not 0", "" + postDataArray?.size)
                    recyclerView.adapter = adapter
                }
            }
            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
            }
        })
    }
}