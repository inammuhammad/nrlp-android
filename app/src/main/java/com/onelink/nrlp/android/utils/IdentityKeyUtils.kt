package com.onelink.nrlp.android.utils

import android.annotation.SuppressLint
import com.onelink.nrlp.android.utils.network.NetworkConnectivity
import java.text.SimpleDateFormat
import java.util.*

object IdentityKeyUtils {

    /**
     * Algorithm to create random key
     */

    fun generateKey(nic: CharArray): String = StringBuilder().append(getCharactersAtIndices(12, 11, charArray = nic))
        .append(getDateTimeChunks(Calendar.SECOND))
        .append(NetworkConnectivity.getLocalIpAddress().toIpOctets()[3].takeLast(2))
        .append(getCharactersAtIndices(10, 9, charArray = nic))
        .append(getDateTimeChunks(Calendar.MINUTE))
        .append(NetworkConnectivity.getLocalIpAddress().toIpOctets()[2].takeLast(2))
        .append(getCharactersAtIndices(8, 7, charArray = nic))
        .append(getDateTimeChunks(Calendar.HOUR))
        .append(NetworkConnectivity.getLocalIpAddress().toIpOctets()[1].takeLast(2))
        .append(getCharactersAtIndices(6, 5, charArray = nic))
        .append(getDateTimeChunks(Calendar.YEAR).takeLast(2))
        .append(NetworkConnectivity.getLocalIpAddress().toIpOctets()[0].takeLast(2))
        .append(getCharactersAtIndices(4, 3, charArray = nic))
        .append(getDateTimeChunks(Calendar.MONTH))
        .append(getCharactersAtIndices(2, 1, 0, charArray = nic))
        .append(getDateTimeChunks(0).take(1))
        .toString()



    @SuppressLint("SimpleDateFormat")
    private fun getDateTimeChunks(type: Int): String = when (type) {
        Calendar.SECOND -> SimpleDateFormat("ss").format(Date())
        Calendar.MINUTE -> SimpleDateFormat("mm").format(Date())
        Calendar.HOUR -> SimpleDateFormat("hh").format(Date())
        Calendar.YEAR -> SimpleDateFormat("yyyy").format(Date())
        Calendar.MONTH -> SimpleDateFormat("MM").format(Date())
        else -> {
            val year = Calendar.getInstance().get(Calendar.YEAR)
            if (year % 100 == 0) {
                (year / 100).toString()
            } else {
                ((year / 100) + 1).toString()
            }
        }
    }

    private fun String.toIpOctets(): List<String> {
        val ipOctetsArray = mutableListOf<String>()
        if (this.contains(".")) {
            this.split(".").toList().forEach { octet ->
                ipOctetsArray.add(String.format("%8s", (octet.toInt()).toString(2)).replace(" ", "0"))
            }
        }
        return ipOctetsArray;
    }

    private fun getCharactersAtIndices(vararg indices: Int, charArray: CharArray): String {
        val jointChars = StringBuilder()
        indices.forEach { index ->
            jointChars.append(charArray[index])
        }
        return jointChars.toString()
    }
}