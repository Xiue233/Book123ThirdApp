package io.github.xiue233.book123.di

import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.xiue233.book123.network.Book123Service
import io.github.xiue233.book123.network.RequestInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

private val trustAllCerts = arrayOf<TrustManager>(
    object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            // Do nothing, accept all clients
        }

        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            // Do nothing, accept all servers
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    }
)

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(RequestInterceptor())
        .sslSocketFactory(run {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            sslContext.socketFactory
        }, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }
        .readTimeout(30,TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(Book123Service.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideBook123Service(
        retrofit: Retrofit
    ): Book123Service = retrofit.create(Book123Service::class.java)
}