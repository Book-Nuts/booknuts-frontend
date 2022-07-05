package kr.co.booknuts.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class BookSearchInfo (
    @SerializedName("lastBuildDate")
    val lastBuildDate: String?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("start")
    val start: Int?,
    @SerializedName("display")
    val display: Int?,
    @SerializedName("items")
    val item: List<BookItem>?,
)   :Serializable {}

data class BookItem(
    @SerializedName("title")
    val title: String?,
    @SerializedName("link")
    val link: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("author")
    val author: String?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("discount")
    val discount: String?,
    @SerializedName("publisher")
    val publisher: String?,
    @SerializedName("isbn")
    val isbn: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("pubdate")
    val pubdate: String?,
)   : Serializable {}

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