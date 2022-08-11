package kr.co.booknuts.api

import kr.co.booknuts.data.remote.Comment
import kr.co.booknuts.data.remote.CommentRequestDTO
import kr.co.booknuts.data.remote.RefreshToken
import retrofit2.Call
import retrofit2.http.*

interface CommentService {
    // 댓글 조회
    @GET("/comment/{boardId}")
    fun getCommentList(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("boardId") boardId: Long?,
    ): Call<Array<Comment>>

    // 댓글 작성
    @POST("/comment/{boardId}/write")
    fun writeComment(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("boardId") boardId: Long?,
        @Body commentRequest : CommentRequestDTO
    ): Call<Comment>
}