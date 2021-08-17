package com.onelink.nrlp.android.viewmodel

import com.onelink.nrlp.android.features.redeem.repo.RedemptionRepo
import com.onelink.nrlp.android.features.redeem.viewmodels.RedeemOtpFragmentViewModel
import com.onelink.nrlp.android.base.BaseViewModelTest
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock

class RedeemOTPViewModelTest : BaseViewModelTest() {

    companion object {
        private const val OTP1 = "1"

        private const val OTP2 = "2"

        private const val OTP3 = "3"

        private const val OTP4 = "4"

    }

    private lateinit var viewModel: RedeemOtpFragmentViewModel

    @Mock
    private lateinit var redemptionRepo: RedemptionRepo


    override fun setUp() {
        super.setUp()
        viewModel = RedeemOtpFragmentViewModel(redemptionRepo)
    }


    @Test
    fun getOTPSuccess() {

        assertNotNull(viewModel.getOTPCode())

        viewModel.etOTP1.value = OTP1
        viewModel.etOTP2.value = OTP2
        viewModel.etOTP3.value = OTP3
        viewModel.etOTP4.value = OTP4


        assertEquals(4, viewModel.getOTPCode().length)

        assertEquals(
            OTP1 + OTP2 + OTP3 + OTP4,
            viewModel.getOTPCode()
        )

    }


    @Test
    fun otp1Test() {
        assertTrue(
            viewModel.etOTP1.value.isNullOrEmpty()
        )
        viewModel.etOTP1.postValue(OTP1)

        assertNotNull(
            viewModel.etOTP1.value
        )

        assertEquals(
            OTP1,
            viewModel.etOTP1.value
        )
    }

    @Test
    fun otp2Test() {
        assertTrue(
            viewModel.etOTP2.value.isNullOrEmpty()
        )
        viewModel.etOTP2.postValue(OTP2)

        assertNotNull(
            viewModel.etOTP2.value
        )

        assertEquals(
            OTP2,
            viewModel.etOTP2.value
        )
    }

    @Test
    fun otp3Test() {
        assertTrue(
            viewModel.etOTP3.value.isNullOrEmpty()
        )
        viewModel.etOTP3.postValue(OTP3)

        assertNotNull(
            viewModel.etOTP3.value
        )

        assertEquals(
            OTP3,
            viewModel.etOTP3.value
        )
    }

    @Test
    fun otp4Test() {
        assertTrue(
            viewModel.etOTP4.value.isNullOrEmpty()
        )
        viewModel.etOTP4.postValue(OTP4)

        assertNotNull(
            viewModel.etOTP4.value
        )

        assertEquals(
            OTP4,
            viewModel.etOTP4.value
        )
    }

}