package kr.co.booknuts.api

import kr.co.booknuts.data.remote.*
import retrofit2.Call
import retrofit2.http.*

interface BoardService {
    // 전체 게시글 검색
    // request body - type
    @GET("/board/list/{type}")
    fun getBoardList(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("type") type: Int
    ): Call<ArrayList<Post>>

    // 게시글 상세 가져오기
    @GET("/board/{boardId}")
    fun getPostDetail(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("boardId") boardId: Long?
    ): Call<PostDetail>

    // 게시글 작성
    // request body - 제목, 내용, 책제목, 작가, 책표지, 장르
    @POST("/board/write")
    fun doPost(
        @Header("X-AUTH-TOKEN") token: String?,
        @Body postInfo : PostRequestDTO
    ): Call<PostDetail>

    // 게시글 삭제
    // request body - boardId
    @DELETE("/board/{boardId}")
    fun deletePost(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("boardId") boardId: Long?
    ): Call<DeleteResult>

    // 게시글 수정
    // request body - boardId
    @PATCH("/board/{boardId}")
    fun editPost(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("boardId") boardId: Long?,
        @Body editInfo: PostEditInfo
    ): Call<PostDetail>
}