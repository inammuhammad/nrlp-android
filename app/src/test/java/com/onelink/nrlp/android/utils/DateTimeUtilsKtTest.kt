package com.onelink.nrlp.android.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class DateTimeUtilsKtTest {

    @Test
    fun getCurrentDateSuccess() {
        val date = getCurrentDate()
        assertEquals(formatDate(Calendar.getInstance()), date)
    }
}