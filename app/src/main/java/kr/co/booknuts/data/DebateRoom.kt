package kr.co.booknuts.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DebateRoom(
    @SerializedName("roomId")
    val roomId: Int,
    @SerializedName("bookTitle")
    val bookTitle: String,
    @SerializedName("bookAuthor")
    val bookAuthor: String,
    @SerializedName("bookImgUrl")
    val bookImgUrl: String,
    @SerializedName("bookGenre")
    val bookGenre: String,
    @SerializedName("topic")
    val topic: String,
    @SerializedName("coverImgUrl")
    val coverImgUrl: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("curYesUser")
    val curYesUser: Int,
    @SerializedName("curNoUser")
    val curNoUser: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("owner")
    val owner: String,
): Serializable {}

data class DebateListRequestDTO(
    @SerializedName("맞춤 토론")
    val personalizedDebate: List<DebateRoom>,
    @SerializedName("현재 진행 중인 토론")
    val proceedingDebate: List<DebateRoom>,
    @SerializedName("현재 대기 중인 토론")
    val waitDebate: List<DebateRoom>,
): Serializable {}

data class DebateSearchInfo(
    @SerializedName("id")
    val id: Int,
    @SerializedName("topic")
    val topic: String,
    @SerializedName("bookTitle")
    val bookTitle: String,
    @SerializedName("coverImg")
    val coverImg: String,
    @SerializedName("pros")
    val pros: Int,
    @SerializedName("cons")
    val cons: Int,
    @SerializedName("time")
    val time: String,
    @SerializedName("owner")
    val owner: String,
    @SerializedName("participants")
    val participants: Int,
)