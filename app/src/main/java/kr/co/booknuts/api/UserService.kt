package kr.co.booknuts.api

import kr.co.booknuts.data.remote.*
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    // 로그인
    // request body - 이메일, 비밀번호
    @POST("/auth/login")
    fun doLogin(@Body loginRequest: LoginRequestDTO): Call<LoginInfo>

    // 회원가입
    // request body - 아이디, 비밀번호, 이름, 닉네임, 이메일
    @POST("/auth/join")
    fun createUser(@Body joinRequest : JoinRequestDTO): Call<User>

    // 유저 닉네임 중복 체크
    // request body - 닉네임
    @GET("/auth/checkNickname/{nickname}")
    fun checkNickname(@Path("nickname") nickname: String): Call<String>

    // 유저 아이디 중복 체크
    // request body - 아이디
    @GET("/auth/checkLoginId/{loginId}")
    fun checkId(@Path("loginId") loginId: String): Call<String>

    // 유저 정보 조회
    // request body - token
    @GET("/user/info")
    fun getUserInfo(
        @Header("X-AUTH-TOKEN") token: String?,
    ): Call<UserInfo>

    // 유저 프로필 정보 조회
    @GET("/user/profile/{nickname}")
    fun getProfile(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("nickname") nickname: String,
    ): Call<GetProfileResultDTO>
}