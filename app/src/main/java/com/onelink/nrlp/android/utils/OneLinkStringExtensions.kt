package com.onelink.nrlp.android.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.Base64
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.lang.IndexOutOfBoundsException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Umar Javed.
 */

const val ZERO_PAD = "0000000000000"
fun String.formattedCnicNumber(): String {
    val paddedStr = ZERO_PAD.substring(this.length) + this
    val sb = StringBuilder(paddedStr).insert(5, " - ").insert(15, " - ")
    return sb.toString()
}

fun String.formattedCnicNumberNoSpaces(): String {
    val paddedStr = ZERO_PAD.substring(this.length) + this
    val sb = StringBuilder(paddedStr).insert(5, "-").insert(13, "-")
    return sb.toString()
}

fun String.cleanNicNumber() = this.replace("-", "")

fun String.toSpanned(): Spanned {
    return SpannableStringBuilder(this)
}

fun String.getCountryCode(): String {
    val phoneNumberUtils = PhoneNumberUtil.getInstance()
    val phoneNumber = phoneNumberUtils.parse(this, "")
    return phoneNumber.countryCode.toString()
}

@Suppress("DEPRECATION")
fun String.parseHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

fun String.allCharactersSame(): Boolean {
    val n = this.length
    for (i in 1 until n) if (this[i] != this[0]) return false
    return true
}

fun String.removeDashes(): String {
    return this.replace("-", "")
}

fun String.removePlusCharacter(): String {
    return this.replace("+", "")
}

fun String.dc() = String(Base64.decode(this, Base64.DEFAULT))

fun String.toFormattedDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    val dateString: Date = dateFormat.parse(this) ?: Date()
    val cal = Calendar.getInstance()
    cal.time = dateString
    val day = cal[Calendar.DATE]
    return if (day !in 11..18) when (day % 10) {
        1 -> SimpleDateFormat("d'st' MMMM yyyy", Locale.US).format(dateString)
        2 -> SimpleDateFormat("d'nd' MMMM yyyy", Locale.US).format(dateString)
        3 -> SimpleDateFormat("d'rd' MMMM yyyy", Locale.US).format(dateString)
        else -> SimpleDateFormat("d'th' MMMM yyyy", Locale.US).format(dateString)
    } else SimpleDateFormat("d'th' MMMM yyyy", Locale.US).format(dateString)
}

fun String.base64ToBitmap(): Bitmap? {
    return try {
        val base64Image: String = this.split(",")[1]
        val decodedString: ByteArray = Base64.decode(base64Image, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    } catch (e: IndexOutOfBoundsException) {
        null
    }
}

fun String.getParse() : String {
   return this.substring(0,2) + "/" + this.substring(2,4) + "/20" + this.substring(4,this.length)
}

@Suppress("DEPRECATION")
fun SpannableStringBuilder.toHtmlString(): String =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.toHtml(this, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL)
    } else Html.toHtml(this)