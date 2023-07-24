package io.github.xiue233.book123

import com.google.gson.Gson
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import io.github.xiue233.book123.di.NetworkModule
import io.github.xiue233.book123.model.BookPreview
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        runBlocking {
            NetworkModule.provideBook123Service(
                NetworkModule.provideRetrofit(
                    NetworkModule.provideOkHttpClient()
                )
            ).searchRelatedBooksByISBN("9787521747423")
                .suspendOnFailure {
                    println(message())
                }
                .suspendOnSuccess {
                    data.books.forEach { book ->
                        println(book.title)
                    }
                }
        }
    }
}