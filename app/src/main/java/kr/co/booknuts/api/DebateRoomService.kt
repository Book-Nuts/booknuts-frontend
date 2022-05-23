package kr.co.booknuts.api

import kr.co.booknuts.data.DebateListRequestDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface DebateRoomService {
    // 탭에 따른 토론장 리스트 반환(토론장 홈 화면 - 텍스트/음성/전체)
    @GET("/debate/list/{type}")
    fun roomList(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("type") type: Int,
    ): Call<DebateListRequestDTO>
}