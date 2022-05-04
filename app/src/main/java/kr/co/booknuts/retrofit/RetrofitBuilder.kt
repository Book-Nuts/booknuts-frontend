package kr.co.booknuts.retrofit

import com.google.gson.GsonBuilder
import kr.co.booknuts.api.UserService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST

object RetrofitBuilder {
    // 사용할 api 인터페이스 선언
    var api: UserService

    val gson = GsonBuilder().setLenient().create()

    init {
        // api 서버 연결
        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-38-4-56.ap-northeast-2.compute.amazonaws.com:8080")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(UserService::class.java)
    }
}