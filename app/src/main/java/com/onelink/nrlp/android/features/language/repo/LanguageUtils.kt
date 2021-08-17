package com.onelink.nrlp.android.features.language.repo

import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.features.language.model.LanguageTypeModel

class LanguageUtils {
    companion object {
        fun languages() = mutableListOf<LanguageTypeModel>().apply {
            add(LanguageTypeModel(R.string.english, "1"))
            add(LanguageTypeModel(R.string.urdu, "2"))
        }
    }
}