package io.github.xiue233.book123.network

import com.skydoves.sandwich.ApiResponse
import io.github.xiue233.book123.model.BookDetail
import io.github.xiue233.book123.model.BookPreview
import io.github.xiue233.book123.model.BookSearchResult
import io.github.xiue233.book123.model.BookSummary
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Book123Service {
    companion object {
        const val BASE_URL = "https://www.book123.info/"
        const val IMG_HOST_URL = "https://file3.book123.info/"
        const val DOWNLOAD_HOST_URL = "https://static.file123.info:8443/"

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
    ): ApiResponse<BookSearchResult>

    @GET("api/recentHot")
    suspend fun fetchRecentHotBooks(
        @Query("tag") tag: String = ""
    ): ApiResponse<List<BookPreview>>

    @GET("api/detail/{isbn}")
    suspend fun fetchBookDetail(
        @Path("isbn") isbn: String
    ): ApiResponse<BookDetail>

}