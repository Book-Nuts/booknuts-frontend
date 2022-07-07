package kr.co.booknuts.data.remote

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BookSearchInfoNaver (
    @SerializedName("lastBuildDate")
    val lastBuildDate: String?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("start")
    val start: Int?,
    @SerializedName("display")
    val display: Int?,
    @SerializedName("items")
    val item: List<BookItemNaver>?,
)   : Serializable {}

data class BookItemNaver(
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

data class BookSearchInfoKakao (
    @SerializedName("meta")
    val meta: BookMetaKakao?,
    @SerializedName("documents")
    val documents: List<BookItemKakao>?,
)   : Serializable {}

data class BookMetaKakao(
    @SerializedName("is_end")
    val is_end: Boolean?,
    @SerializedName("pageable_count")
    val pageable_count: Int?,
    @SerializedName("total_count")
    val total_count: Int?,
)  : Serializable {}

data class BookItemKakao(
    @SerializedName("authors")
    val authors: List<String>?,
    @SerializedName("contents")
    val contents: String?,
    @SerializedName("datetime")
    val datetime: String?,
    @SerializedName("isbn")
    val isbn: String?,
    @SerializedName("price")
    val price: Int?,
    @SerializedName("publisher")
    val publisher: String?,
    @SerializedName("sale_price")
    val sale_price: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("translators")
    val translators: List<String>?,
    @SerializedName("url")
    val url: String?,
    )   : Serializable {}

