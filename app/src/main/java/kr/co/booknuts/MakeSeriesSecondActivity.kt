package kr.co.booknuts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kr.co.booknuts.data.MySeries
import kr.co.booknuts.data.PostDetail
import kr.co.booknuts.data.PostRequestDTO
import kr.co.booknuts.data.SeriesRequestDTO
import kr.co.booknuts.databinding.ActivityMakeSeriesFirstBinding
import kr.co.booknuts.databinding.ActivityMakeSeriesSecondBinding
import kr.co.booknuts.fragment.MyFragment
import kr.co.booknuts.fragment.MySeriesDetailFragment
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MakeSeriesSecondActivity : AppCompatActivity() {

    val binding by lazy { ActivityMakeSeriesSecondBinding.inflate(layoutInflater) }

    private val fragmentMy by lazy { MyFragment() }

    private var savedToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var intentG = intent
        var postClickedList: Array<Int> = intentG.getIntegerArrayListExtra("postClickedList") as Array<Int>
        Log.d("Get Post List", postClickedList.toString())

        binding.imgClose.setOnClickListener {
            finish()
        }

        // 로컬에 저장된 토큰
        val pref = getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
        savedToken = pref?.getString("Token", null).toString()

        binding.textGoNext.setOnClickListener{
            var title = binding.editTitle.text.toString()
            var content = binding.editContent.text.toString()
            var imgUrl = "https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072821_960_720.jpg"

            if(!title.isEmpty() && !content.isEmpty()){
                var seriesInfo = SeriesRequestDTO(title, content, imgUrl, postClickedList)
                RetrofitBuilder.api.postSeries(savedToken, seriesInfo).enqueue(object :
                    Callback<MySeries> {
                    override fun onResponse(
                        call: Call<MySeries>,
                        response: Response<MySeries>
                    ) {
                        Log.d("Post Info Sent", seriesInfo.toString())
                        var responseData = response.body()
                        Log.d("Post Success", responseData.toString())
                        Toast.makeText(this@MakeSeriesSecondActivity, "통신 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@MakeSeriesSecondActivity, MainActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onFailure(call: Call<MySeries>, t: Throwable) {
                        Log.d("Approach Fail", "wrong server approach")
                        Toast.makeText(this@MakeSeriesSecondActivity, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}