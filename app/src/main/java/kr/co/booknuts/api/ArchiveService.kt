package kr.co.booknuts.api

import kr.co.booknuts.data.remote.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

interface ArchiveService {
    // 아카이브 생성
    // request body - 제목, 내용, 책표지
    @Multipart
    @POST("/archive/create")
    fun postArchive(
        @Header("X-AUTH-TOKEN") token: String?,
        @Part file: MultipartBody.Part?,
        @Part archive: MultipartBody.Part,
    ): Call<MyArchive>

    // 아카이브 삭제
    // request body - 아카이브 아이디
    @DELETE("/archive/{archiveId}")
    fun deleteArchive(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("archiveId") archiveId: Long?,
    ): Call<DeleteResult>

    // 아카이브 수정
    // request body - 아카이브 아이디, 제목, 내용
    @PATCH("/archive/{archiveId}")
    fun modifyArchive(
        @Header("X-AUTH-TOKEN") token: String?,
        @Path("archiveId") archiveId: Long?,
        @Body body: ModifyArchiveRequestDTO
    ): Call<MyArchive>
}