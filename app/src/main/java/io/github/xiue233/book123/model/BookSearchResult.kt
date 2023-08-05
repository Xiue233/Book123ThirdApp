package io.github.xiue233.book123.model

data class BookSearchResult(
    val page: Int = 1, // counts from 1
    val count: Int = 10,
    val books: List<BookSummary>
)

data class RelatedBooksResult(
    val books: List<BookPreview>
)
