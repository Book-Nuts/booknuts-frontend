package kr.co.booknuts.api

import kr.co.booknuts.data.remote.*
import retrofit2.Call
import retrofit2.http.*

interface DebateRoomService {
    // 탭에 따른 토론장 리스트 반환(토론장 홈 화면 - 텍스트/음성/전체)
    @GET("/debate/list/{type}")
    fun roomList(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("type") type: Int,
    ): Call<DebateListRequestDTO>

    // 토론장 개설
    @POST("/debate/create")
    fun debateCreate(
        @Header("X-AUTH-TOKEN") token: String,
        @Body debateInfo: DebateCreateDTO,
    ): Call<DebateRoom>

    // 찬반 참여 가능 여부
    @GET("/debate/canenter/{roomId}")
    fun joinable(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("roomId") roomId: Long,
    ) : Call<DebateJoinableDTO>

    // 토론장 참여
    @PATCH("/debate/enter/{roomId}")
    fun join(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("roomId") roomId: Long,
        @Query("opinion") opinion: Boolean,
    ) : Call<DebateJoinDTO>

    // 토론장 상태 변경
    @PATCH("/debate/update/{roomId}")
    fun activate(
        @Header("X-AUTH-TOKEN") token: String,
        @Path("roomId") roomId: Long,
        @Query("status") status: Int,
    ) : Call<DebateJoinDTO>
}