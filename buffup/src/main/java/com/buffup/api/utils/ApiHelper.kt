package com.buffup.api.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

suspend fun <T> safeCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): BaseResult<T> {
    return withContext(dispatcher) {
        try {
            BaseResult(apiCall.invoke(), null)
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> BaseResult(null, parseHttpError(throwable))
                is SocketTimeoutException -> BaseResult(
                    null,
                    BuffError.TimeoutError(throwable = throwable)
                )
                else -> BaseResult(null, BuffError.GenericError(throwable, null, null))
            }
        }
    }
}

fun parseHttpError(throwable: HttpException): BuffError = when (throwable.code()) {
    HttpURLConnection.HTTP_UNAUTHORIZED -> BuffError.UnauthorizedError(
        HttpURLConnection.HTTP_UNAUTHORIZED,
        throwable.message()
    )
    HttpURLConnection.HTTP_CLIENT_TIMEOUT,
    HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> BuffError.TimeoutError(throwable = throwable)
    else -> BuffError.HttpError(throwable.code(), throwable.message())
}

