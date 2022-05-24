package kr.co.booknuts.api

import kr.co.booknuts.data.*
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

    // 책 검색
    // request body - 책 제목
    @GET("book.json")
    fun searchBook(
        @Query("query") bookTitle: String?
    ): Call<BookSearchInfo>

    // 유저 닉네임 중복 체크
    // request body - 닉네임
    @GET("/auth/checkNickname/{nickname}")
    fun checkNickname(@Path("nickname") nickname: String): Call<String>

    // 유저 아이디 중복 체크
    // request body - 아이디
    @GET("/auth/checkLoginId/{loginId}")
    fun checkId(@Path("loginId") loginId: String): Call<String>

    // 전체 게시글 검색
    // request body - type
    @GET("/board/list/{type}")
    fun getBoardList(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("type") type: Int
    ): Call<ArrayList<Post>>

    // 유저 정보 조회
    // request body - token
    @GET("/auth/userinfo")
    fun getUserInfo(
        @Header("X-AUTH-TOKEN") token: String?,
    ): Call<UserInfo>

    // 내가 쓴 게시글 조회
    // request body - token
    @GET("/board/mypost")
    fun getMyPostList(
        @Header("X-AUTH-TOKEN") token: String?,
    ): Call<ArrayList<Post>>

    // 나의 시리즈 조회
    // request body - token
    @GET("/series/list")
    fun getMySeriesList(
        @Header("X-AUTH-TOKEN") token: String?,
    ): Call<ArrayList<MySeries>>

    // 나의 시리즈 세부 조회
    // request body - token
    @GET("/series/{seriesId}")
    fun getMySeriesDetailPost(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("seriesId") seriesId: Int?
    ): Call<ArrayList<PostDetail>>

    // 게시글 상세 가져오기
    @GET("/board/{boardId}")
    fun getPostDetail(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("boardId") boardId: Long?
    ): Call<PostDetail>
}