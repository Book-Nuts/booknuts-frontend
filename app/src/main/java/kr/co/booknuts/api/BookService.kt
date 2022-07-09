package kr.co.booknuts.api

import kr.co.booknuts.data.remote.BookSearchInfoNaver
import kr.co.booknuts.data.remote.BookSearchInfoKakao
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    // 책 검색
    // request body - 책 제목
    @GET("book.json")
    fun searchBookNaver(
        @Query("query") bookTitle: String?
    ): Call<BookSearchInfoNaver>

    // 책 검색
    @GET("book")
    fun searchBookKakao(
        @Query("query") bookTitle: String?,
        @Query("size") size: Int?
    ): Call<BookSearchInfoKakao>
}