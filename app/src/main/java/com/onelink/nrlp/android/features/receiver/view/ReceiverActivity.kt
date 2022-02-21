package com.onelink.nrlp.android.features.receiver.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.ActivityReceiverBinding
import com.onelink.nrlp.android.features.beneficiary.adapter.BeneficiariesAdapter
import com.onelink.nrlp.android.features.beneficiary.fragments.BeneficiaryDetailsFragment
import com.onelink.nrlp.android.features.beneficiary.fragments.ManageBeneficiaryFragment
import com.onelink.nrlp.android.features.beneficiary.view.BeneficiaryActivity
import com.onelink.nrlp.android.features.receiver.fragments.AddRemittanceReceiverFragment
import com.onelink.nrlp.android.features.receiver.fragments.ManageReceiverFragment
import com.onelink.nrlp.android.features.receiver.fragments.ReceiverDetailsFragment
import com.onelink.nrlp.android.features.receiver.fragments.ReceiverTypeFragment
import com.onelink.nrlp.android.features.receiver.viewmodel.ReceiverViewModel
import com.onelink.nrlp.android.features.select.city.model.CitiesModel
import com.onelink.nrlp.android.features.select.city.view.SelectCityFragment
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import com.onelink.nrlp.android.models.BeneficiaryDetailsModel
import kotlinx.android.synthetic.main.beneficiary_activity.*
import javax.inject.Inject

class ReceiverActivity :
    BaseFragmentActivity<ActivityReceiverBinding, ReceiverViewModel>(ReceiverViewModel::class.java),
    BeneficiariesAdapter.ClickEventHandler, SelectCountryFragment.OnSelectCountryListener,
    SelectCityFragment.OnSelectCityListener {

    var isFromHome = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var beneficiaryAdpaterCallBack: BeneficiariesAdapter.ClickEventHandler
    private lateinit var countriesAdpaterCallBack: SelectCountryFragment.OnSelectCountryListener
    lateinit var listenerCity: SelectCityFragment.OnSelectCityListener

    override fun getLayoutRes() = R.layout.activity_receiver

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is ManageReceiverFragment) {
            beneficiaryAdpaterCallBack = fragment
        }
        if (fragment is ReceiverDetailsFragment) {
            countriesAdpaterCallBack = fragment
            listenerCity = fragment
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
    }

    private fun initView() {
        isFromHome = intent.getBooleanExtra("isFromHomeScreen", false)

        if(isFromHome) {
            addFragment(
                AddRemittanceReceiverFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
        }
        else{
            addFragment(
                ManageReceiverFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
        }
    }

    override fun onBeneficiarySelected(beneficiaryDetailsModel: BeneficiaryDetailsModel) {
        beneficiaryAdpaterCallBack.onBeneficiarySelected(beneficiaryDetailsModel)
    }

    override fun onSelectCountryListener(countryCodeModel: CountryCodeModel) {
        countriesAdpaterCallBack.onSelectCountryListener(countryCodeModel)
    }

    override fun onSelectCityListener(citiesModel: CitiesModel) {
        listenerCity.onSelectCityListener(citiesModel)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, ReceiverActivity::class.java)
        }
    }

}