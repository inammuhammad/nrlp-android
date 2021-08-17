package com.onelink.nrlp.android.utils

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class UniqueDeviceIdTest {

    private val testByteArray = "sdf908asd98asd0fasdf80as".toByteArray(Charsets.UTF_8)
    private val testResult = "736466393038617364393861736430666173646638306173"

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun toHexStringTest() {
        val result = UniqueDeviceID.toHexString(
            testByteArray
        )
        assertNotNull(
            result
        )

        assertEquals(
            testResult,
            result
        )
    }

    @Test
    fun getUniqueIdTest() {
        assertNull(
            UniqueDeviceID.getUniqueId()
        )
    }
}