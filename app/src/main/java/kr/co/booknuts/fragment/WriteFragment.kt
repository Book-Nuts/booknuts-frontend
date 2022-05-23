package kr.co.booknuts.fragment

import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_write.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.co.booknuts.MainActivity
import kr.co.booknuts.R
import kr.co.booknuts.data.*
import kr.co.booknuts.databinding.FragmentWriteBinding
import kr.co.booknuts.retrofit.BookRetrofitBuilder
import kr.co.booknuts.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL

class WriteFragment : Fragment() {

    //lateinit var binding: FragmentWriteBinding

    val binding by lazy { FragmentWriteBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentWriteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBookTitle.setOnClickListener {
            val bookTitle = binding.editBookTitle.text.toString()
            Toast.makeText(activity, "Title: " + bookTitle, Toast.LENGTH_SHORT).show()

            if(!bookTitle.isEmpty()) {
                var searchResponse: List<BookItem>? = null

                BookRetrofitBuilder.api.searchBook(bookTitle).enqueue(object: Callback<BookSearchInfo> {
                    override fun onResponse(call: Call<BookSearchInfo>, response: Response<BookSearchInfo>) {
                        Toast.makeText(activity, "통신 성공", Toast.LENGTH_SHORT).show()
                        searchResponse = response.body()?.item
                        if(searchResponse != null) {
                            for(item in searchResponse!!){
                                //Log.d("Search1", "Search : " + item.image)
                                CoroutineScope(Dispatchers.Main).launch {
                                    val bitmap = withContext(Dispatchers.IO){
                                        BitmapFactory.decodeStream(URL(item.image).openStream())
                                    }
                                    binding.imgBook.setImageBitmap(bitmap)
                                }

                            }
                            //Log.d("Search Success", "Search : " + searchResponse)
                            Toast.makeText(activity, "Search: ", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("Search Null", "no book")
                            Toast.makeText(activity, "책 정보 없음", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<BookSearchInfo>, t: Throwable) {
                        Log.d("Approach Fail", "wrong server approach")
                        Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(activity, "도서 제목을 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}