package kr.co.booknuts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.booknuts.adapter.BoardListAdapter
import kr.co.booknuts.adapter.MakeSeriesListAdapter
import kr.co.booknuts.adapter.MySeriesListAdapter
import kr.co.booknuts.data.Post
import kr.co.booknuts.databinding.ActivityMakeSeriesFirstBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakeSeriesFirstActivity : AppCompatActivity() {

    val binding by lazy {ActivityMakeSeriesFirstBinding.inflate(layoutInflater) }
    lateinit var recyclerView: RecyclerView

    private var savedToken: String? = null
    private var postDataArray: ArrayList<Post>? = null

    var postClickedList = ArrayList<Int?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 로컬에 저장된 토큰
        val pref = getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
        savedToken = pref?.getString("Token", null).toString()

        binding.textGoNext.setOnClickListener {
            val intent = Intent(this@MakeSeriesFirstActivity, MakeSeriesSecondActivity::class.java)
            intent.putIntegerArrayListExtra("postClickedList", postClickedList)
            startActivity(intent)
        }

        binding.imgClose.setOnClickListener {
            finish()
        }

        // 서버에서 내가 쓴 게시글 데이터 받아오기
        RetrofitBuilder.api.getMyPostList(savedToken).enqueue(object:
            Callback<ArrayList<Post>> {
            override fun onResponse(call: Call<ArrayList<Post>>, response: Response<ArrayList<Post>>) {
                postDataArray = response.body()
                Log.d("Post List Get Test", "data : " + postDataArray?.get(0)?.boardId)
                Toast.makeText(this@MakeSeriesFirstActivity, "통신 성공", Toast.LENGTH_SHORT).show()

                recyclerView = binding.rvPost
                recyclerView.layoutManager = LinearLayoutManager(this@MakeSeriesFirstActivity)
                val adapter: MakeSeriesListAdapter = MakeSeriesListAdapter(postDataArray)
                if(postDataArray?.size != 0 ) {
                    Log.d("DataArray size is not 0", "" + postDataArray?.size)
                    recyclerView.adapter = adapter
                }
                adapter.setItemClickListener(object: MakeSeriesListAdapter.OnItemClickListener{
                    override fun onClick(v: View, position: Int) {
                        postClickedList.add(postDataArray?.get(position)?.boardId)
                        Log.d("Board ID", postClickedList.toString())
                    }
                })
            }
            override fun onFailure(call: Call<ArrayList<Post>>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                Toast.makeText(this@MakeSeriesFirstActivity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }
}