package kr.co.booknuts.data.remote

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Post(
    @SerializedName("boardId")
    val boardId: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("writer")
    val writer: String?,
    @SerializedName("createdDate")
    val createdDate: String?,
    @SerializedName("bookTitle")
    val bookTitle: String?,
    @SerializedName("bookAuthor")
    val bookAuthor: String?,
    @SerializedName("bookImgUrl")
    val bookImgUrl: String?,
    @SerializedName("bookGenre")
    val bookGenre: String?,
    @SerializedName("nutsCnt")
    val nutsCnt: Int?,
    @SerializedName("heartCnt")
    val heartCnt: Int?,
    @SerializedName("archiveCnt")
    val archiveCnt: Int?,
    @SerializedName("isNuts")
    val isNuts: Boolean?,
    @SerializedName("isHeart")
    val isHeart: Boolean?,
    @SerializedName("isArchived")
    val isArchived: Boolean?,
    @SerializedName("curUser")
    val curUser: Boolean?,
)   : Serializable {}

data class PostDetail(
    @SerializedName("boardId")
    val boardId: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("writer")
    val writer: String?,
    @SerializedName("createdDate")
    val createdDate: String?,
    @SerializedName("bookTitle")
    val bookTitle: String?,
    @SerializedName("bookAuthor")
    val bookAuthor: String?,
    @SerializedName("bookImgUrl")
    val bookImgUrl: String?,
    @SerializedName("bookGenre")
    val bookGenre: String?,
    @SerializedName("nutsCnt")
    val nutsCnt: Int?,
    @SerializedName("heartCnt")
    val heartCnt: Int?,
    @SerializedName("archiveCnt")
    val archiveCnt: Int?,
    @SerializedName("isNuts")
    val isNuts: Boolean?,
    @SerializedName("isHeart")
    val isHeart: Boolean?,
    @SerializedName("isArchived")
    val isArchived: Boolean?,
    @SerializedName("curUser")
    val curUser: Boolean?,
)   : Serializable {}

data class PostRequestDTO(
    @SerializedName("title")
    val title: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("bookTitle")
    var bookTitle: String?,
    @SerializedName("bookAuthor")
    var bookAuthor: String?,
    @SerializedName("bookImgUrl")
    var bookImgUrl: String?,
    @SerializedName("bookGenre")
    val bookGenre: String?,
)   : Serializable {}