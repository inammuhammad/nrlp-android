package com.onelink.nrlp.android.features.receiver.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.ActivityReceiverBinding
import com.onelink.nrlp.android.features.beneficiary.adapter.BeneficiariesAdapter
import com.onelink.nrlp.android.features.beneficiary.fragments.BeneficiaryDetailsFragment
import com.onelink.nrlp.android.features.beneficiary.fragments.ManageBeneficiaryFragment
import com.onelink.nrlp.android.features.beneficiary.view.BeneficiaryActivity
import com.onelink.nrlp.android.features.home.fragments.FatherNameVerificationFragment
import com.onelink.nrlp.android.features.home.fragments.NadraVerificationRequiredFragment
import com.onelink.nrlp.android.features.home.fragments.popup.GeneralInfoFragment
import com.onelink.nrlp.android.features.home.view.TAG_CONFIRM_LOGOUT_DIALOG
import com.onelink.nrlp.android.features.login.view.LoginActivity
import com.onelink.nrlp.android.features.receiver.adapter.ReceiversAdapter
import com.onelink.nrlp.android.features.receiver.fragments.AddRemittanceReceiverFragment
import com.onelink.nrlp.android.features.receiver.fragments.ManageReceiverFragment
import com.onelink.nrlp.android.features.receiver.fragments.ReceiverDetailsFragment
import com.onelink.nrlp.android.features.receiver.fragments.ReceiverTypeFragment
import com.onelink.nrlp.android.features.receiver.models.ReceiverDetailsModel
import com.onelink.nrlp.android.features.receiver.viewmodel.ReceiverViewModel
import com.onelink.nrlp.android.features.select.bank.model.BankDetailsModel
import com.onelink.nrlp.android.features.select.bank.view.SelectBankFragment
import com.onelink.nrlp.android.features.select.city.model.CitiesModel
import com.onelink.nrlp.android.features.select.city.view.SelectCityFragment
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.toSpanned
import kotlinx.android.synthetic.main.beneficiary_activity.*
import javax.inject.Inject

class ReceiverActivity :
    BaseFragmentActivity<ActivityReceiverBinding, ReceiverViewModel>(ReceiverViewModel::class.java),
    OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners,
    ReceiversAdapter.ClickEventHandler, SelectCountryFragment.OnSelectCountryListener,
    SelectCityFragment.OnSelectCityListener, SelectBankFragment.OnSelectBankListener {

    var isFromHome = false

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var receiverAdapterCallBack: ReceiversAdapter.ClickEventHandler
    private lateinit var countriesAdpaterCallBack: SelectCountryFragment.OnSelectCountryListener
    private lateinit var banksAdpaterCallBack: SelectBankFragment.OnSelectBankListener
    lateinit var listenerCity: SelectCityFragment.OnSelectCityListener

    override fun getLayoutRes() = R.layout.activity_receiver

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is ManageReceiverFragment) {
            receiverAdapterCallBack = fragment
        }
        if (fragment is ReceiverDetailsFragment) {
            countriesAdpaterCallBack = fragment
            listenerCity = fragment
            banksAdpaterCallBack = fragment
        }
    }

    override fun initViewModel(viewModel: ReceiverViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { onBack() })
        toolbar.showBorderView(true)
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                binding.toolbar.setTitle(it.getTitle())
                if (it is ManageReceiverFragment)
                    it.refresh()
            }
        }

        initView()
        initObservers()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                if (it !is AddRemittanceReceiverFragment) {
                    onBack()
                }
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun initView() {
        isFromHome = intent.getBooleanExtra("isFromHomeScreen", false)

        if (isFromHome) {
            addFragment(
                AddRemittanceReceiverFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
        } else {
            addFragment(
                ManageReceiverFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
        }
        setLeftButtonVisibility()
    }

    private fun setLeftButtonVisibility() {
        val fragment = getCurrentFragment() as BaseFragment<*, *>?
        fragment?.let {
            if (it is AddRemittanceReceiverFragment)
                binding.toolbar.setLeftButtonVisible(false)
            else
                binding.toolbar.setLeftButtonVisible(true)
        }
    }

    private fun initObservers() {
        viewModel.observeLogout().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS, Status.ERROR -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    UserData.emptyUserData()
                    launchLoginActivity()
                }
                Status.LOADING -> {
                    oneLinkProgressDialog.showProgressDialog(this)
                }
            }
        })
    }

    fun showLogoutConfirmationDialog() {
        OneLinkAlertDialogsFragment.Builder().setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_error_dialog).setIsAlertOnly(false)
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.nadra_confirmation_msg).toSpanned())
            .setNeutralButtonText(getString(R.string.okay))
            .setPositiveButtonText(resources.getString(R.string.yes))
            .setNegativeButtonText(resources.getString(R.string.no)).setCancelable(true)
            .setCancelable(false)
            .show(supportFragmentManager, TAG_CONFIRM_LOGOUT_DIALOG)
    }

    fun launchLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onSelectCountryListener(countryCodeModel: CountryCodeModel) {
        countriesAdpaterCallBack.onSelectCountryListener(countryCodeModel)
    }

    override fun onSelectCityListener(citiesModel: CitiesModel) {
        listenerCity.onSelectCityListener(citiesModel)
    }

    override fun onSelectBankListener(bankDetailsModel: BankDetailsModel) {
        banksAdpaterCallBack.onSelectBankListener(bankDetailsModel)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, ReceiverActivity::class.java)
        }
    }

    override fun onReceiverSelected(receiverDetailsModel: ReceiverDetailsModel) {
        receiverAdapterCallBack.onReceiverSelected(receiverDetailsModel)
    }

    override fun onPositiveButtonClicked(targetCode: Int) {
        super.onPositiveButtonClicked(targetCode)
        viewModel.performLogout()
    }

}