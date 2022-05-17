package com.onelink.nrlp.android.features.home.fragments

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.databinding.FragmentFatherNameVerificationBinding
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class FatherNameVerificationFragment : BaseFragment<HomeFragmentViewModel, FragmentFatherNameVerificationBinding>
    (HomeFragmentViewModel::class.java) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    override fun getLayoutRes(): Int = R.layout.fragment_father_name_verification

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): HomeFragmentViewModel = ViewModelProvider(
        this, viewModelFactory
    ).get(HomeFragmentViewModel::class.java)

    companion object {
        @JvmStatic
        fun newInstance() = FatherNameVerificationFragment()
    }
}