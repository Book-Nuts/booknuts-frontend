package kr.co.booknuts.data.remote

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
    @SerializedName("timeFromNow")
    val time: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("maxUser")
    val maxUser:Int,
    @SerializedName("curYesUser")
    val curYesUser: Int,
    @SerializedName("curNoUser")
    val curNoUser: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("owner")
    val owner: String,
): Serializable {}

data class DebateJoinableDTO(
    @SerializedName("canJoinYes")
    val canJoinPros: Boolean,
    @SerializedName("canJoinNo")
    val canJoinCons: Boolean,
)

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
    @SerializedName("author")
    val author: String,
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

data class DebateCreateDTO (
    @SerializedName("bookTitle")
    val title: String,
    @SerializedName("bookAuthor")
    val author: String,
    @SerializedName("bookImgUrl")
    val bookImgUrl: String,
    @SerializedName("bookGenre")
    val genre: String,
    @SerializedName("topic")
    val topic: String,
    @SerializedName("coverImgUrl")
    val coverImgURl: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("maxUser")
    val maxUser: Int,
    @SerializedName("opinion")
    val opinion: Boolean
)   :Serializable {}

data class DebateJoinDTO (
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
    @SerializedName("timeFromNow")
    val time: String,
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