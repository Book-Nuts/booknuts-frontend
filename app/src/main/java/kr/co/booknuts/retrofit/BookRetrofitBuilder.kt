package kr.co.booknuts.retrofit

import com.google.gson.GsonBuilder
import kr.co.booknuts.BuildConfig.BASE_URL
import kr.co.booknuts.api.UserService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object BookRetrofitBuilder {
    // 사용할 api 인터페이스 선언
    var api: UserService

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
                .addHeader("X-Naver-Client-Id", "_72aHxgLB7HtfAoc9iQC")
                .addHeader("X-Naver-Client-Secret", "QbgL7NEhC5")
                .build()

            proceed(newRequest)
        }
    }

    init {
        // api 서버 연결
        val retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com/v1/search/")
            .client(provideOkHttpClient(AppInterceptor()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(UserService::class.java)
    }
}