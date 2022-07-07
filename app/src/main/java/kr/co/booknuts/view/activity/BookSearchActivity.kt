package kr.co.booknuts.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.booknuts.data.remote.*
import kr.co.booknuts.view.adapter.BookSearchListAdapter
import kr.co.booknuts.databinding.ActivityBookSearchBinding
import kr.co.booknuts.retrofit.BookRetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookSearchActivity : AppCompatActivity() {

    val binding by lazy { ActivityBookSearchBinding.inflate(layoutInflater) }

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 키보드 내리기
        binding.toolbar.setOnClickListener { hideKeyboard() }
        binding.rvBook.setOnClickListener { hideKeyboard() }
        binding.linear.setOnClickListener { hideKeyboard() }

        binding.imgClose.setOnClickListener{
            finish()
        }

        binding.imgSearch.setOnClickListener {
            var bookTitle = binding.editBookTitle.text.toString()
            hideKeyboard()
            //Toast.makeText(this@BookSearchActivity, "Title: " + bookTitle, Toast.LENGTH_SHORT).show()

            if(!bookTitle.isEmpty()) {
                /*var searchResponse: List<BookItemNaver>?
                BookRetrofitBuilder.api.searchBookNaver(bookTitle).enqueue(object: Callback<BookSearchInfoNaver> {
                    override fun onResponse(call: Call<BookSearchInfoNaver>, response: Response<BookSearchInfoNaver>) {
                        //Toast.makeText(this@BookSearchActivity, "통신 성공", Toast.LENGTH_SHORT).show()
                        searchResponse = response.body()?.item
                        if(searchResponse != null) {
                            //Log.d("Search Success", searchResponse.toString())
                            //Toast.makeText(this@BookSearchActivity, "Search Success", Toast.LENGTH_SHORT).show()
                            recyclerView = binding.rvBook
                            recyclerView.layoutManager = LinearLayoutManager(this@BookSearchActivity)
                            val adapter: BookSearchListAdapter = BookSearchListAdapter(searchResponse)
                            if(searchResponse?.size != 0 ){
                                recyclerView.adapter = adapter
                                adapter.setItemClickListener(object: BookSearchListAdapter.OnItemClickListener {
                                    override fun onClick(v: View, position: Int) {
                                        var bookInfo = searchResponse?.get(position)
                                        var postInfo = PostRequestDTO("", "",
                                            bookInfo?.title?.replace("<b>", "")?.replace("</b>", ""), bookInfo?.author?.replace("<b>", "")?.replace("</b>", ""), bookInfo?.image, "")
                                        var intent = Intent()
                                        intent.putExtra("postInfo", postInfo)
                                        Log.d("Book Search Post Info", postInfo.toString())
                                        setResult(RESULT_OK, intent)
                                        finish()
                                    }
                                })
                            }
                        } else {
                            Log.d("Search Null", "no book")
                            Toast.makeText(this@BookSearchActivity, "책 정보 없음", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<BookSearchInfoNaver>, t: Throwable) {
                        Log.d("Approach Fail", "wrong server approach")
                        //Toast.makeText(this@BookSearchActivity, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })*/
                var searchResponse: List<BookItemKakao>?
                BookRetrofitBuilder.api.searchBookKakao(bookTitle, 20).enqueue(object: Callback<BookSearchInfoKakao> {
                    override fun onResponse(call: Call<BookSearchInfoKakao>, response: Response<BookSearchInfoKakao>) {
                        searchResponse = response.body()?.documents
                        if(searchResponse != null) {
                            recyclerView = binding.rvBook
                            recyclerView.layoutManager = LinearLayoutManager(this@BookSearchActivity)
                            val adapter = BookSearchListAdapter(searchResponse)
                            if(searchResponse?.size != 0 ){
                                recyclerView.adapter = adapter
                                adapter.setItemClickListener(object: BookSearchListAdapter.OnItemClickListener {
                                    override fun onClick(v: View, position: Int) {
                                        var bookInfo = searchResponse?.get(position)
                                        val authorSize = bookInfo?.authors?.size?.minus(1)
                                        var bookAuthor = bookInfo?.authors?.get(0)
                                        if (authorSize != null && authorSize > 0) bookAuthor = bookAuthor + " 외 " + authorSize + "명"

                                        var postInfo = PostRequestDTO("", "",
                                            bookInfo?.title, bookAuthor, bookInfo?.thumbnail, "")
                                        var intent = Intent()
                                        intent.putExtra("postInfo", postInfo)
                                        //Log.d("Book Search Post Info", postInfo.toString())
                                        setResult(RESULT_OK, intent)
                                        finish()
                                    }
                                })
                            }
                        } else {
                            Log.d("Search Null", "no book")
                            Toast.makeText(this@BookSearchActivity, "책 정보 없음", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<BookSearchInfoKakao>, t: Throwable) {
                        Log.d("Approach Fail", "wrong server approach")
                        //Toast.makeText(this@BookSearchActivity, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this@BookSearchActivity, "도서 제목을 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 키보드 비활성화 함수
    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editBookTitle.windowToken, 0)
    }
}