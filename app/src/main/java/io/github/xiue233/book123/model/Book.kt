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
}

interface IBookPreview : Book

abstract class AbstractBookPreview : IBookPreview {
    fun getImgUrl(
        imgHost: String = Book123Service.IMG_HOST_URL
    ) = imgHost + img
}

interface IBookSummary : IBookPreview {
    val pubDate: String
    val tags: List<String>
    val rate: String
    val summary: String
}

interface IBookDetail : IBookSummary {
    val binding: String
    val catalogues: List<String>
    val comments: List<String>
    val lastUpdate: String
    val price: String
    val publisher: String
    val fileSize: Long
    val fileType: String
    val downloadUrl: String
}

data class BookPreview(
    override val isbn: String,
    override val title: String,
    @SerializedName("img")
    override val img: String,
    override val author: String,
    override val imgHost: String = Book123Service.IMG_HOST_URL,
    override val downloadHost: String = Book123Service.DOWNLOAD_HOST_URL,
) : AbstractBookPreview()

data class BookSummary(
    override val isbn: String,
    override val title: String,
    @SerializedName("img")
    override val img: String,
    override val author: String,
    override val pubDate: String,
    override val tags: List<String>,
    override val rate: String,
    override val summary: String,
    override val imgHost: String = Book123Service.IMG_HOST_URL,
    override val downloadHost: String = Book123Service.DOWNLOAD_HOST_URL,
) : IBookSummary, AbstractBookPreview()

data class BookDetail(
    override val isbn: String,
    override val title: String,
    @SerializedName("img")
    override val img: String,
    override val author: String,
    override val binding: String,
    @SerializedName("catelogues") // A typo in json
    override val catalogues: List<String>,
    override val comments: List<String>,
    override val lastUpdate: String,
    override val price: String,
    override val pubDate: String,
    override val publisher: String,
    override val rate: String,
    override val summary: String,
    override val tags: List<String>,
    override val fileSize: Long,
    override val fileType: String,
    override val downloadUrl: String,
    override val imgHost: String = Book123Service.IMG_HOST_URL,
    override val downloadHost: String = Book123Service.DOWNLOAD_HOST_URL,
) : IBookDetail, AbstractBookPreview() {
    fun canDownload() = TextUtils.isEmpty(downloadUrl)
}
