package com.buffup.views

import com.buffup.api.BuffRepository
import com.buffup.api.model.BuffResponse
import com.buffup.api.utils.BaseResult
import kotlinx.coroutines.*
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * Buff Presenter is responsible for loading Buffs from BuffRepository and sending the result back
 * to its Listener.
 */
internal class BuffViewPresenter(private val repository: BuffRepository, var listener: Listener? = null) :
    CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun loadBuff(buffId: Long) {
        launch(Dispatchers.IO) {
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

    /**
     * Cancel coroutines.
     */
    fun stop() {
        job.cancel()
    }

    interface Listener {
        fun onBuffLoaded(data: BaseResult<BuffResponse>)
    }
}