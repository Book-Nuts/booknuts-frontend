package kr.co.booknuts.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kr.co.booknuts.databinding.ActivityWriteBinding
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kr.co.booknuts.R
import kr.co.booknuts.data.remote.PostDetail
import kr.co.booknuts.data.remote.PostRequestDTO
import kr.co.booknuts.view.CommonMethod


class WriteActivity : AppCompatActivity() {

    val binding by lazy { ActivityWriteBinding.inflate(layoutInflater) }
    //private val genres = resources.getStringArray(R.array.genres)
    var genres = arrayOf("자기계발", "매거진", "현대문학", "경제경영", "고전문학", "세계문학", "라이프스타일", "자녀교육", "어린이/청소년", "인문사회", "과학기술", "판타지/무협", "로맨스/BL", "독립서적")

    var bookGenre: String? = "자기계발"
    var bookImgUrl: String? = ""
    var bookAuthor: String? = ""
    var bookTitle: String? = ""
    var title: String? = ""
    var content: String? = ""

    var responseData: PostDetail? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === 1234 && resultCode === RESULT_OK) {
            val bookInfo = data?.extras?.get("postInfo") as PostRequestDTO
            Log.d("Book Info", bookInfo.toString())
            var title = bookInfo.bookTitle
            bookTitle = title
            bookAuthor = bookInfo.bookAuthor
            bookImgUrl = bookInfo.bookImgUrl
            binding.imgBook.visibility = View.VISIBLE
            binding.imgPlus.visibility = View.GONE
            binding.textAddBook.visibility = View.GONE
            Glide.with(binding.imgBook)
                .load(bookInfo.bookImgUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.img_user2)
                .fitCenter()
                .into(binding.imgBook)
        } else {
            Log.d("Book Info", "null")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var type = intent.getIntExtra("type", 0)


        // 키보드 내리기
        binding.layout.setOnClickListener { CommonMethod.hideKeyboards(binding.editContent, binding.editTitle, this@WriteActivity) }
        binding.toolbar.setOnClickListener { CommonMethod.hideKeyboards(binding.editContent, binding.editTitle, this@WriteActivity) }
        binding.linear.setOnClickListener { CommonMethod.hideKeyboards(binding.editContent, binding.editTitle, this@WriteActivity) }

        val spinnerAdapter = ArrayAdapter(this,
            R.layout.support_simple_spinner_dropdown_item, genres)
        binding.spinnerGenre.adapter = spinnerAdapter

        binding.spinnerGenre.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                bookGenre = genres[position]
            }
        }

        binding.linearAddBook.setOnClickListener{
            title = binding.editTitle.text.toString()
            content = binding.editContent.text.toString()
            var intent = Intent(this@WriteActivity, BookSearchActivity::class.java)
            startActivityForResult(intent, 1234)
        }

        // 로컬에 저장된 토큰
        val pref = getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val savedToken = pref?.getString("accessToken", null)

        binding.imgPosting.setOnClickListener {
            title = binding.editTitle.text.toString()
            content = binding.editContent.text.toString()

            if (!bookGenre?.isEmpty()!! && !bookImgUrl?.isEmpty()!! && !bookAuthor?.isEmpty()!! && !bookTitle?.isEmpty()!! && !title?.isEmpty()!!) {
                var postInfo =
                    PostRequestDTO(title, content, bookTitle, bookAuthor, bookImgUrl, bookGenre)
                RetrofitBuilder.boardApi.doPost(savedToken, postInfo).enqueue(object :
                    Callback<PostDetail> {
                    override fun onResponse(
                        call: Call<PostDetail>,
                        response: Response<PostDetail>
                    ) {
                        responseData = response.body()
                        //Toast.makeText(this@WriteActivity, "통신 성공", Toast.LENGTH_SHORT).show()
                        closeWriteActivity()
                    }

                    override fun onFailure(call: Call<PostDetail>, t: Throwable) {
                        Log.d("Approach Fail", "wrong server approach")
                        //Toast.makeText(this@WriteActivity, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this@WriteActivity, "게시글 정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                //Log.d("Post Info", postInfo.toString())
            }
        }

        binding.imgClose.setOnClickListener {
            closeWriteActivity()
        }
    }

    override fun onBackPressed() {
        closeWriteActivity()
    }

    fun closeWriteActivity() {
        val intent = Intent(this@WriteActivity, MainActivity::class.java)
        startActivity(intent)
    }
}