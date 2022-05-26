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
    @SerializedName("archiveCnt")
    val archiveCnt: Int?,
    @SerializedName("createdAt")
    val createdAt: String?,
)   :Serializable {}

data class SeriesRequestDTO (
    @SerializedName("title")
    val title: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("imgUrl")
    val imgUrl: String?,
    @SerializedName("boardIdlist")
    val boardIdlist: List<Long>,
)   :Serializable {}

data class ArchiveRequestDTO (
    @SerializedName("title")
    val title: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("imgUrl")
    val imgUrl: String?,
)   :Serializable {}

data class ResultData (
    @SerializedName("result")
    val result: String?,
)   :Serializable {}


