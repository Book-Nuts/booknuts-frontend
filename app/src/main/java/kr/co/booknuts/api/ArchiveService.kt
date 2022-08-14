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
        @Part("file") file: File?,
        @Part("archive") archive: ArchiveRequestDTO
    ): Call<MyArchive>
}