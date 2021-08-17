package com.onelink.nrlp.android.features.language.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.databinding.FragmentLanguageBinding
import com.onelink.nrlp.android.features.home.view.HomeActivity
import com.onelink.nrlp.android.features.language.adapter.LanguageAdapter
import com.onelink.nrlp.android.features.language.model.LanguageTypeModel
import com.onelink.nrlp.android.features.language.repo.LanguageUtils
import com.onelink.nrlp.android.features.language.viewmodel.LanguageFragmentViewModel
import com.onelink.nrlp.android.features.login.view.LoginActivity
import com.onelink.nrlp.android.features.redeem.fragments.TAG_TIME_EXPIRED_DIALOG
import com.onelink.nrlp.android.features.redeem.fragments.TIME_EXPIRED_DIALOG
import com.onelink.nrlp.android.utils.LocaleManager
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.toSpanned
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class LanguageFragment(private var isFromSplash: Boolean = false) :
    BaseFragment<LanguageFragmentViewModel, FragmentLanguageBinding>(
        LanguageFragmentViewModel::class.java
    ), OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners,
    LanguageAdapter.OnItemClickListener {
    private lateinit var selectedLang: String
    override fun getLayoutRes() = R.layout.fragment_language

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var local = -1

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getTitle(): String = resources.getString(R.string.language_selection)
    override fun getViewM(): LanguageFragmentViewModel =
        ViewModelProvider(this, viewModelFactory).get(LanguageFragmentViewModel::class.java)


    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        val languageName: List<LanguageTypeModel> = LanguageUtils.languages()
        binding.languageList.setHasFixedSize(true)
        if (LocaleManager.getLanguagePref(context) == LocaleManager.ENGLISH) {
            local = 0
            selectedLang = LocaleManager.ENGLISH
        } else {
            local = 1
            selectedLang = LocaleManager.URDU
        }
        binding.languageList.adapter = LanguageAdapter(languageName, this, local)
        binding.btnsaveLanguge.isEnabled = false
        binding.btnsaveLanguge.setOnClickListener { _ ->
            if (isFromSplash) {
                context?.let {
                    LocaleManager.setNewLocale(it, selectedLang)
                    LocaleManager.setLanguageSetBoolPref(it, true)
                }
                activity?.let { LoginActivity.start(it) }
            } else {
                showLanguageChangeDialog()
            }
        }
        if (isFromSplash) binding.btnsaveLanguge.isEnabled = true
    }

    companion object {
        @JvmStatic
        fun newInstance(isFromSplash: Boolean = false) = LanguageFragment(isFromSplash)
    }

    private fun showLanguageChangeDialog() {
        val oneLinkAlertDialogsFragment = OneLinkAlertDialogsFragment.newInstance(
            false,
            R.drawable.ic_language_change_alert,
            getString(R.string.language_chagne_title),
            (getString(R.string.language_change)).toSpanned(),
            positiveButtonText = getString(R.string.confirm),
            negativeButtonText = getString(R.string.cancel),
            neutralButtonText = getString(R.string.okay)
        )
        oneLinkAlertDialogsFragment.setTargetFragment(
            this, TIME_EXPIRED_DIALOG
        )
        oneLinkAlertDialogsFragment.show(parentFragmentManager, TAG_TIME_EXPIRED_DIALOG)
        oneLinkAlertDialogsFragment.isCancelable = false
    }

    override fun onPositiveButtonClicked(targetCode: Int) {
        super.onPositiveButtonClicked(targetCode)
        context?.let {
            LocaleManager.setNewLocale(it, selectedLang)
            LocaleManager.setLanguageSetBoolPref(it, true)
        }
//        if (isFromSplash) activity?.let { SplashActivity.start(it) }
//        else
        activity?.let { HomeActivity.start(it) }
    }

    override fun onItemClicked(bool: Boolean, lang: String) {
        binding.btnsaveLanguge.isEnabled = LocaleManager.getLanguagePref(context) != lang
        if (isFromSplash) binding.btnsaveLanguge.isEnabled = true
        selectedLang = lang

    }
}