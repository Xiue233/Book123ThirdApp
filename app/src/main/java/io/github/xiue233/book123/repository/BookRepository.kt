package io.github.xiue233.book123.repository

import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import io.github.xiue233.book123.model.BookDetail
import io.github.xiue233.book123.model.BookPreview
import io.github.xiue233.book123.model.CheckBookFileResult
import io.github.xiue233.book123.model.SimpleSearchResult
import io.github.xiue233.book123.network.Book123Service
import io.github.xiue233.book123.network.EmptyRequestHandler
import io.github.xiue233.book123.network.RequestHandler
import io.github.xiue233.book123.util.BookSummaries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


interface BookRepository {
    fun simpleSearchByKey(
        key: String,
        count: Int = 5,
        page: Int = 1,
        requestHandler: RequestHandler = EmptyRequestHandler
    ): Flow<List<BookPreview>>

    fun searchBookByTag(
        tag: String,
        count: Int = 10,
        page: Int = 1, // counts from 1
        type: Book123Service.Companion.SortType = Book123Service.Companion.SortType.LastUpdate,
        hasFile: Boolean = true,
        requestHandler: RequestHandler = EmptyRequestHandler
    ): Flow<BookSummaries>

    fun searchBooksByKey(
        key: String,
        count: Int = 10,
        page: Int = 1, // counts from 1
        type: Book123Service.Companion.SortType = Book123Service.Companion.SortType.LastUpdate,
        hasFile: Boolean = true,
        isExact: Boolean = false, // "exact" means searching a book with 'key' as its name
        requestHandler: RequestHandler = EmptyRequestHandler
    ): Flow<BookSummaries>

    fun searchBooksByAuthor(
        author: String,
        count: Int = 10,
        page: Int = 1, // counts from 1
        type: Book123Service.Companion.SortType = Book123Service.Companion.SortType.LastUpdate,
        hasFile: Boolean = true,
        requestHandler: RequestHandler = EmptyRequestHandler
    ): Flow<BookSummaries>

    fun searchBooksByPublisher(
        publisher: String,
        count: Int = 10,
        page: Int = 1, // counts from 1
        type: Book123Service.Companion.SortType = Book123Service.Companion.SortType.LastUpdate,
        hasFile: Boolean = true,
        requestHandler: RequestHandler = EmptyRequestHandler
    ): Flow<BookSummaries>

    fun searchRelatedBooksByISBN(
        isbn: String,
        requestHandler: RequestHandler = EmptyRequestHandler
    ): Flow<BookSummaries>

    fun fetchRecentHotBooks(
        tag: String = "undefined",
        requestHandler: RequestHandler = EmptyRequestHandler
    ): Flow<List<BookPreview>>

    fun fetchBookDetail(
        isbn: String,
        requestHandler: RequestHandler = EmptyRequestHandler
    ): Flow<BookDetail>

    fun checkHasBookFile(
        fileName: String,
        requestHandler: RequestHandler = EmptyRequestHandler
    ): Flow<CheckBookFileResult>

}

fun <T> BookRepositoryImpl.requestWithHandler(
    requestHandler: RequestHandler,
    coroutineContext: CoroutineContext = Dispatchers.IO,
    block: BookRepositoryImpl.() -> Flow<T>
): Flow<T> = this.block().onStart {
    requestHandler.onStart()
}.onCompletion {
    requestHandler.onCompletion()
}.flowOn(Dispatchers.IO)

