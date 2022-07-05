package kr.co.booknuts.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.booknuts.view.adapter.MyArchiveListAdapter
import kr.co.booknuts.data.remote.MyArchive
import kr.co.booknuts.data.remote.ResultData
import kr.co.booknuts.databinding.ActivityArchiveAddBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArchiveAddActivity : AppCompatActivity() {

    val binding by lazy { ActivityArchiveAddBinding.inflate(layoutInflater) }

    private var archiveDataArray: ArrayList<MyArchive>? = null
    private var savedToken: String? = null
    var boardId: Int? = null

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        boardId = intent.getIntExtra("boardId", -1)?.toInt()
        Log.d("Board Id Got", boardId.toString())

        // 로컬에 저장된 토큰
        val pref = getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
        savedToken = pref?.getString("Token", null).toString()

        binding.imgClose.setOnClickListener{
            finish()
        }

        // 서버에서 나의 아카이브 데이터 받아오기
        RetrofitBuilder.api.getMyArchiveList(savedToken).enqueue(object:
            Callback<ArrayList<MyArchive>> {
            override fun onResponse(call: Call<ArrayList<MyArchive>>, response: Response<ArrayList<MyArchive>>) {
                archiveDataArray = response.body()
                //Log.d("Archive List Get Test", "data : " + archiveDataArray?.get(0)?.archiveId)
                //Toast.makeText(this@ArchiveAddActivity, "통신 성공", Toast.LENGTH_SHORT).show()

                recyclerView = binding.rvArchive
                recyclerView.layoutManager = LinearLayoutManager(this@ArchiveAddActivity)
                val adapter: MyArchiveListAdapter = MyArchiveListAdapter(archiveDataArray)
                if(archiveDataArray?.size != 0 )
                    Log.d("DataArray size is not 0", archiveDataArray?.size.toString())
                    recyclerView.adapter = adapter
                    adapter.setItemClickListener(object: MyArchiveListAdapter.OnItemClickListener{
                        override fun onClick(v: View, position: Int) {
                            Log.d("DataArray position", position.toString())
                            var archiveId: Int? = archiveDataArray?.get(position)?.archiveId
                            setPostInArchive(archiveId)
                            finish()
                        }
                })
            }
            override fun onFailure(call: Call<ArrayList<MyArchive>>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                //Toast.makeText(this@ArchiveAddActivity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun setPostInArchive(archiveId: Int?) {
        RetrofitBuilder.api.addPostToArchive(savedToken, archiveId, boardId).enqueue(object :
            Callback<ResultData> {
            override fun onResponse(
                call: Call<ResultData>,
                response: Response<ResultData>
            ) {
                Log.d("Add Archive", archiveId.toString() + " " + boardId)
                var responseData = response.body()
                Log.d("Post Success", responseData.toString())
                //Toast.makeText(this@ArchiveAddActivity, "통신 성공", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ResultData>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                //Toast.makeText(this@ArchiveAddActivity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }
}