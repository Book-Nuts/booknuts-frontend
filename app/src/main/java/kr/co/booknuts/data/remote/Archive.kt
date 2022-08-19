package kr.co.booknuts.data.remote

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ModifyArchiveRequestDTO(
    @SerializedName("title")
    val title: String?,
    @SerializedName("content")
    val content: String?,
)   : Serializable {}