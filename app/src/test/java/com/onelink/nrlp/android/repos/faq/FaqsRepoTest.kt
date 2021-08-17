package com.onelink.nrlp.android.repos.faq

import com.onelink.nrlp.android.base.BaseRepoTest
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.features.faqs.model.FaqsResponseModel
import com.onelink.nrlp.android.features.faqs.repo.FaqsRepo
import com.onelink.nrlp.android.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import java.util.concurrent.TimeUnit

class FaqsRepoTest : BaseRepoTest() {

    @Mock
    lateinit var faqsResponse: BaseResponse<FaqsResponseModel>

    private lateinit var faqsRepo: FaqsRepo

    private lateinit var faqService: FaqService

    override fun setUp() {
        super.setUp()
        faqService = FaqService()
        faqsRepo = FaqsRepo(networkHelper, faqService.serviceGateway)
    }

    @Test
    fun getFaqsQuestionTest() {
        val response = getFaqResponseModel()
        faqService.mockGetFaqs(response)
        faqsRepo.getFaqsQuestion()
        faqsRepo.observeFAQs().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(it.data, response)
        }
    }

    @Test
    fun onClearSuccess() {

        assertNull(networkHelper.disposable)

        networkHelper.disposable = disposable

        assertNotNull(networkHelper.disposable)

        assertEquals(disposable, networkHelper.disposable)

        assertTrue(disposable === networkHelper.disposable)

        faqsRepo.onClear()

        assertNull(networkHelper.disposable)

    }


    @Test
    fun onClearFailed() {
        assertFalse(
            networkHelper.disposable != null
        )

        assertFalse(networkHelper.disposable != null)

        networkHelper.disposable = disposable

        faqsRepo.onClear()

        assertFalse(networkHelper.disposable != null)

    }

    @Test
    fun faqsResponseEmitSuccess() {

        assertNotNull(faqsRepo.faqsResponse)

        assertNull(faqsRepo.faqsResponse.value)

        faqsRepo.faqsResponse.value = faqsResponse

        assertEquals(faqsRepo.faqsResponse.value, faqsResponse)

        assertNotNull(
            faqsRepo.faqsResponse.value
        )

    }

    @Test
    fun faqsResponseEmitFail() {

        assertFalse(faqsRepo.faqsResponse.value != null)

        assertNotEquals(faqsRepo.faqsResponse.value, faqsResponse)

    }

}