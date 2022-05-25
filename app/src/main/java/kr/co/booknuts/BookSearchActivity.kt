package kr.co.booknuts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.booknuts.adapter.BoardListAdapter
import kr.co.booknuts.adapter.BookSearchListAdapter
import kr.co.booknuts.data.*
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

        binding.imgClose.setOnClickListener{
            finish()
        }

        binding.imgSearch.setOnClickListener {
            var bookTitle = binding.editBookTitle.text.toString()
            Toast.makeText(this@BookSearchActivity, "Title: " + bookTitle, Toast.LENGTH_SHORT).show()

            if(!bookTitle.isEmpty()) {
                var searchResponse: List<BookItem>?
                bookTitle = "하"
                BookRetrofitBuilder.api.searchBook(bookTitle).enqueue(object: Callback<BookSearchInfo> {
                    override fun onResponse(call: Call<BookSearchInfo>, response: Response<BookSearchInfo>) {
                        Toast.makeText(this@BookSearchActivity, "통신 성공", Toast.LENGTH_SHORT).show()
                        searchResponse = response.body()?.item
                        if(searchResponse != null) {
                            //Log.d("Search Success", searchResponse.toString())
                            Toast.makeText(this@BookSearchActivity, "Search Success", Toast.LENGTH_SHORT).show()
                            recyclerView = binding.rvBook
                            recyclerView.layoutManager = LinearLayoutManager(this@BookSearchActivity)
                            val adapter: BookSearchListAdapter = BookSearchListAdapter(searchResponse)
                            if(searchResponse?.size != 0 ){
                                recyclerView.adapter = adapter
                                adapter.setItemClickListener(object: BookSearchListAdapter.OnItemClickListener {
                                    override fun onClick(v: View, position: Int) {
                                        var bookInfo = searchResponse?.get(position)
                                        var postInfo = PostRequestDTO("", "",bookInfo?.title, bookInfo?.author, bookInfo?.image, "")
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
                    override fun onFailure(call: Call<BookSearchInfo>, t: Throwable) {
                        Log.d("Approach Fail", "wrong server approach")
                        Toast.makeText(this@BookSearchActivity, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this@BookSearchActivity, "도서 제목을 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}