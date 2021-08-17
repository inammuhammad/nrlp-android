package com.onelink.nrlp.android.repos.viewstatement

import com.onelink.nrlp.android.base.BaseRepoTest
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.features.viewStatement.models.StatementsResponseModel
import com.onelink.nrlp.android.features.viewStatement.repo.ViewStatementRepo
import com.onelink.nrlp.android.getOrAwaitValue
import com.onelink.nrlp.android.models.GeneralMessageResponseModel
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import java.util.concurrent.TimeUnit

class ViewStatementRepoTest : BaseRepoTest() {

    @Mock
    lateinit var statementsResponse: BaseResponse<StatementsResponseModel>

    @Mock
    lateinit var detailedStatementResponse: BaseResponse<GeneralMessageResponseModel>

    private lateinit var viewStatementRepo: ViewStatementRepo

    private lateinit var viewStatementService: ViewStatementService

    override fun setUp() {
        super.setUp()
        viewStatementService = ViewStatementService()
        viewStatementRepo = ViewStatementRepo(networkHelper, viewStatementService.serviceGateway)
    }

    @Test
    fun getStatementsTest() {
        val request = getLoyaltyStatementRequest()
        val response = getStatementsResponse()
        viewStatementService.mockGetStatements(request, response)
        viewStatementRepo.getStatements(request)
        viewStatementRepo.observeStatements().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(it.data, response)
        }
    }

    @Test
    fun getDetailedStatementsTest() {
        val request = getDetailedStatementRequest()
        val response = getDetailsStatementResponse()
        viewStatementService.mockGetDetailedStatement(request, response)
        viewStatementRepo.getDetailedStatements(request)
        viewStatementRepo.observeDetailedStatement().getOrAwaitValue(10, TimeUnit.SECONDS).let {
            assertEquals(it.data, response)
        }
    }

    @Test
    fun statementsResponseEmitSuccess() {

        assertNotNull(viewStatementRepo.statementsResponse)

        assertNull(viewStatementRepo.statementsResponse.value)

        viewStatementRepo.statementsResponse.value = statementsResponse

        assertEquals(viewStatementRepo.statementsResponse.value, statementsResponse)

        assertNotNull(
            viewStatementRepo.statementsResponse.value
        )

    }

    @Test
    fun statementsResponseEmitFail() {

        assertFalse(viewStatementRepo.statementsResponse.value != null)

        assertNotEquals(viewStatementRepo.statementsResponse.value, statementsResponse)

    }

    @Test
    fun detailedStatementResponseEmitSuccess() {

        assertNotNull(viewStatementRepo.detailedStatementResponse)

        assertNull(viewStatementRepo.detailedStatementResponse.value)

        viewStatementRepo.detailedStatementResponse.value = detailedStatementResponse

        assertEquals(viewStatementRepo.detailedStatementResponse.value, detailedStatementResponse)

        assertNotNull(
            viewStatementRepo.detailedStatementResponse.value
        )

    }

    @Test
    fun detailedStatementResponseEmitFail() {

        assertFalse(viewStatementRepo.detailedStatementResponse.value != null)

        assertNotEquals(
            viewStatementRepo.detailedStatementResponse.value,
            detailedStatementResponse
        )

    }
}