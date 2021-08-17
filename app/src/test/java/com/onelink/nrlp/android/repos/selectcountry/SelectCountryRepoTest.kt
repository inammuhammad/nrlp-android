package com.onelink.nrlp.android.repos.selectcountry

import com.onelink.nrlp.android.base.BaseRepoTest
import com.onelink.nrlp.android.core.BaseResponse
import com.onelink.nrlp.android.features.select.country.model.CountryCodeResponseModel
import com.onelink.nrlp.android.features.select.country.repo.SelectCountryRepo
import com.onelink.nrlp.android.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import java.util.concurrent.TimeUnit

class SelectCountryRepoTest : BaseRepoTest() {

    @Mock
    lateinit var countryCodesResponse: BaseResponse<CountryCodeResponseModel>

    private lateinit var selectCountryRepo: SelectCountryRepo
    private lateinit var selectCountryCodeService: SelectCountryCodeService

    override fun setUp() {
        super.setUp()
        selectCountryCodeService = SelectCountryCodeService()
        selectCountryRepo =
            SelectCountryRepo(networkHelper, selectCountryCodeService.serviceGateway)
    }

    @Test
    fun getCountryCodesTest() {
        val response = getCountryCodeResponse()
        selectCountryCodeService.mockGetCountryCodes(response)
        selectCountryRepo.getCountryCodes()
        selectCountryRepo.observeCountryCodes().getOrAwaitValue(10, TimeUnit.SECONDS).let {
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

        selectCountryRepo.onClear()

        assertNull(networkHelper.disposable)

    }


    @Test
    fun onClearFailed() {
        assertFalse(
            networkHelper.disposable != null
        )

        assertFalse(networkHelper.disposable != null)

        networkHelper.disposable = disposable

        selectCountryRepo.onClear()

        assertFalse(networkHelper.disposable != null)

    }

    @Test
    fun countryCodesResponseEmitSuccess() {

        assertNotNull(selectCountryRepo.countryCodesResponse)

        assertNull(selectCountryRepo.countryCodesResponse.value)

        selectCountryRepo.countryCodesResponse.value = countryCodesResponse

        assertEquals(selectCountryRepo.countryCodesResponse.value, countryCodesResponse)

        assertNotNull(
            selectCountryRepo.countryCodesResponse.value
        )

    }

    @Test
    fun countryCodesResponseEmitFail() {

        assertFalse(selectCountryRepo.countryCodesResponse.value != null)

        assertNotEquals(selectCountryRepo.countryCodesResponse.value, countryCodesResponse)

    }

}