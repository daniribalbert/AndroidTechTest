package com.buffup.api.utils

data class BaseResult<out T>(val result: T?, val error: BuffError?)