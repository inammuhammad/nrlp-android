package com.onelink.nrlp.android.viewmodel

import com.onelink.nrlp.android.features.uuid.viewmodel.UUIDSharedViewModel
import com.onelink.nrlp.android.models.LoginCredentials
import com.onelink.nrlp.android.base.BaseViewModelTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.mockito.Mock

class UUIDSharedViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: UUIDSharedViewModel

    @Mock
    private lateinit var loginCredentials: LoginCredentials

    override fun setUp() {
        super.setUp()
        viewModel = UUIDSharedViewModel()
    }

    @Test
    fun setLoginCredentialsModelSuccess() {

        assertEquals(
            null,
            viewModel.loginCredentials.value
        )

        viewModel.setLoginCredentialsModel(loginCredentials)

        assertNotNull(
            viewModel.loginCredentials
        )

        assertEquals(
            loginCredentials,
            viewModel.loginCredentials.value
        )

    }
}