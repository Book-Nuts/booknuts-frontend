package kr.co.booknuts.api

import kr.co.booknuts.data.remote.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

interface MyService {
    // 유저의 게시글 조회
    // request body - token
    @GET("/board/post/{userId}")
    fun getMyPostList(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("userId") userId: Int?,
    ): Call<ArrayList<Post>>

    // 유저의 시리즈 조회
    // request body - token
    @GET("/series/list/{userId}")
    fun getMySeriesList(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("userId") userId: Int?,
    ): Call<ArrayList<MySeries>>

    // 유저의 아카이브 조회
    // request body - token
    @GET("/archive/list/{userId}")
    fun getMyArchiveList(
        @Header("X-AUTH-TOKEN")  token: String?,
        @Path("userId") userId: Int?,
    ): Call<ArrayList<MyArchive>>

    // 나의 시리즈 세부 조회
    // request body - token
    @GET("/series/{seriesId}")
    fun getMySeriesDetailPost(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("seriesId") seriesId: Int?
    ): Call<ArrayList<PostDetail>>

    // 나의 아카이브 세부 조회
    // request body - token
    @GET("/archive/{archiveId}")
    fun getMyArchiveDetail(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("archiveId") archiveId: Int?
    ): Call<ArrayList<PostDetail>>

    // 시리즈 생성
    // request body - 제목, 내용, 책표지, 게시글 리스트
    @Multipart
    @POST("/series/create")
    fun createSeries(
        @Header("X-AUTH-TOKEN") token: String?,
        @Part("file") file: File?,
        @Part("series") series: SeriesRequestDTO
    ): Call<MySeries>

    // 아카이브에 게시글 추가하기
    // request body - 제목, 내용, 책표지
    @PATCH("/archive/add/{archiveId}/{boardId}")
    fun addPostToArchive(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("archiveId") archiveId: Long?,
        @Path("boardId") boardId: Int?,
    ): Call<ResultData>
}