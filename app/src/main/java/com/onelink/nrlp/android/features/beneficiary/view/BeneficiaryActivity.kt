package com.onelink.nrlp.android.features.beneficiary.view

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.BeneficiaryActivityBinding
import com.onelink.nrlp.android.features.beneficiary.adapter.BeneficiariesAdapter
import com.onelink.nrlp.android.features.beneficiary.fragments.BeneficiaryDetailsFragment
import com.onelink.nrlp.android.features.beneficiary.fragments.ManageBeneficiaryFragment
import com.onelink.nrlp.android.features.beneficiary.viewmodel.BeneficiaryViewModel
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import com.onelink.nrlp.android.models.BeneficiaryDetailsModel
import kotlinx.android.synthetic.main.beneficiary_activity.*
import javax.inject.Inject

class BeneficiaryActivity :
    BaseFragmentActivity<BeneficiaryActivityBinding, BeneficiaryViewModel>(BeneficiaryViewModel::class.java),
    BeneficiariesAdapter.ClickEventHandler, SelectCountryFragment.OnSelectCountryListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var beneficiaryAdpaterCallBack: BeneficiariesAdapter.ClickEventHandler
    private lateinit var countriesAdpaterCallBack: SelectCountryFragment.OnSelectCountryListener


    override fun getLayoutRes() = R.layout.beneficiary_activity

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is ManageBeneficiaryFragment) {
            beneficiaryAdpaterCallBack = fragment
        }
        if (fragment is BeneficiaryDetailsFragment) {
            countriesAdpaterCallBack = fragment
        }
    }

    override fun initViewModel(viewModel: BeneficiaryViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { onBack() })
        toolbar.showBorderView(true)
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                binding.toolbar.setTitle(it.getTitle())
                if (it is ManageBeneficiaryFragment)
                    it.refresh()
            }
        }

        addFragment(
            ManageBeneficiaryFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

    override fun onBeneficiarySelected(beneficiaryDetailsModel: BeneficiaryDetailsModel) {
        beneficiaryAdpaterCallBack.onBeneficiarySelected(beneficiaryDetailsModel)
    }

    override fun onSelectCountryListener(countryCodeModel: CountryCodeModel) {
        countriesAdpaterCallBack.onSelectCountryListener(countryCodeModel)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, BeneficiaryActivity::class.java)
        }
    }
}