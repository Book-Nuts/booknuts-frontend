package kr.co.booknuts.retrofit

import com.google.gson.GsonBuilder
import kr.co.booknuts.BuildConfig.BASE_URL
import kr.co.booknuts.api.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    // 사용할 api 인터페이스 선언
    var api: UserService
    var boardApi: BoardService
    var myApi: MyService
    var debateApi: DebateRoomService
    var authApi: AuthService

    val gson = GsonBuilder().setLenient().create()

    init {
        // api 서버 연결
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(UserService::class.java)
        boardApi = retrofit.create(BoardService::class.java)
        myApi = retrofit.create(MyService::class.java)
        debateApi = retrofit.create(DebateRoomService::class.java)
        authApi = retrofit.create(AuthService::class.java)
    }
}