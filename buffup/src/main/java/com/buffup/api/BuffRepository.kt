package com.buffup.api

import com.buffup.api.utils.safeCall
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BuffRepository {

    companion object {
        private const val BASE_URL = "https://buffup.proxy.beeceptor.com"
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()
                )
            )
            .client(okHttpClient)
            .build()
    }

    private val buffApiService: BuffApi by lazy { retrofit.create(BuffApi::class.java) }

    suspend fun loadBuff(buffId: Long) =
        safeCall(Dispatchers.IO) { buffApiService.loadBuff(buffId) }
}