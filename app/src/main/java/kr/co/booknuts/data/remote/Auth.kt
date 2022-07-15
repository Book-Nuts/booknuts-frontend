package kr.co.booknuts.data.remote

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RefreshToken(
    @SerializedName("accessToken")
    val accessToken: String?,
    @SerializedName("refreshToken")
    val refreshToken: String?,
)   : Serializable {}