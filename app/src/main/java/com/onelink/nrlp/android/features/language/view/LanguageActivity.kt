package com.onelink.nrlp.android.features.language.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.ActivityLanguageBinding
import com.onelink.nrlp.android.features.language.viewmodel.LanguageActivityViewModel
import com.onelink.nrlp.android.utils.IntentConstants
import javax.inject.Inject

class LanguageActivity : BaseFragmentActivity<ActivityLanguageBinding, LanguageActivityViewModel>(
    LanguageActivityViewModel::class.java
) {
    override fun getLayoutRes() = R.layout.activity_language
    private var isFromSplash: Boolean = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory
    override fun initViewModel(viewModel: LanguageActivityViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { onBack() })
        binding.toolbar.showBorderView(true)
        isFromSplash = intent.getBooleanExtra(IntentConstants.IS_FROM_SPLASH, false)
        if (isFromSplash) binding.toolbar.setLeftButtonVisible(false)
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                binding.toolbar.setTitle(it.getTitle())
            }
        }
        addFragment(
            LanguageFragment.newInstance(isFromSplash),
            clearBackStack = true,
            addToBackStack = true
        )
    }

    companion object {

        fun start(activity: Activity, isFromSplash: Boolean = false) {
            activity.startActivity(Intent(activity, LanguageActivity::class.java).apply {
                putExtra(IntentConstants.IS_FROM_SPLASH, isFromSplash)
            })
            if (isFromSplash) activity.finish()
        }

        @Suppress("unused")
        fun newLanguageIntent(context: Context): Intent {
            return Intent(context, LanguageActivity::class.java)
        }
    }
}