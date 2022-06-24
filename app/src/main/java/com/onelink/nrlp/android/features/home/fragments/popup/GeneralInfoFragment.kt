package com.onelink.nrlp.android.features.home.fragments.popup

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.features.home.fragments.HomeFragmentViewModel
import com.onelink.nrlp.android.databinding.FragmentGeneralInfoBinding
import com.onelink.nrlp.android.features.home.view.HomeActivity
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class GeneralInfoFragment : BaseFragment<HomeFragmentViewModel, FragmentGeneralInfoBinding>
    (HomeFragmentViewModel::class.java) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    override fun getLayoutRes(): Int = R.layout.fragment_general_info

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): HomeFragmentViewModel = ViewModelProvider(
        this, viewModelFactory
    ).get(HomeFragmentViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.lifecycleOwner = this
        (activity as HomeActivity).hideHomeScreenTools()
        initListeners()
        initObservers()
    }

    private fun initListeners() {

        binding.btnNext.setOnSingleClickListener {
            activity?.onBackPressed()
        }
    }

    private fun initObservers() {

    }

    companion object {
        @JvmStatic
        fun newInstance() = GeneralInfoFragment()
    }
}