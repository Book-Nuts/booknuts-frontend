package kr.co.booknuts.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class User(
    @SerializedName("userId")
    val userId: Long?,
    @SerializedName("loginId")
    val loginId: String?,
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
    @SerializedName("loginId")
    val loginId: String?,
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

data class BookSearchRequestDTO (
    @SerializedName("query")
    val query: String?,
)   :Serializable {}