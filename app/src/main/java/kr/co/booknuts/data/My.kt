package kr.co.booknuts.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class MySeries (
    @SerializedName("seriesId")
    val seriesId: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("imgUrl")
    val imgUrl: String?,
    @SerializedName("totalPost")
    val totalPost: Int?,
    @SerializedName("totalNuts")
    val totalNuts: Int?,
)   :Serializable {}

data class MyArchive (
    @SerializedName("archiveId")
    val archiveId: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("imgUrl")
    val imgUrl: String?,
)   :Serializable {}
