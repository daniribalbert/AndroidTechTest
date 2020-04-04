package com.buffup.views

import com.buffup.api.BuffRepository
import com.buffup.api.model.BuffApiResult
import com.buffup.api.model.BuffResponse
import com.buffup.api.utils.BaseResult
import com.buffup.api.utils.BuffError
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BuffViewPresenterUnitTest {

    @Mock
    lateinit var mockRepository: BuffRepository

    @Mock
    lateinit var mockBuffApiResponse: BuffApiResult<BuffResponse>

    @Mock
    lateinit var mockBuffResponse: BuffResponse

    @Mock
    lateinit var mockException: BuffError

    private lateinit var presenter: BuffViewPresenter

    @Spy
    private val successListener = object : BuffViewPresenter.Listener {
        override fun onBuffLoaded(data: BaseResult<BuffResponse>) {
            Assert.assertNotNull(data)
            Assert.assertEquals(data.result?.id,
                BUFF_TEST_ID
            )
            Assert.assertNull(data.error)
        }
    }

    @Spy
    private val failListener = object : BuffViewPresenter.Listener {
        override fun onBuffLoaded(data: BaseResult<BuffResponse>) {
            Assert.assertNotNull(data)
            Assert.assertNull(data.result)
            Assert.assertEquals(data.error, mockException)
        }
    }

    @Before
    fun setupTests() {
        `when`(mockBuffApiResponse.result).thenReturn(mockBuffResponse)
        `when`(mockBuffResponse.id).thenReturn(BUFF_TEST_ID)

        presenter = BuffViewPresenter(mockRepository)
    }

    @Test
    fun testBuffLoad_Success() {
        TestCoroutineScope().launch {
            val result = BaseResult(mockBuffApiResponse, null)
            `when`(mockRepository.loadBuff(1)).thenReturn(result)

            presenter.listener = successListener
            presenter.loadBuff(1)

            val baseListenerResponse = BaseResult(mockBuffResponse,null)
            verify(successListener)!!.onBuffLoaded(baseListenerResponse)
        }
    }

    @Test
    fun testBuffLoad_Failed() {
        TestCoroutineScope().launch {
            `when`(mockRepository.loadBuff(-1)).thenReturn(BaseResult(null, mockException))

            presenter.listener = failListener
            presenter.loadBuff(-1)

            val baseListenerResponse = BaseResult<BuffResponse>(null, mockException)
            verify(failListener)!!.onBuffLoaded(baseListenerResponse)
        }
    }

    companion object {
        private const val BUFF_TEST_ID = 7L
    }


}