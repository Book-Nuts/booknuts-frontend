package kr.co.booknuts.api

import kr.co.booknuts.data.remote.RefreshToken
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface AuthService {
    // 토큰 리프레쉬
    @GET("/auth/refresh")
    fun getTokenRefresh(
        @Header("X-AUTH-TOKEN") refreshtoken: String?,
    ): Call<RefreshToken>
}