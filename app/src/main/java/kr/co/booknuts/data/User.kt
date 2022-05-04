package kr.co.booknuts.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("email")
    val email: String?,
    @SerializedName("nickname")
    val nickname: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("userId")
    val userId: String?,
    @SerializedName("username")
    val username: String?
)   :Serializable {}

data class LoginRequestDTO(
    @SerializedName("email")
    val email: String?,
    @SerializedName("password")
    val password: String?,
)   :Serializable {}

data class Token (
    @SerializedName("token")
    val token: String?,
)   :Serializable {}