package kr.co.booknuts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kr.co.booknuts.data.ArchiveRequestDTO
import kr.co.booknuts.data.MyArchive
import kr.co.booknuts.data.ResultData
import kr.co.booknuts.databinding.ActivityMakeArchiveBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MakeArchiveActivity : AppCompatActivity() {

    val binding by lazy { ActivityMakeArchiveBinding.inflate(layoutInflater) }

    private var savedToken: String? = null
    var boardId: Int? = null

    private var imageList: ArrayList<String> = arrayListOf("https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072821_960_720.jpg",
        "https://cdn.pixabay.com/photo/2016/09/08/22/43/books-1655783_1280.jpg",
        "https://cdn.pixabay.com/photo/2016/03/26/22/21/books-1281581_960_720.jpg",
        "https://cdn.pixabay.com/photo/2017/02/26/21/39/rose-2101475_960_720.jpg",
        "https://cdn.pixabay.com/photo/2018/10/05/14/39/sunset-3726030_960_720.jpg",
        "https://cdn.pixabay.com/photo/2016/10/18/21/28/seljalandsfoss-1751463_960_720.jpg",
        "https://cdn.pixabay.com/photo/2016/11/08/05/20/sunset-1807524__340.jpg")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        boardId = intent.getIntExtra("boardId", -1)?.toInt()
        binding.imgClose.setOnClickListener {
            finish()
        }

        // 로컬에 저장된 토큰
        val pref = getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
        savedToken = pref?.getString("Token", null).toString()

        binding.textGoNext.setOnClickListener{
            var title = binding.editTitle.text.toString()
            var content = binding.editContent.text.toString()
            var random = Random().nextInt(imageList.size)
            var imgUrl = imageList[random]

            if(!title.isEmpty() && !content.isEmpty()){
                var archiveInfo = ArchiveRequestDTO(title, content, imgUrl)
                RetrofitBuilder.api.postArchive(savedToken, archiveInfo).enqueue(object :
                    Callback<MyArchive> {
                    override fun onResponse(
                        call: Call<MyArchive>,
                        response: Response<MyArchive>
                    ) {
                        Log.d("Post Info Sent", archiveInfo.toString())
                        var responseData = response.body()
                        Log.d("Post Success", responseData?.archiveId.toString())
                        Toast.makeText(this@MakeArchiveActivity, "통신 성공", Toast.LENGTH_SHORT).show()
                        setPostInArchive(responseData?.archiveId)

                        val intent = Intent(this@MakeArchiveActivity, MainActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onFailure(call: Call<MyArchive>, t: Throwable) {
                        Log.d("Approach Fail", "wrong server approach")
                        Toast.makeText(this@MakeArchiveActivity, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    fun setPostInArchive(archiveId: Int?) {
        RetrofitBuilder.api.addPostToArchive(savedToken, archiveId, boardId).enqueue(object :
            Callback<ResultData> {
            override fun onResponse(
                call: Call<ResultData>,
                response: Response<ResultData>
            ) {
                //Log.d("Post Info Sent", archiveInfo.toString())
                var responseData = response.body()
                Log.d("Post Success", archiveId.toString())
                Toast.makeText(this@MakeArchiveActivity, "통신 성공", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ResultData>, t: Throwable) {
                Log.d("Approach Fail", "wrong server approach")
                Toast.makeText(this@MakeArchiveActivity, "통신 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }
}