package com.onelink.nrlp.android.features.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentFatherNameVerificationBinding
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.setOnSingleClickListener
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

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.btnNext.setOnSingleClickListener {
            viewModel.verifyFatherName(binding.etFatherName.text.toString())
        }
    }

    private fun initObservers() {
        viewModel.observeVerifyFatherName().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                }
                Status.LOADING -> {
                    oneLinkProgressDialog.showProgressDialog(context)
                }
                Status.ERROR -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.error?.let { showGeneralErrorDialog(this, it) }
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = FatherNameVerificationFragment()
    }
}