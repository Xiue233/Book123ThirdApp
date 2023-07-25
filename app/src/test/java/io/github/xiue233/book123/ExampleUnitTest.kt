package io.github.xiue233.book123

import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import io.github.xiue233.book123.di.NetworkModule
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
            ).checkHasBookFile("9787521747423.epub")
                .suspendOnFailure {
                    println(message())
                }
                .suspendOnSuccess {
                    println(data.isExist)
                }
        }
    }
}