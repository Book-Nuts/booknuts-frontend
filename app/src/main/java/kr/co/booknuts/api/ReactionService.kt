package kr.co.booknuts.api

import kr.co.booknuts.data.remote.HeartResult
import kr.co.booknuts.data.remote.NutsResult
import kr.co.booknuts.data.remote.Post
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReactionService {
    // 좋아요 클릭
    // request - token, boardId
    @PUT("/reaction/heart/{boardId}")
    fun putHeart(
        @Header("X-AUTH-TOKEN") accessToken: String?,
        @Path("boardId") boardId: Int?,
    ): Call<HeartResult>

    // 넛츠 클릭
    // request - token, boardId
    @PUT("/reaction/nuts/{boardId}")
    fun putNuts(
        @Header("X-AUTH-TOKEN") accessToken: String?,
        @Path("boardId") boardId: Int?,
    ): Call<NutsResult>
}