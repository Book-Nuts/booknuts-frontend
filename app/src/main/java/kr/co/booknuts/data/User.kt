package kr.co.booknuts.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class User(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("userId")
    val userId: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("accessToken")
    val accessToken: String?,
    @SerializedName("roles")
    val roles: List<String>?,
)   :Serializable {}

data class LoginRequestDTO(
    @SerializedName("email")
    val email: String?,
    @SerializedName("password")
    val password: String?,
)   :Serializable {}

data class JoinRequestDTO(
    @SerializedName("userId")
    val userId: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("email")
    val email: String?,
)   :Serializable {}

data class Token (
    @SerializedName("token")
    val token: String?,
)   :Serializable {}

data class BookSearchInfo (
    @SerializedName("lastBuildDate")
    val lastBuildDate: Date?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("start")
    val start: Int?,
    @SerializedName("display")
    val display: Int?,
    @SerializedName("item")
    val item: List<BookItem>?,
)   :Serializable {}