package kr.co.booknuts.api

import kr.co.booknuts.data.LoginRequestDTO
import kr.co.booknuts.data.Token
import kr.co.booknuts.data.User
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    // 로그인
    // request body - 이메일, 비밀번호
    @POST("/auth/login")
    fun doLogin(@Body loginInfo: LoginRequestDTO): Call<Token>
}