class BookRepositoryImpl @Inject constructor(
    private val book123Service: Book123Service
) : BookRepository {
    override fun simpleSearchByKey(
        key: String,
        count: Int,
        page: Int,
        requestHandler: RequestHandler
    ): Flow<List<BookPreview>> = requestWithHandler(requestHandler) {
        flow {
            book123Service.simpleSearch(
                key, count, page
            ).onFailure {
                requestHandler.onFailure(message())
            }.suspendOnSuccess {
                emit(data.books)
            }
        }
    }

    override fun searchBookByTag(
        tag: String,
        count: Int,
        page: Int,
        type: Book123Service.Companion.SortType,
        hasFile: Boolean,
        requestHandler: RequestHandler
    ): Flow<BookSummaries> = requestWithHandler(requestHandler) {
        flow<BookSummaries> {
            book123Service.searchBooksByTag(
                tag, count, page,
                type.type,
                if (hasFile) Book123Service.Companion.HasFileSort.HasFile.type
                else Book123Service.Companion.HasFileSort.All.type
            ).onFailure {
                requestHandler.onFailure(message())
            }.suspendOnSuccess {
                emit(data.books)
            }
        }
    }

    override fun searchBooksByKey(
        key: String,
        count: Int,
        page: Int,
        type: Book123Service.Companion.SortType,
        hasFile: Boolean,
        isExact: Boolean,
        requestHandler: RequestHandler
    ): Flow<BookSummaries> = requestWithHandler(requestHandler) {
        flow<BookSummaries> {
            book123Service.searchBooksByKey(
                key, count, page,
                type.type,
                if (hasFile) Book123Service.Companion.HasFileSort.HasFile.type
                else Book123Service.Companion.HasFileSort.All.type,
                if (isExact) Book123Service.Companion.SearchExactly.Exactly.type
                else Book123Service.Companion.SearchExactly.Approximate.type
            ).onFailure {
                requestHandler.onFailure(message())
            }.suspendOnSuccess {
                emit(data.books)
            }
        }
    }

    override fun searchBooksByAuthor(
        author: String,
        count: Int,
        page: Int,
        type: Book123Service.Companion.SortType,
        hasFile: Boolean,
        requestHandler: RequestHandler
    ): Flow<BookSummaries> = requestWithHandler(requestHandler) {
        flow<BookSummaries> {
            book123Service.searchBooksByAuthor(
                author, count, page,
                type.type,
                if (hasFile) Book123Service.Companion.HasFileSort.HasFile.type
                else Book123Service.Companion.HasFileSort.All.type
            ).onFailure {
                requestHandler.onFailure(message())
            }.suspendOnSuccess {
                emit(data.books)
            }
        }
    }

    override fun searchBooksByPublisher(
        publisher: String,
        count: Int,
        page: Int,
        type: Book123Service.Companion.SortType,
        hasFile: Boolean,
        requestHandler: RequestHandler
    ): Flow<BookSummaries> = requestWithHandler(requestHandler) {
        flow<BookSummaries> {
            book123Service.searchBooksByPublisher(
                publisher, count, page,
                type.type,
                if (hasFile) Book123Service.Companion.HasFileSort.HasFile.type
                else Book123Service.Companion.HasFileSort.All.type
            ).onFailure {
                requestHandler.onFailure(message())
            }.suspendOnSuccess {
                emit(data.books)
            }
        }
    }

    override fun searchRelatedBooksByISBN(
        isbn: String,
        requestHandler: RequestHandler
    ): Flow<BookSummaries> = requestWithHandler(requestHandler) {
        flow<BookSummaries> {
            book123Service.searchRelatedBooksByISBN(isbn)
                .onFailure {
                    requestHandler.onFailure(message())
                }.suspendOnSuccess {
                    emit(data.books)
                }
        }
    }

    override fun fetchRecentHotBooks(
        tag: String,
        requestHandler: RequestHandler
    ): Flow<List<BookPreview>> = requestWithHandler(requestHandler) {
        flow {
            book123Service.fetchRecentHotBooks(tag)
                .onFailure {
                    requestHandler.onFailure(message())
                }
                .suspendOnSuccess {
                    emit(data)
                }
        }
    }

    override fun fetchBookDetail(
        isbn: String,
        requestHandler: RequestHandler
    ): Flow<BookDetail> = requestWithHandler(requestHandler) {
        flow {
            book123Service.fetchBookDetail(isbn)
                .onFailure {
                    requestHandler.onFailure(message())
                }
                .suspendOnSuccess {
                    emit(data)
                }
        }
    }

    override fun checkHasBookFile(
        fileName: String,
        requestHandler: RequestHandler
    ): Flow<CheckBookFileResult> = requestWithHandler(requestHandler) {
        flow {
            book123Service.checkHasBookFile(fileName)
                .onFailure {
                    requestHandler.onFailure(message())
                }
                .suspendOnSuccess {
                    emit(data)
                }
        }
    }
}