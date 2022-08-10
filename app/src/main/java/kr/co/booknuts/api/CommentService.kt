package kr.co.booknuts.api

import kr.co.booknuts.data.remote.Comment
import kr.co.booknuts.data.remote.RefreshToken
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface CommentService {
    // 댓글 조회
    @GET("/comment/{boardId}")
    fun getCommentList(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("boardId") boardId: Long?,
    ): Call<Array<Comment>>
}