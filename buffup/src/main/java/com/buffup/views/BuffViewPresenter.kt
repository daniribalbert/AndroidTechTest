package com.buffup.views

import com.buffup.api.BuffRepository
import com.buffup.api.model.BuffResponse
import com.buffup.api.utils.BaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Buff Presenter is responsible for loading Buffs from BuffRepository and sending the result back
 * to its Listener.
 */
internal class BuffViewPresenter(private val repository: BuffRepository, var listener: Listener? = null) {

    fun loadBuff(buffId: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            val buff = repository.loadBuff(buffId)
            buff.result?.let {
                listener?.onBuffLoaded(BaseResult(it.result, null))
            }
            buff.error?.let {
                Timber.e("Something went wrong while loading Buff ${buff.error}")
                listener?.onBuffLoaded(BaseResult(null, it))
            }
        }
    }

    interface Listener {
        fun onBuffLoaded(data: BaseResult<BuffResponse>)
    }
}