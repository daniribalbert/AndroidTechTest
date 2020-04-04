package com.buffup.api.utils

sealed class BuffError {
    data class HttpError(val errorCode: Int, val errorMsg: String?): BuffError()
    data class UnauthorizedError(val errorCode: Int = 401, val errorMsg: String?): BuffError()
    data class TimeoutError(val errorMsg: String = "Timeout", val throwable: Throwable): BuffError()
    data class GenericError(val throwable: Throwable, val errorCode: Int?, val errorMsg: String?): BuffError()
}