package com.onelink.nrlp.android.utils

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class AppUtilsTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var context: Context

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        context = Mockito.mock(Context::class.java)
    }

    @Test
    fun isValidInstallationSuccess() {
        assertTrue(AppUtils.isFromPlayStore())
    }

    @Test
    fun isEmulatorSuccess() {
        assertTrue(AppUtils.isValidInstallation(context))
    }
}