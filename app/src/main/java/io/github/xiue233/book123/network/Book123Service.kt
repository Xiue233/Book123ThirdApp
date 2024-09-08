package io.github.xiue233.book123.network

import com.skydoves.sandwich.ApiResponse
import io.github.xiue233.book123.model.BookDetail
import io.github.xiue233.book123.model.BookPreview
import io.github.xiue233.book123.model.BookSearchResult
import io.github.xiue233.book123.model.CheckBookFileResult
import io.github.xiue233.book123.model.RelatedBooksResult
import io.github.xiue233.book123.model.SimpleSearchResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object BookTags {
    const val RECENT_UPDATE = "最近更新"
    const val LITERATURE = "文学"
    const val SOCIETY_CULTURE = "社会文化"
    const val HISTORY = "历史"
    const val ECONOMICS = "经济"
    const val SCIENCE_AND_ENGINEERING = "理工科"
    const val POLITICS = "政治"
    const val HEALTH = "健康"
    const val NATURAL_SCIENCE = "自然科学"
    const val COMPUTER = "计算机"
    const val DESIGN = "设计"
    const val FOOD_AND_TRAVEL = "美食旅行"
    const val THOUGHTS = "思想"
    const val BIOLOGY = "生物"
    const val ARCHITECTURE = "建筑"
    const val PICTURE_BOOK = "绘本"
    const val ASTRONOMICAL = "天文"

    val TAGS: List<String> = ArrayList<String>().apply {
        add(RECENT_UPDATE)
        add(LITERATURE)
        add(SOCIETY_CULTURE)
        add(HISTORY)
        add(ECONOMICS)
        add(SCIENCE_AND_ENGINEERING)
        add(POLITICS)
        add(HEALTH)
        add(NATURAL_SCIENCE)
        add(COMPUTER)
        add(DESIGN)
        add(FOOD_AND_TRAVEL)
        add(THOUGHTS)
        add(BIOLOGY)
        add(ARCHITECTURE)
        add(PICTURE_BOOK)
        add(ASTRONOMICAL)
    }

}

interface Book123Service {
    companion object {
        const val BASE_URL = "https://www.book345.com/"
        const val IMG_HOST_URL = "https://static.book345.com/"
        const val DOWNLOAD_HOST_URL = "https://static2.book345.com"

        sealed class SortType(val type: String) {
            object LastUpdate : SortType("lastUpdate")
            object PubDate : SortType("pubDate")
            object Rate : SortType("rate")
        }

        sealed class HasFileSort(val type: Int) {
            object HasFile : HasFileSort(1) // filter books without downloadable file
            object All : HasFileSort(0) // show all books
        }

        sealed class SearchExactly(val type: Int) {
            object Exactly : HasFileSort(1) // only when book's name equals to the searched key
            object Approximate : HasFileSort(0)
        }
    }

    @GET("api/search")
    suspend fun searchBooksByTag(
        @Query("tag") tag: String,
        @Query("count") count: Int = 10,
        @Query("page") page: Int = 1, // counts from 1
        @Query("type") type: String = SortType.LastUpdate.type,
        @Query("hasfile") hasFile: Int = HasFileSort.HasFile.type
    ): ApiResponse<BookSearchResult>

    @GET("api/search")
    suspend fun searchBooksByKey(
        @Query("key") key: String,
        @Query("count") count: Int = 10,
        @Query("page") page: Int = 1, // counts from 1
        @Query("type") type: String = SortType.LastUpdate.type,
        @Query("hasfile") hasFile: Int = HasFileSort.HasFile.type,
        @Query("isExact") isExact: Int = SearchExactly.Approximate.type // "exact" means searching a book with 'key' as its name
    ): ApiResponse<BookSearchResult>

    @GET("api/search")
    suspend fun searchBooksByAuthor(
        @Query("author") author: String,
        @Query("count") count: Int = 10,
        @Query("page") page: Int = 1, // counts from 1
        @Query("type") type: String = SortType.LastUpdate.type,
        @Query("hasfile") hasFile: Int = HasFileSort.HasFile.type,
    ): ApiResponse<BookSearchResult>

    @GET("api/search")
    suspend fun searchBooksByPublisher(
        @Query("publisher") publisher: String,
        @Query("count") count: Int = 10,
        @Query("page") page: Int = 1, // counts from 1
        @Query("type") type: String = SortType.LastUpdate.type,
        @Query("hasfile") hasFile: Int = HasFileSort.HasFile.type,
    ): ApiResponse<BookSearchResult>

    @GET("api/getRelatedBook")
    suspend fun searchRelatedBooksByISBN(
        @Query("isbn") isbn: String
    ): ApiResponse<RelatedBooksResult>

    @GET("api/recentHot")
    suspend fun fetchRecentHotBooks(
        @Query("tag") tag: String = ""
    ): ApiResponse<List<BookPreview>>

    @GET("api/detail/{isbn}")
    suspend fun fetchBookDetail(
        @Path("isbn") isbn: String
    ): ApiResponse<BookDetail>

    @GET("api/simple_search")
    suspend fun simpleSearch(
        @Query("key") key: String,
        @Query("count") count: Int = 5,
        @Query("page") page: Int = 1,
    ): ApiResponse<SimpleSearchResult>

    @GET("$DOWNLOAD_HOST_URL/api/checkHasFile")
    suspend fun checkHasBookFile(
        @Query("fileName") fileName: String
    ): ApiResponse<CheckBookFileResult>

}