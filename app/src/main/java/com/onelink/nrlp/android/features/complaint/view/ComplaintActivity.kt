package com.onelink.nrlp.android.features.complaint.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.ActivityComplaintBinding
import com.onelink.nrlp.android.features.beneficiary.fragments.ManageBeneficiaryFragment
import com.onelink.nrlp.android.features.complaint.fragment.ComplaintDetailsFragment
import com.onelink.nrlp.android.features.complaint.fragment.UserTypeComplaintFragment
import com.onelink.nrlp.android.features.complaint.viewmodel.ComplaintSharedViewModel
import com.onelink.nrlp.android.features.complaint.viewmodel.ComplaintViewModel
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import kotlinx.android.synthetic.main.beneficiary_activity.*
import javax.inject.Inject

class ComplaintActivity:
BaseFragmentActivity<ActivityComplaintBinding,ComplaintViewModel>(ComplaintViewModel::class.java),
    SelectCountryFragment.OnSelectCountryListener
{
    @Inject
    lateinit var viewModelFactory:ViewModelProvider.Factory
    private lateinit var complainSharedViewModel : ComplaintSharedViewModel
    lateinit var listener: SelectCountryFragment.OnSelectCountryListener
    val selectedCountry = MutableLiveData<CountryCodeModel>()
    override fun getLayoutRes() = R.layout.activity_complaint

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: ComplaintViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { onBack() })
        toolbar.showBorderView(true)
        complainSharedViewModel = ViewModelProvider(this,viewModelFactory).get(ComplaintSharedViewModel::class.java)
        binding.toolbar.setTitle(getString(R.string.complaint))

        addFragment(
            UserTypeComplaintFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is SelectCountryFragment) {
            fragment.setOnClickListener(this)
        }else if(fragment is ComplaintDetailsFragment){
            listener = fragment
        }

    }

    companion object {
        fun newUnRegisteredComplaintIntent(context: Context): Intent {
            return Intent(context, ComplaintActivity::class.java)
        }

        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, ComplaintActivity::class.java))
            activity.finishAffinity()
        }
    }

    override fun onSelectCountryListener(countryCodeModel: CountryCodeModel) {
        //selectedCountry.postValue(countryCodeModel)
        listener.onSelectCountryListener(countryCodeModel)
    }

    private fun adjustWindowPan() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

}