package com.onelink.nrlp.android.utils

import com.onelink.nrlp.android.features.contactus.models.ContactDetailModel
import com.onelink.nrlp.android.features.contactus.view.ContactUsUtils
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ContactUsUtilsTest {

    @Test
    fun contactUsFailedCase() =
        assertNotEquals(listOf<ContactDetailModel>(), ContactUsUtils.contactUsList())

    @Test
    fun contactUsSuccessCase() = assertEquals(true, ContactUsUtils.contactUsList().isNotEmpty())


}