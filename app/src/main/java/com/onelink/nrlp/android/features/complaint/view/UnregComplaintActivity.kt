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
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.ActivityComplaintBinding
import com.onelink.nrlp.android.features.complaint.fragment.unregistered.UnregComplaintDetailsFragment
import com.onelink.nrlp.android.features.complaint.fragment.unregistered.UserTypeComplaintFragment
import com.onelink.nrlp.android.features.complaint.viewmodel.UnRegComplaintSharedViewModel
import com.onelink.nrlp.android.features.complaint.viewmodel.ComplaintViewModel
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import kotlinx.android.synthetic.main.beneficiary_activity.*
import javax.inject.Inject

class UnregComplaintActivity:
BaseFragmentActivity<ActivityComplaintBinding,ComplaintViewModel>(ComplaintViewModel::class.java),
    SelectCountryFragment.OnSelectCountryListener
{
    @Inject
    lateinit var viewModelFactory:ViewModelProvider.Factory
    private lateinit var complainSharedViewModelUnReg : UnRegComplaintSharedViewModel
    lateinit var listener: SelectCountryFragment.OnSelectCountryListener
    val selectedCountry = MutableLiveData<CountryCodeModel>()
    override fun getLayoutRes() = R.layout.activity_complaint

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: ComplaintViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { onBack() })
        toolbar.showBorderView(true)
        complainSharedViewModelUnReg = ViewModelProvider(this,viewModelFactory).get(UnRegComplaintSharedViewModel::class.java)
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
        }else if(fragment is UnregComplaintDetailsFragment){
            listener = fragment
        }

    }

    companion object {
        fun newUnRegisteredComplaintIntent(context: Context): Intent {
            return Intent(context, UnregComplaintActivity::class.java)
        }

        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, UnregComplaintActivity::class.java))
            activity.finishAffinity()
        }
    }

    override fun onSelectCountryListener(countryCodeModel: CountryCodeModel) {
        listener.onSelectCountryListener(countryCodeModel)
    }

}