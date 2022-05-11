package kr.co.booknuts.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

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
    val price: Int?,
    @SerializedName("discount")
    val discount: Int?,
    @SerializedName("publisher")
    val publisher: String?,
    @SerializedName("isbn")
    val isbn: Int?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("pubdate")
    val pubdate: Date?,
)   : Serializable {}