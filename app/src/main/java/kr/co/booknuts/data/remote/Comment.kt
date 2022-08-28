package kr.co.booknuts.data.remote

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Comment(
    @SerializedName("commentId")
    val commentId: Long?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("createdDate")
    val createdDate: String?,
    @SerializedName("writer")
    val writer: String?,
    @SerializedName("boardId")
    val boardId: Long?,
    @SerializedName("parentId")
    val parentId: Long?,
)   : Serializable {}

data class CommentRequestDTO(
    @SerializedName("content")
    val content: String?,
)   : Serializable {}
