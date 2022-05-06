package kr.co.booknuts.api

import kr.co.booknuts.data.JoinRequestDTO
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

    // 회원가입
    // request body - 아이디, 비밀번호, 이름, 닉네임, 이메일
    @POST("/auth/join")
    fun createUser(@Body joinInfo : JoinRequestDTO): Call<User>

    // 유저 닉네임 중복 체크
    // request body - 닉네임
    @GET("/auth/checkNickname/{nickname}")
    fun checkNicknameDuplication(@Path("nickname") nickname: String): Call<String>
}