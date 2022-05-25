package kr.co.booknuts.api

import android.text.Editable
import kr.co.booknuts.data.DebateCreateDTO
import kr.co.booknuts.data.DebateListRequestDTO
import kr.co.booknuts.data.DebateRoom
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
}