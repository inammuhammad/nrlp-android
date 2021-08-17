package com.onelink.nrlp.android.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
abstract class BaseViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()


    @Before
    open fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @After
    open fun tearDown() {

    }

}