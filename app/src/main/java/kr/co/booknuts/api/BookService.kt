package kr.co.booknuts.api

import kr.co.booknuts.data.remote.BookSearchInfo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    // 책 검색
    // request body - 책 제목
    @GET("book.json")
    fun searchBook(
        @Query("query") bookTitle: String?
    ): Call<BookSearchInfo>
}