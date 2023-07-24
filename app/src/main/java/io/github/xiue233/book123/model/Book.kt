package io.github.xiue233.book123.model

import android.text.TextUtils
import com.google.gson.annotations.SerializedName

interface Book {
    val isbn: String
    val title: String
    val imgUrl: String
    val author: String
}

data class BookPreview(
    override val isbn: String,
    override val title: String,
    override val imgUrl: String,
    override val author: String,
) : Book

data class BookSummary(
    override val isbn: String,
    override val title: String,
    override val imgUrl: String,
    override val author: String,
    val pubDate: String,
    val tags: List<String>,
    val rate: String,
    val summary: String,
) : Book

data class BookDetail(
    override val isbn: String,
    override val title: String,
    @SerializedName("img")
    override val imgUrl: String,
    override val author: String,
    val binding: String,
    @SerializedName("catelogues") // A typo in json
    val catalogues: List<String>,
    val comments: List<String>,
    val lastUpdate: String,
    val price: String,
    val pubDate: String,
    val publisher: String,
    val rate: String,
    val summary: String,
    val tags: List<String>,
    val fileSize: Long,
    val fileType: String,
    val downloadUrl: String,
) : Book {
    fun canDownload() = TextUtils.isEmpty(downloadUrl)
}
