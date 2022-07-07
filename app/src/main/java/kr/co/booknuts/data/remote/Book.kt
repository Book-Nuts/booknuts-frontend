package kr.co.booknuts.data.remote

import com.google.gson.annotations.SerializedName
import java.io.Serializable

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
)   : Serializable {}

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
    val price: String?,
    @SerializedName("discount")
    val discount: String?,
    @SerializedName("publisher")
    val publisher: String?,
    @SerializedName("isbn")
    val isbn: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("pubdate")
    val pubdate: String?,
)   : Serializable {}