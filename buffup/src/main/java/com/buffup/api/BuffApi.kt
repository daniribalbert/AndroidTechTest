package com.buffup.api

import com.buffup.api.model.BuffApiResult
import com.buffup.api.model.BuffResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BuffApi {

    @GET("/buffs/{buffId}")
    suspend fun loadBuff(@Path("buffId") id: Long): BuffApiResult<BuffResponse>
}