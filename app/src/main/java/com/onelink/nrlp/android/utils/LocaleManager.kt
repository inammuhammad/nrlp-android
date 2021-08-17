package com.onelink.nrlp.android.utils

import android.content.Context
import android.os.Build
import androidx.annotation.StringDef
import androidx.preference.PreferenceManager
import java.util.*

object LocaleManager {
    const val ENGLISH = "en"
    const val URDU = "ur"
    //const val None = "none"

    /**
     * SharedPreferences Key
     */
    private const val LANGUAGE_KEY = "language_key"
    private const val LANGUAGE_SET_KEY = "language__set_key"

    /**
     * set current pref locale
     */
    fun updateBaseContext(mContext: Context): Context {
        return updateResources(mContext, getLanguagePref(mContext))
    }

    /**
     * Set new Locale with context
     */
    fun setNewLocale(mContext: Context, @LocaleDef language: String) {
        setLanguagePref(mContext, language)
        updateResources(mContext, language)
    }

    /**
     * Get saved Locale from SharedPreferences
     *
     * @param mContext current context
     * @return current locale key by default return english locale
     */
    fun getLanguagePref(mContext: Context?): String? {
        val languagePm: String = Locale.getDefault().language

        val mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        return mPreferences.getString(LANGUAGE_KEY, languagePm)
    }

    fun getLanguageBoolPref(mContext: Context?): Boolean? {
        val mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        return mPreferences.getBoolean(LANGUAGE_SET_KEY, false)
    }

    /**
     * set pref key
     */
    private fun setLanguagePref(mContext: Context, localeKey: String) {
        val mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        mPreferences.edit().putString(LANGUAGE_KEY, localeKey).apply()
    }

    fun setLanguageSetBoolPref(mContext: Context, bool: Boolean) {
        val mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        mPreferences.edit().putBoolean(LANGUAGE_SET_KEY, bool).apply()
    }

    /**
     * update resource
     */
    private fun updateResources(context: Context, language: String?): Context {
        language?.let {
            val locale = Locale(it, getCountryCode(it))
            Locale.setDefault(locale)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                updateResourcesLocale(context, locale)
            } else {
                updateResourcesLocaleLegacy(context, locale)
            }
        } ?: return context
    }

    private fun updateResourcesLocale(context: Context, locale: Locale): Context {
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

    private fun getCountryCode(lang: String?) = if (lang == ENGLISH) "US" else "PK"

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @StringDef(ENGLISH, URDU)
    annotation class LocaleDef {
        companion object {
            //var SUPPORTED_LOCALES = arrayOf(ENGLISH, URDU)
        }
    }
}
