package com.dalmuri.dalmuri.di

import com.dalmuri.dalmuri.BuildConfig
import com.dalmuri.dalmuri.data.remote.api.OpenAiApi
import com.dalmuri.dalmuri.data.remote.datasource.TilRemoteDataSource
import com.dalmuri.dalmuri.data.remote.datasource.TilRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://api.openai.com/"

    @Provides
    @Singleton
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            encodeDefaults = true
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val apiKey = BuildConfig.OPENAI_API_KEY

        val authInterceptor =
            Interceptor { chain ->
                val request =
                    chain
                        .request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer $apiKey")
                        .build()
                chain.proceed(request)
            }

        return OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenAiApi(retrofit: Retrofit): OpenAiApi = retrofit.create(OpenAiApi::class.java)

    @Provides
    @Singleton
    fun provideTilRemoteDataSource(api: OpenAiApi): TilRemoteDataSource = TilRemoteDataSourceImpl(api)
}
