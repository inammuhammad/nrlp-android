package com.onelink.nrlp.android.utils

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class LocaleManagerTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context

    }

    @Test
    fun urduConstTest() = assertNotNull(LocaleManager.URDU)


    @Test
    fun englishConstTest() = assertNotNull(LocaleManager.ENGLISH)

    // here is the test and Crash
    @Test
    fun updatBaseContexTest() = assertFalse(context === LocaleManager.updateBaseContext(context))


}