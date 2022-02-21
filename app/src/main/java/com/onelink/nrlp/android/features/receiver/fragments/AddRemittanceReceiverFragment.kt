package com.onelink.nrlp.android.features.receiver.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.databinding.FragmentAddRemittanceReceiverBinding
import com.onelink.nrlp.android.features.home.view.HomeActivity
import com.onelink.nrlp.android.features.login.view.LoginActivity
import com.onelink.nrlp.android.features.receiver.view.ReceiverActivity
import com.onelink.nrlp.android.features.receiver.viewmodel.ReceiverSharedViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class AddRemittanceReceiverFragment : BaseFragment<ReceiverSharedViewModel, FragmentAddRemittanceReceiverBinding>
(ReceiverSharedViewModel::class.java){

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    override fun getTitle() = resources.getString(R.string.remittance_receiver_manager)

    override fun getLayoutRes() = R.layout.fragment_add_remittance_receiver

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): ReceiverSharedViewModel = ViewModelProvider(
        this, viewModelFactory
    ).get(ReceiverSharedViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.lifecycleOwner = this
        initListeners()
    }

    private fun initListeners(){
        oneLinkProgressDialog.hideProgressDialog()
        binding.btnNext.setOnSingleClickListener {
            fragmentHelper.addFragment(
                ReceiverTypeFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
        }
        binding.btnSkip.setOnSingleClickListener {
            fragmentHelper.onBack()
        }
    }

    companion object{
        @JvmStatic
        fun newInstance() = AddRemittanceReceiverFragment()
    }

}