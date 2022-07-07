package kr.co.booknuts.api

import kr.co.booknuts.data.remote.*
import retrofit2.Call
import retrofit2.http.*

interface MyService {
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

    // 나의 아카이브 조회
    // request body - token
    @GET("/archive/list")
    fun getMyArchiveList(
        @Header("X-AUTH-TOKEN") token: String?,
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
    @POST("/series/create")
    fun postSeries(
        @Header("X-AUTH-TOKEN") token: String?,
        @Body seriesInfo : SeriesRequestDTO
    ): Call<MySeries>

    // 아카이브 생성
    // request body - 제목, 내용, 책표지
    @POST("/archive/create")
    fun postArchive(
        @Header("X-AUTH-TOKEN") token: String?,
        @Body seriesInfo : ArchiveRequestDTO
    ): Call<MyArchive>

    // 아카이브에 게시글 추가하기
    // request body - 제목, 내용, 책표지
    @PATCH("/archive/add/{archiveId}/{boardId}")
    fun addPostToArchive(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("archiveId") archiveId: Int?,
        @Path("boardId") boardId: Int?,
    ): Call<ResultData>
}