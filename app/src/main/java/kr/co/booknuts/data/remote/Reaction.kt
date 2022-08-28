package kr.co.booknuts.data.remote

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HeartResult(
    @SerializedName("result")
    val result: String?
)   : Serializable {}

data class NutsResult(
    @SerializedName("result")
    val result: String?
)   : Serializable {}