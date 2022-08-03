package com.onelink.nrlp.android.features.complaint.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.ActivityComplaintBinding
import com.onelink.nrlp.android.features.complaint.fragment.registered.RegComplaintCameraFragment
import com.onelink.nrlp.android.features.complaint.fragment.registered.RegComplaintDetailsFragment
import com.onelink.nrlp.android.features.complaint.fragment.registered.RegComplaintTypeFragment
import com.onelink.nrlp.android.features.complaint.viewmodel.ComplaintViewModel
import com.onelink.nrlp.android.features.complaint.viewmodel.RegComplaintSharedViewModel
import com.onelink.nrlp.android.features.home.fragments.FatherNameVerificationFragment
import com.onelink.nrlp.android.features.home.fragments.NadraVerificationRequiredFragment
import com.onelink.nrlp.android.features.home.fragments.popup.GeneralInfoFragment
import com.onelink.nrlp.android.features.select.banksandexchange.view.SelectBanksAndExchangeFragment
import com.onelink.nrlp.android.features.select.city.model.CitiesModel
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import com.onelink.nrlp.android.features.select.generic.model.BranchCenterModel
import com.onelink.nrlp.android.features.select.generic.view.SelectBranchCenterFragment
import kotlinx.android.synthetic.main.beneficiary_activity.*
import javax.inject.Inject

class RegComplaintActivity:
    BaseFragmentActivity<ActivityComplaintBinding, ComplaintViewModel>(ComplaintViewModel::class.java),
    SelectCountryFragment.OnSelectCountryListener,
    SelectBranchCenterFragment.OnSelectBranchCenterListener,
    SelectBanksAndExchangeFragment.OnSelectBanksExchangeListener
{
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var complainSharedViewModelReg : RegComplaintSharedViewModel
    lateinit var listener: SelectCountryFragment.OnSelectCountryListener
    lateinit var listenerBranchCenter: SelectBranchCenterFragment.OnSelectBranchCenterListener
    lateinit var listenerBanksAndExchange: SelectBanksAndExchangeFragment.OnSelectBanksExchangeListener
    val selectedCountry = MutableLiveData<CountryCodeModel>()
    override fun getLayoutRes() = R.layout.activity_complaint

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: ComplaintViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { keyEventSender() })
        toolbar.showBorderView(true)
        complainSharedViewModelReg =
            ViewModelProvider(this, viewModelFactory).get(RegComplaintSharedViewModel::class.java)
        binding.toolbar.setTitle("")
        hideToolbar()

        addFragment(
            RegComplaintTypeFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

    fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    fun showToolbar() {
        binding.toolbar.visibility = View.VISIBLE
    }

    private fun hideToolbar() {
        binding.toolbar.visibility = View.GONE
    }

    fun keyEventSender() {
        dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK))
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is SelectCountryFragment) {
            fragment.setOnClickListener(this)
        } else if (fragment is RegComplaintDetailsFragment) {
            listener = fragment
            listenerBranchCenter = fragment
            listenerBanksAndExchange = fragment
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                if (it is RegComplaintCameraFragment)
                    hideToolbar()
                onBack()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        fun newRegisteredComplaintIntent(context: Context): Intent {
            return Intent(context, RegComplaintActivity::class.java)
        }

        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, RegComplaintActivity::class.java))
            activity.finishAffinity()
        }
    }

    override fun onSelectCountryListener(countryCodeModel: CountryCodeModel) {
        listener.onSelectCountryListener(countryCodeModel)
    }

    override fun onSelectBranchCenterListener(branchCenterModel: BranchCenterModel) {
        listenerBranchCenter.onSelectBranchCenterListener(branchCenterModel)
    }

    override fun onSelectBanksExchangeListener(banksAndExchangeModel: String) {
        listenerBanksAndExchange.onSelectBanksExchangeListener(banksAndExchangeModel)
    }

}