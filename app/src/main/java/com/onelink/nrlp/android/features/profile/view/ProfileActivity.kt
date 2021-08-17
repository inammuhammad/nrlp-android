package com.onelink.nrlp.android.features.profile.view

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.ActivityProfileBinding
import com.onelink.nrlp.android.features.beneficiary.fragments.ManageBeneficiaryFragment
import com.onelink.nrlp.android.features.beneficiary.viewmodel.BeneficiaryViewModel
import com.onelink.nrlp.android.features.profile.fragments.EditProfileFragment
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import kotlinx.android.synthetic.main.beneficiary_activity.*
import javax.inject.Inject

class ProfileActivity :
    BaseFragmentActivity<ActivityProfileBinding, BeneficiaryViewModel>(BeneficiaryViewModel::class.java),
    SelectCountryFragment.OnSelectCountryListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var countriesAdpaterCallBack: SelectCountryFragment.OnSelectCountryListener


    override fun getLayoutRes() = R.layout.activity_profile

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is EditProfileFragment) {
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
                    it.onResume()
            }
        }

        addFragment(
            EditProfileFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }


    override fun onSelectCountryListener(countryCodeModel: CountryCodeModel) {
        countriesAdpaterCallBack.onSelectCountryListener(countryCodeModel)
    }

    companion object {
        @Suppress("unused")
        fun newProfileActivityIntent(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }
    }
}