package kr.co.booknuts.retrofit

import com.google.gson.GsonBuilder
import kr.co.booknuts.BuildConfig.*
import kr.co.booknuts.api.BookService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object BookRetrofitBuilder {
    // 사용할 api 인터페이스 선언
    var api: BookService

    val gson = GsonBuilder().setLenient().create()

    private fun provideOkHttpClient(
        interceptor: AppInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .run{
            addInterceptor(interceptor)
            build()
        }

    class AppInterceptor: Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain)
                : Response = with(chain) {
            val newRequest = request().newBuilder()
                //.addHeader("X-Naver-Client-Id", NAVER_CLIENT_ID)
                //.addHeader("X-Naver-Client-Secret", NAVER_CLIENT_SECRET)
                .addHeader("Authorization", "KakaoAK " + KAKAO_REST_API_KEY)
                .build()

            proceed(newRequest)
        }
    }

    init {
        // api 서버 연결
        val retrofitNaver = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com/v1/search/")
            .client(provideOkHttpClient(AppInterceptor()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val retrofitKakao = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/v3/search/")
            .client(provideOkHttpClient(AppInterceptor()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        //api = retrofitNaver.create(BookService::class.java)
        api = retrofitKakao.create(BookService::class.java)
    }
}