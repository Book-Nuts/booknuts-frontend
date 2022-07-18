package kr.co.booknuts.view.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import kr.co.booknuts.data.remote.MySeries
import kr.co.booknuts.data.remote.SeriesRequestDTO
import kr.co.booknuts.databinding.ActivityMakeSeriesSecondBinding
import kr.co.booknuts.view.adapter.fragment.MyFragment
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MakeSeriesSecondActivity : AppCompatActivity() {

    val binding by lazy { ActivityMakeSeriesSecondBinding.inflate(layoutInflater) }

    private val fragmentMy by lazy { MyFragment() }

    private var savedToken: String? = null

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

        // 키보드 내리기
        binding.toolbar.setOnClickListener { hideKeyboard() }
        binding.linearTitle.setOnClickListener { hideKeyboard() }
        binding.linearContent.setOnClickListener { hideKeyboard() }

        var intentG = intent
        var postClickedList: List<Long> = intentG.getIntegerArrayListExtra("postClickedList") as List<Long>
        Log.d("Get Post List", postClickedList.toString())

        binding.imgClose.setOnClickListener {
            finish()
        }

        // 로컬에 저장된 토큰
        val pref = getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        savedToken = pref?.getString("accessToken", null).toString()

        // 만들기 버튼 클릭
        binding.textGoNext.setOnClickListener{
            var title = binding.editTitle.text.toString()
            var content = binding.editContent.text.toString()
            var random = Random().nextInt(imageList.size)
            var imgUrl = imageList[random]

            if(!title.isEmpty() && !content.isEmpty()){
                var seriesInfo = SeriesRequestDTO(title, content, imgUrl, postClickedList)
                // 시리즈 생성
                RetrofitBuilder.myApi.postSeries(savedToken, seriesInfo).enqueue(object : Callback<MySeries> {
                    override fun onResponse(call: Call<MySeries>, response: Response<MySeries>) {
                        Log.d("MAKE_SERIES", seriesInfo.toString())
                        var responseData = response.body()
                        Log.d("MAKE_SERIES", responseData.toString())
                        finish()
//                        val intent = Intent(this@MakeSeriesSecondActivity, MainActivity::class.java)
//                        startActivity(intent)
                    }

                    override fun onFailure(call: Call<MySeries>, t: Throwable) {
                        Log.d("MAKE_SERIES", "wrong server approach")
                        //Toast.makeText(this@MakeSeriesSecondActivity, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    // 키보드 비활성화 함수
    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editTitle.windowToken, 0)
        imm.hideSoftInputFromWindow(binding.editContent.windowToken, 0)
    }
}