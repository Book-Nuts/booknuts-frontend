package kr.co.booknuts.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

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
    @SerializedName("refreshToken")
    val refreshToken: String?,
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
    @SerializedName("userId")
    val userId: Int?,
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
    @SerializedName("refreshToken")
    val refreshToken: String?,
    )   :Serializable {}

data class UserInfo (
    @SerializedName("userId")
    val userId: Int?,
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
    @SerializedName("refreshToken")
    val refreshToken: String?,
)   :Serializable {}
