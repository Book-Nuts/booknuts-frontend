package kr.co.booknuts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.booknuts.adapter.BoardListAdapter
import kr.co.booknuts.data.*
import kr.co.booknuts.databinding.ActivityMainBinding
import kr.co.booknuts.databinding.ActivityWriteBinding
import kr.co.booknuts.fragment.HomeFragment
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WriteActivity : AppCompatActivity() {

    val binding by lazy { ActivityWriteBinding.inflate(layoutInflater) }
    //private val genres = resources.getStringArray(R.array.genres)
    var genres = arrayOf("자기계발", "매거진", "현대문학", "경제경영", "고전문학", "세계문학", "라이프스타일", "자녀교육", "어린이/청소년", "인문사회", "과학기술", "판타지/무협", "로맨스/BL", "독립서적")

    var bookGenre: String = "자기계발"
    var bookImgUrl = "https://cdn.pixabay.com/photo/2013/07/18/10/56/railroad-163518_960_720.jpg"
    var bookAuthor = "book author"
    var bookTitle = "book title"
    var title = ""
    var content = ""
    
    var responseData: PostDetail? = null

    private val fragmentHome by lazy { HomeFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val spinnerAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, genres)
        //Log.d("Genres", "1")
        binding.spinnerGenre.adapter = spinnerAdapter
        //Log.d("Genres", "2")
        //Log.d("Genres", ""+genres)

        binding.spinnerGenre.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //Log.d("Genres", "3")
                //if(position != 0) Toast.makeText(this@WriteActivity, genres[position], Toast.LENGTH_SHORT).show()
                bookGenre = genres[position]
            }
        }

        // 로컬에 저장된 토큰
        val pref = getSharedPreferences("authToken", AppCompatActivity.MODE_PRIVATE)
        val savedToken = pref?.getString("Token", null)

        binding.imgPosting.setOnClickListener {
            title = binding.editTitle.text.toString()
            content = binding.editContent.text.toString()

            if (!bookGenre.isEmpty() && !bookImgUrl.isEmpty() && !bookAuthor.isEmpty() && !bookTitle.isEmpty() && !title.isEmpty()) {
                var postInfo =
                    PostRequestDTO(title, content, bookTitle, bookAuthor, bookImgUrl, bookGenre)
                RetrofitBuilder.api.doPost(savedToken, postInfo).enqueue(object :
                    Callback<PostDetail> {
                    override fun onResponse(
                        call: Call<PostDetail>,
                        response: Response<PostDetail>
                    ) {
                        responseData = response.body()
                        Log.d("Post Success", "data : " + responseData.toString())
                        Toast.makeText(this@WriteActivity, "통신 성공", Toast.LENGTH_SHORT).show()
                        finish()
                    }

                    override fun onFailure(call: Call<PostDetail>, t: Throwable) {
                        Log.d("Approach Fail", "wrong server approach")
                        Toast.makeText(this@WriteActivity, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this@WriteActivity, "게시글 정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imgClose.setOnClickListener {
            finish()
        }
    }
}