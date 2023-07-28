package io.github.xiue233.book123.model

import android.text.TextUtils
import com.google.gson.annotations.SerializedName
import io.github.xiue233.book123.network.Book123Service

interface Book {
    val isbn: String
    val title: String
    val img: String
    val author: String
    val imgHost: String
    val downloadHost: String

    fun getImgUrl(
        imgHost: String = Book123Service.IMG_HOST_URL
    ) = imgHost + img
}

data class BookPreview(
    override val isbn: String,
    override val title: String,
    @SerializedName("img")
    override val img: String,
    override val author: String,
    override val imgHost: String = Book123Service.IMG_HOST_URL,
    override val downloadHost: String = Book123Service.DOWNLOAD_HOST_URL,
) : Book

data class BookSummary(
    override val isbn: String,
    override val title: String,
    @SerializedName("img")
    override val img: String,
    override val author: String,
    val pubDate: String,
    val tags: List<String>,
    val rate: String,
    val summary: String,
    override val imgHost: String = Book123Service.IMG_HOST_URL,
    override val downloadHost: String = Book123Service.DOWNLOAD_HOST_URL,
) : Book

data class BookDetail(
    override val isbn: String,
    override val title: String,
    @SerializedName("img")
    override val img: String,
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
    override val imgHost: String = Book123Service.IMG_HOST_URL,
    override val downloadHost: String = Book123Service.DOWNLOAD_HOST_URL,
) : Book {
    fun canDownload() = TextUtils.isEmpty(downloadUrl)
}
