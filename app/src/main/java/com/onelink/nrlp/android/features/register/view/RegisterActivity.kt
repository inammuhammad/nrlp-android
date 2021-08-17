package com.onelink.nrlp.android.features.register.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.ActivityRegisterContainerBinding
import com.onelink.nrlp.android.features.register.fragments.*
import com.onelink.nrlp.android.features.register.viewmodel.RegisterViewModel
import com.onelink.nrlp.android.features.register.viewmodel.SharedViewModel
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import com.onelink.nrlp.android.utils.Constants
import kotlinx.android.synthetic.main.activity_register_container.*
import javax.inject.Inject

/**
 * Created by Umar Javed.
 */
const val REGISTER_ACCOUNT_FLOW_NUMBER = 1
const val REMITTER_VERIFICATION_FLOW_NUMBER = 2
const val BENEFICIARY_VERIFICATION_FLOW_NUMBER = 2
const val OTP_VERIFICATION_FLOW_NUMBER = 3
const val TERMS_AND_CONDITIONS_FLOW_NUMBER = 4

class RegisterActivity :
    BaseFragmentActivity<ActivityRegisterContainerBinding, RegisterViewModel>(RegisterViewModel::class.java),
    SelectCountryFragment.OnSelectCountryListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var sharedViewModel: SharedViewModel
    lateinit var listener: SelectCountryFragment.OnSelectCountryListener

    override fun getLayoutRes() = R.layout.activity_register_container

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: RegisterViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { onBack() })
        web_view_progress_bar.max = 3
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                binding.toolbar.setTitle(it.getTitle())
                if (it !is TermsAndConditionsFragment)
                    binding.toolbar.setLeftButtonVisible(true)
                else
                    binding.toolbar.setLeftButtonVisible(false)
                if (it is RegisterAccountFragment) {
                    adjustWindowResize()
                } else {
                    adjustWindowPan()
                }
                when (it) {
                    is RegisterAccountFragment -> web_view_progress_bar.progress =
                        REGISTER_ACCOUNT_FLOW_NUMBER
                    is VerifyRemitterFragment -> web_view_progress_bar.progress =
                        REMITTER_VERIFICATION_FLOW_NUMBER
                    is VerifyBeneficiaryFragment -> web_view_progress_bar.progress =
                        BENEFICIARY_VERIFICATION_FLOW_NUMBER
                    is OtpAuthenticationFragment -> web_view_progress_bar.progress =
                        OTP_VERIFICATION_FLOW_NUMBER
                    is TermsAndConditionsFragment -> web_view_progress_bar.progress =
                        TERMS_AND_CONDITIONS_FLOW_NUMBER
                }
            }
        }
        addFragment(
            RegisterAccountFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
        initObservers()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is SelectCountryFragment) {
            fragment.setOnClickListener(this)
        } else if (fragment is RegisterAccountFragment) {
            listener = fragment
        }
    }

    private fun initObservers() {
        sharedViewModel.maxProgress.observe(this, Observer {
            it?.let {
                web_view_progress_bar.max = it
            }
        })

        sharedViewModel.startRegisterSuccessActivity.observe(this, Observer {
            it?.let {
                if (it) {
                    val intent = RegisterSuccessActivity.newRegisterSuccessIntent(this)
                    intent.putExtra(
                        Constants.INTENT_KEY_ACCOUNT_TYPE,
                        sharedViewModel.registerFlowDataModel.value?.accountType
                    )
                    startActivity(intent)
                }
            }
        })
    }

    override fun onFragmentBackStackChanged() {
        super.onFragmentBackStackChanged()
        when (getCurrentFragment()) {
            RegisterAccountFragment() -> web_view_progress_bar.progress = 1
            VerifyRemitterFragment() -> web_view_progress_bar.progress = 2
            OtpAuthenticationFragment() -> web_view_progress_bar.progress = 3
        }
    }

    companion object {
        fun newRegisterUserIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java)
        }
    }

    override fun onSelectCountryListener(countryCodeModel: CountryCodeModel) {
        listener.onSelectCountryListener(countryCodeModel)
    }

    @Suppress("DEPRECATION")
    private fun adjustWindowResize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)
        } else {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    private fun adjustWindowPan() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                if (it !is TermsAndConditionsFragment) {
                    onBack()
                }
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
